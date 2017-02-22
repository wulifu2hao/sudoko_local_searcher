import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by lifu.wu on 20/2/17.
 */
public class URSolver {
    public static Random random;

    public static boolean solve(SudokuProblem problem ,long randomSeed){
        random = new Random(randomSeed);

        ArrayList<Position> unknownPositions = new ArrayList<Position>();

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

        if (unknownPositions.size() == 0) {
            return true;
        }

        Printer.printlnIfVerbose(String.format("solving problem with %d unknown positions",unknownPositions.size()));

        ArrayList<Integer> numbersForUse = getAvailableNumbers(numberQuotaCount);
        randomAssignment(problem, unknownPositions,numbersForUse);

        int iterationsNum = 1;
        while (!problem.solved()) {
            int indexOfUnknownPosition1 =randomInt(0, unknownPositions.size());
            int indexOfUnknownPosition2 =randomInt(0, unknownPositions.size());
            randomShuffle(problem, unknownPositions.get(indexOfUnknownPosition1), unknownPositions.get(indexOfUnknownPosition2));
            iterationsNum++;
        }

        if (problem.solved()) {
            Printer.printlnIfVerbose("succeeded after "+iterationsNum+" iterations");
            return true;
        }

        resetProblem(problem, unknownPositions);
        return false;
    }

    public static int randomInt(int minInclusive, int maxExclusive){
        int randomNum = random.nextInt(maxExclusive-minInclusive);
        return randomNum + minInclusive;
    }

    public static void randomShuffle(SudokuProblem problem, Position unknownPosition1, Position unknownPosition2) {
        problem.shuffle(unknownPosition1, unknownPosition2);
//        Printer.printlnIfVerbose(String.format("shuffling (%d,%d) with (%d,%d)", unknownPosition1.i, unknownPosition1.j,unknownPosition2.i, unknownPosition2.j));
    }

    public static void randomAssignment(SudokuProblem problem, ArrayList<Position> unknownPositions, ArrayList<Integer> availableNumbers) {
        Collections.shuffle(availableNumbers);
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
}
