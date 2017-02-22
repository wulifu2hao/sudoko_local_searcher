import java.util.Scanner;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by lifu.wu on 22/2/17.
 */
public class MainTest {
    public static Scanner scanner = new Scanner(System.in);
    public static final int SUDOKU_SIZE = 9;
    public static int maxIteration = 0;
    private static final AtomicLong seedUniquifier
            = new AtomicLong(8682522807148012L);

    public static void main(String[] args) throws Exception{
        long seed = seedUniquifier() ^ System.nanoTime();
        Printer.printlnIfVerbose(""+seed);
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

    private static long seedUniquifier() {
        // L'Ecuyer, "Tables of Linear Congruential Generators of
        // Different Sizes and Good Lattice Structure", 1999
        for (;;) {
            long current = seedUniquifier.get();
            long next = current * 181783497276652981L;
            if (seedUniquifier.compareAndSet(current, next))
                return next;
        }
    }
}
