import javafx.geometry.Pos;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by lifu.wu on 19/2/17.
 */
public class URSolver {

    public static final int MAX_ITERATION = 10000;
    public static Random random = new Random();

    static class Position {
        int i, j;

        public Position(int i, int j){
            this.i = i;
            this.j = j;
        }
    }

    public static boolean solve(SudokuProblem problem, int maxIteration){
        ArrayList<Position> unknownPositions = new ArrayList<Position>();
        if (maxIteration == 0) {
            maxIteration = MAX_ITERATION;
        }

        for (int i=0; i<problem.problemSize;i++) {
            for (int j=0; j<problem.problemSize;j++){
                if (problem.board[i][j] == 0) {
                    Position position = new Position(i, j);
                    unknownPositions.add(position);
                }
            }
        }

        if (unknownPositions.size() == 0) {
            return true;
        }

        Printer.printlnIfVerbose(String.format("solving problem with %d unknown positions",unknownPositions.size()));

        randomAssignment(problem, unknownPositions);
        int iterationsNum = 1;
        while (!problem.solved() && iterationsNum < maxIteration) {
            int indexOfUnknownPositionsToFlip =randomInt(0, unknownPositions.size());
            Position unknownPosition = unknownPositions.get(indexOfUnknownPositionsToFlip);
            randomFlip(problem, unknownPosition);
            iterationsNum++;
        }

        if (problem.solved()) {
            return true;
        }

        resetProblem(problem, unknownPositions);
        return false;
    }

    public static int randomInt(int minInclusive, int maxExclusive){
        int randomNum = random.nextInt(maxExclusive-minInclusive);
        return randomNum + minInclusive;
    }

    public static void randomFlip(SudokuProblem problem, Position unknownPosition) {
        problem.board[unknownPosition.i][unknownPosition.j] = randomInt(1, problem.problemSize+1);
        Printer.printlnIfVerbose(String.format("flipping (%d,%d) to %d", unknownPosition.i, unknownPosition.j, problem.board[unknownPosition.i][unknownPosition.j]));
    }

    public static void randomAssignment(SudokuProblem problem, ArrayList<Position> unknownPositions) {
        for (Position unknownPosition : unknownPositions) {
            problem.board[unknownPosition.i][unknownPosition.j] = randomInt(1, problem.problemSize+1);
        }
    }

    public static void resetProblem(SudokuProblem problem, ArrayList<Position> unknownPositions) {
        for (Position unknownPosition : unknownPositions) {
            problem.board[unknownPosition.i][unknownPosition.j] = 0;
        }
    }



}

