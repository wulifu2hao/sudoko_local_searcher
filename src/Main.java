import java.util.Scanner;

public class Main {

    public static Scanner scanner = new Scanner(System.in);
    public static final int SUDOKU_SIZE = 9;

    public static long seed = 8006738581902217899L;
    public static int maxRestartCount = 100000;
    public static int maxStepCount = 200;
    public static int tabuListCapacity = 2;
    public static int solverType = 2;

    public static void main(String[] args) throws Exception{
        parseArgs(args);

        SudokuProblem problem = readInput();
        if (solverType == 1) {
            URSolver.solve(problem, seed);
        } else {
            OptimisedSolver.solve(problem, seed, maxRestartCount, maxStepCount);
        }

        if (problem.solved()) {
            Printer.println("# satisfiable solution");
        } else {
            Printer.println("# fail");
        }

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
                SudokuProblem problem = new SudokuProblem(board, tabuListCapacity);
//                problem.printBoard();
                return problem;
            }
        }

        throw new Exception("invalid input");
    }

    public static void parseArgs(String[] args) throws Exception{
        for (int i = 0; i < args.length; i++) {
            switch (args[i].charAt(0)) {
                case '-':
                    if (args[i].length() < 2)
                        throw new IllegalArgumentException("Not a valid argument: "+args[i]);
                    else {
                        if (args.length-1 == i)
                            throw new IllegalArgumentException("Expected arg after: "+args[i]);

                        switch (args[i]){
                            case "-seed":
                                seed = Long.parseLong(args[i+1]);
                                break;
                            case "-max_restart_count":
                                maxRestartCount = Integer.parseInt(args[i+1]);
                                break;
                            case "-max_step_count":
                                maxStepCount = Integer.parseInt(args[i+1]);
                                break;
                            case "-tabu_list_capacity":
                                tabuListCapacity = Integer.parseInt(args[i+1]);
                                break;
                            case "-solver_type":
                                if (args[i+1].equals("uninformed")){
                                    solverType = 1;
                                } else {
                                    throw new IllegalArgumentException("Not a valid solver type: "+args[i+1]);
                                }
                                break;
                            default:
                                break;
                        }

                        i++;
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
