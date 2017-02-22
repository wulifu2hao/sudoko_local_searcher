import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by lifu.wu on 20/2/17.
 */
public class OptimisedSolver {
    public static final int MAX_ITERATION = 200;
    public static long randomSeed = 8006738581902217899L;
    public static Random random = new Random(randomSeed);
    public static Random randomForShuffle = new Random(randomSeed);

    public static boolean solve(SudokuProblem problem, int maxIteration){

        ArrayList<Position> unknownPositions = new ArrayList<Position>();
        if (maxIteration == 0) {
            maxIteration = MAX_ITERATION;
        }

        ArrayList<Integer> numberAvailableCounts = new ArrayList<Integer>(problem.problemSize);
        for (int i=0; i<problem.problemSize;i++) {
            numberAvailableCounts.add(problem.problemSize);
        }
        Integer numberQuotaCount[] = new Integer[numberAvailableCounts.size()];
        numberQuotaCount = numberAvailableCounts.toArray(numberQuotaCount);

        for (int i=0; i<problem.problemSize;i++) {
            for (int j=0; j<problem.problemSize;j++){
                if (problem.isUnknownPosition(i, j)) {
                    Position position = new Position(i, j);
                    unknownPositions.add(position);
                } else {
                    numberQuotaCount[problem.getValueAtPosition(i, j)-1]--;
                }
            }
        }

        Printer.printlnIfVerbose(String.format("solving problem with %d unknown positions",unknownPositions.size()));

        ArrayList<Integer> numbersForUse = getAvailableNumbers(numberQuotaCount);
        randomAssignment(problem, unknownPositions,numbersForUse);

        if (unknownPositions.size() <= 1) {
            return true;
        }

        int[] scores = initAllSwapOptionsScores(unknownPositions);
        PositionPair[] swapOptions = getAllSwapOptions(unknownPositions);

        for (int retryIdx=0; retryIdx<100000; retryIdx++){
            randomAssignment(problem, unknownPositions,numbersForUse);

            int iterationsNum = 1;
            while (!problem.solved() && iterationsNum < maxIteration) {
                updateSwapOptionsScores(problem, swapOptions, scores);

//                int swapOptionIdx = findSwapOptionByScore(scores);
                int swapOptionIdx = findSwapOptionByScoreWithProbability(scores);
                shuffle(problem, swapOptions[swapOptionIdx]);
                iterationsNum++;
            }

//            problem.printBoard();
//            problem.printConsistencies();

            if (problem.solved()) {
                Printer.printlnIfVerbose("succeeded after "+retryIdx+" retry and stop at "+iterationsNum+" iterations");
                return true;
            }
        }

        resetProblem(problem, unknownPositions);
        return false;
    }

    public static int randomInt(int minInclusive, int maxExclusive){
        int randomNum = random.nextInt(maxExclusive-minInclusive);
        return randomNum + minInclusive;
    }

    public static void shuffle(SudokuProblem problem, PositionPair pair) {
        problem.shuffle(pair.p1, pair.p2);
//        Printer.printlnIfVerbose(String.format("shuffling (%d,%d) with (%d,%d)", unknownPosition1.i, unknownPosition1.j,unknownPosition2.i, unknownPosition2.j));
    }

    public static void randomAssignment(SudokuProblem problem, ArrayList<Position> unknownPositions, ArrayList<Integer> availableNumbers) {
//        TODO: this shuffling is not deterministic..
        Collections.shuffle(availableNumbers, randomForShuffle);
        for (int i=0; i<unknownPositions.size(); i++) {
            Position position = unknownPositions.get(i);
            problem.setValueAtPosition(position.i, position.j, availableNumbers.get(i));
        }
        problem.computeAllConsistencies();
    }

    public static void resetProblem(SudokuProblem problem, ArrayList<Position> unknownPositions) {
        for (Position unknownPosition : unknownPositions) {
            problem.setValueAtPosition(unknownPosition.i, unknownPosition.j, 0);
        }
        problem.resetAllConsistencies();
    }

    public static ArrayList<Integer> getAvailableNumbers(Integer[] numberQuotaCount) {
        ArrayList<Integer> numberForUse = new ArrayList<Integer>();
        for(int i=0; i<numberQuotaCount.length; i++){
            for(int j=0; j<numberQuotaCount[i]; j++){
                numberForUse.add(i+1);
            }
        }
        return numberForUse;
    }

    public static int calculateShuffleScore(SudokuProblem problem, Position p1, Position p2){
        return problem.calculateShuffleScore(p1, p2);
    }

    public static int findSwapOptionByScore(int[] scores){
        int bestIdx = 0;
        for (int i=1; i<scores.length; i++) {
            if (scores[i] > scores[bestIdx]){
                bestIdx = i;
            }
        }
        return bestIdx;
    }

    public static int findSwapOptionByScoreWithProbability(int[] scores){
        int[] helperArray = new int[scores.length];
        int bestIdx = 0;
        if (scores[0] > 0){
            helperArray[0] = scores[0];
        } else {
            helperArray[0] = 0;
        }

        for (int i=1; i<scores.length; i++) {
            if (scores[i] > scores[bestIdx]){
                bestIdx = i;
            }
            if (scores[i] > 0){
                helperArray[i] = helperArray[i-1] + scores[i];
            } else {
                helperArray[i] = helperArray[i-1];
            }
        }

        if (helperArray[helperArray.length-1] == 0) {
            return bestIdx;
        }

        int randInteger = randomInt(0, helperArray[helperArray.length-1]);
        for (int i=0; i<helperArray.length; i++) {
            if (helperArray[i] > randInteger) {
                return i;
            }
        }

        return bestIdx;
    }

    public static PositionPair[] getAllSwapOptions(ArrayList<Position> unknownPositions){
        int numPossibleSwaps = unknownPositions.size()*(unknownPositions.size()-1)/2;
        PositionPair[] swapOptions = new PositionPair[numPossibleSwaps];
        int optionIdx = 0;
        for (int i=0; i<unknownPositions.size(); i++){
            for(int j=i+1; j<unknownPositions.size(); j++){
                swapOptions[optionIdx] = new PositionPair(unknownPositions.get(i), unknownPositions.get(j));
                optionIdx++;
            }
        }

        return swapOptions;
    }

    public static int[] initAllSwapOptionsScores(ArrayList<Position> unknownPositions){
        int numPossibleSwaps = unknownPositions.size()*(unknownPositions.size()-1)/2;
        int[] scores = new int[numPossibleSwaps];
        for (int i=0; i<numPossibleSwaps; i++){
            scores[i] = 0;
        }

        return scores;
    }

    public static void updateSwapOptionsScores(SudokuProblem problem, PositionPair[] options, int[] scores ){
        for(int i=0; i<options.length; i++){
                if (problem.isInTabuList(options[i])) {
                    scores[i] = SudokuProblem.FAKE_NEGATIVE_INFINITITY;
                } else {
                    scores[i] = calculateShuffleScore(problem, options[i].p1, options[i].p2);
                }
        }
    }
}
