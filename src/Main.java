import java.util.Scanner;

public class Main {

    public static Scanner scanner = new Scanner(System.in);
    public static final int SUDOKU_SIZE = 9;
    public static int maxIteration = 0;

    public static void main(String[] args) throws Exception{
        if (args.length > 0) {
            maxIteration = Integer.parseInt(args[0]);
        }

        SudokuProblem problem = readInput();
        problem.solve(maxIteration);
        problem.printBoard();
    }

    public static SudokuProblem readInput() throws Exception{
        int[][] board = new int[SUDOKU_SIZE][SUDOKU_SIZE];

        int lineIndex = 0;
        while (scanner.hasNextLine()){
            String line = scanner.nextLine().trim();
            if (line.length() == 0) {
                continue;
            }
            char firstSymbol = line.charAt(0);
            if (firstSymbol == '#') {
                continue;
            }
            String[] contents = line.split(" ");
            if (contents.length < SUDOKU_SIZE) {
                    throw new Exception("input line does not match with problem size " + line);
            }
            for (int i=0; i<SUDOKU_SIZE; i++){
                String content = contents[i].trim();
                if (content.equals("_")) {
                    board[lineIndex][i] = 0;
                } else {
                    int value = Integer.parseInt(content);
                    board[lineIndex][i] = value;
                }
            }

            lineIndex++;
            if (lineIndex == SUDOKU_SIZE) {
                SudokuProblem problem = new SudokuProblem(board);
                problem.printBoard();
                return problem;
            }
        }

        throw new Exception("invalid input");
    }
}
