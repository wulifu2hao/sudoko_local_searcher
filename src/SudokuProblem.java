import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by lifu.wu on 19/2/17.
 */
public class SudokuProblem {
    int[][] board;
    int problemSize, problemSizeSqrt;
    ArrayList<Boolean> elementExistsArray;

    public SudokuProblem(int[][] board){
        this.board = board;
        problemSize = board.length;
        problemSizeSqrt = (int)(Math.sqrt(problemSize));

        elementExistsArray = new ArrayList<Boolean>();
        for (int i=0; i<problemSize; i++){
            elementExistsArray.add(false);
        }
    }

    public void printBoard(){
        for (int i=0; i<problemSize; i++) {
            for (int j=0; j<problemSize; j++) {
                Printer.print(board[i][j]+" ");
            }
            Printer.println("");
        }
    }

    public void solve(int maxIteration){
        boolean solved = URSolver.solve(this, maxIteration);
        if (solved) {
            Printer.printlnIfVerbose("successful");
        } else {
            Printer.printlnIfVerbose("fail");
        }
    }

    public boolean solved(){
        if (rowAllDiff() && colAllDiff() && areaAllDiff()){
            return true;
        }

        return false;
    }

    public boolean rowAllDiff(){
        for (int i=0; i<problemSize; i++) {
            resetElementExistsArray();
            int[] row = board[i];
            for (int j=0; j<problemSize; j++) {
                if (elementExistsArray.get(row[j]-1)) {
                    return false;
                }
                elementExistsArray.set(row[j]-1, true);
            }
        }
        return true;
    }

    public boolean colAllDiff(){
        for (int j=0; j<problemSize; j++) {
            resetElementExistsArray();
            for (int i=0; i<problemSize; i++) {
                if (elementExistsArray.get(board[i][j]-1)) {
                    return false;
                }
                elementExistsArray.set(board[i][j]-1, true);
            }
        }
        return true;
    }

    public boolean areaAllDiff(){
        for (int i=0; i<problemSize; i++) {
            resetElementExistsArray();

            int baseRowIndex = i/problemSizeSqrt * problemSizeSqrt;
            int baseColIndex = i%problemSizeSqrt * problemSizeSqrt;
            for(int j=0; j<problemSize;j++){
                int rowIndexOffset = j/problemSizeSqrt;
                int rowIndex = baseRowIndex + rowIndexOffset;

                int colIndexOffset = j%problemSizeSqrt;
                int colIndex = baseColIndex + colIndexOffset;

                if (elementExistsArray.get(board[rowIndex][colIndex]-1)) {
                    return false;
                }
                elementExistsArray.set(board[rowIndex][colIndex]-1, true);
            }

        }


        return true;
    }

    public void resetElementExistsArray(){
        for (int i=0; i<problemSize; i++) {
            elementExistsArray.set(i, false);
        }
    }

}
