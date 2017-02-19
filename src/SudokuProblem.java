/**
 * Created by lifu.wu on 19/2/17.
 */
public class SudokuProblem {
    int[][] board;
    int problemSize;

    public SudokuProblem(int[][] board){
        this.board = board;
        problemSize = board.length;
    }

    public void printBoard(){
        for (int i=0; i<problemSize; i++) {
            for (int j=0; j<problemSize; j++) {
                Printer.print(board[i][j]+" ");
            }
            Printer.println("");
        }
    }

    public void solve(){

    }

}
