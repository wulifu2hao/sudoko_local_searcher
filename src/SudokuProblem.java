import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Created by lifu.wu on 19/2/17.
 */
public class SudokuProblem {
    public static final int FAKE_NEGATIVE_INFINITITY = -1000;

    private int[][] board;
    int problemSize, problemSizeSqrt;
    int[] rowConsistencies, colConsistencies, areaConsistencies;
    boolean[] existCheckArray;
    ConsistencyCalculator calculator;
    private LinkedList<PositionPair> tabuList;
    private int tabuListCapacity;

    public SudokuProblem(int[][] board, int tabuListCapacity){
        this.board = board;
        problemSize = board.length;
        problemSizeSqrt = (int)(Math.sqrt(problemSize));

        existCheckArray = new boolean[problemSize];
        rowConsistencies = new int[problemSize];
        colConsistencies = new int[problemSize];
        areaConsistencies = new int[problemSize];
        for (int i=0; i<problemSize; i++){
            rowConsistencies[i] = 0;
            colConsistencies[i] = 0;
            areaConsistencies[i] = 0;
        }

        calculator = new ConsistencyCalculator(problemSize);
        tabuList = new LinkedList<PositionPair>();
        this.tabuListCapacity = tabuListCapacity;
    }

    public void printBoard(){
        for (int i=0; i<problemSize; i++) {
            for (int j=0; j<problemSize; j++) {
                Printer.print(board[i][j]+" ");
            }
            Printer.println("");
        }
    }

    public void printConsistencies(){
        Printer.println("consistency sum:"+consistencySum());
    }

    public boolean solved(){
        if (consistencySum() == calculator.getFullConsistencyScore()){
            return true;
        }

        return false;
    }

    public void shuffle(Position p1, Position p2){
        shuffleWithoutRecomputeConsistency(p1, p2);
        updateConsistencies(p1);
        updateConsistencies(p2);

        if (tabuList.size() >= this.tabuListCapacity) {
            tabuList.removeFirst();
        }
        tabuList.add(new PositionPair(p1, p2));
    }

    public int calculateShuffleScore(Position p1, Position p2){
        int p1Value = board[p1.i][p1.j];
        int p2Value = board[p2.i][p2.j];

        if (p1Value == p2Value) {
            return FAKE_NEGATIVE_INFINITITY;
        }

        int originalScore = calculatePositionScore(p1, true) + calculatePositionScore(p2, true);
        shuffleWithoutRecomputeConsistency(p1, p2);
        int newScore = calculatePositionScore(p1, false) + calculatePositionScore(p2, false);
        shuffleWithoutRecomputeConsistency(p1, p2);

        return newScore - originalScore;
    }

    public boolean isUnknownPosition(int i, int j) {
        return board[i][j] == 0;
    }

    public int getValueAtPosition(int i, int j) {
        return board[i][j];
    }

    public void setValueAtPosition(int i, int j, int value) {
        board[i][j] = value;
    }

    public void computeAllConsistencies(){
        for(int i=0; i<problemSize; i++){
            rowConsistencies[i] = calculateRowConsistency(i);
            colConsistencies[i] = calculateColConsistency(i);
            areaConsistencies[i] = calculateAreaConsistency(i);
        }
    }

    public void printAllConsistencies(){
        Printer.printlnIfVerbose("rowConsistencies");
        for(int i=0; i<problemSize; i++){
            Printer.printIfVerbose(String.format("(%d, %d)", i, rowConsistencies[i]));
        }
        Printer.printlnIfVerbose("");
        Printer.printlnIfVerbose("colConsistencies");
        for(int i=0; i<problemSize; i++){
            Printer.printIfVerbose(String.format("(%d, %d)", i, colConsistencies[i]));
        }
        Printer.printlnIfVerbose("");
        Printer.printlnIfVerbose("areaConsistencies");
        for(int i=0; i<problemSize; i++){
            Printer.printIfVerbose(String.format("(%d, %d)", i, areaConsistencies[i]));
        }
        Printer.printlnIfVerbose("");
    }

    public void resetAllConsistencies(){
        for(int i=0; i<problemSize; i++){
            rowConsistencies[i] = 0;
            colConsistencies[i] = 0;
            areaConsistencies[i] = 0;
        }
    }

    public int consistencySum(){
        int consistenciesSum = 0;
        for (int i=0; i<problemSize; i++) {
            consistenciesSum += rowConsistencies[i];
            consistenciesSum += colConsistencies[i];
            consistenciesSum += areaConsistencies[i];
        }
        return consistenciesSum;
    }

    public boolean isInTabuList(PositionPair pair){
        ListIterator<PositionPair> iterator = tabuList.listIterator();
        while (iterator.hasNext()){
            PositionPair tabuPair = iterator.next();
            if (tabuPair.equals(pair)){
                return true;
            }
        }
        return false;
    }

    private void updateConsistencies(Position p){
        int rowIdx = p.i;
        int colIdx = p.j;
        int areaIdx = getPositionAreaIdx(rowIdx, colIdx);

        rowConsistencies[rowIdx] = calculateRowConsistency(rowIdx);
        colConsistencies[colIdx] = calculateColConsistency(colIdx);
        areaConsistencies[areaIdx] = calculateAreaConsistency(areaIdx);
    }

    private int calculatePositionScore(Position p, boolean useCache){
        int rowIdx = p.i;
        int colIdx = p.j;
        int areaIdx = getPositionAreaIdx(rowIdx, colIdx);

        if (useCache){
//            Printer.printlnIfVerbose(String.format("(%d,%d,%d) rowConsistencies:%d, colConsistencies:%d, areaConsistencies:%d",p.i, p.j, board[p.i][p.j], rowConsistencies[rowIdx], colConsistencies[colIdx], areaConsistencies[areaIdx]));
            return rowConsistencies[rowIdx] + colConsistencies[colIdx] + areaConsistencies[areaIdx];
        }

        return calculateRowConsistency(rowIdx) + calculateColConsistency(colIdx) + calculateAreaConsistency(areaIdx);
    }

    private void shuffleWithoutRecomputeConsistency(Position p1, Position p2){
        int temp = board[p1.i][p1.j];
        board[p1.i][p1.j] = board[p2.i][p2.j];
        board[p2.i][p2.j] = temp;
    }

    private int getPositionAreaIdx(int rowIdx, int colIdx){
        int baseAreaIdx = rowIdx/problemSizeSqrt * problemSizeSqrt;
        int areaIdxOffset = colIdx/problemSizeSqrt;
        return baseAreaIdx + areaIdxOffset;
    }

    private int calculateRowConsistency(int rowIdx) {
        int[] assignment = new int[problemSize];
        for (int i=0; i<problemSize; i++){
            assignment[i] = board[rowIdx][i];
        }

        return calculator.evaluate(assignment);
    }

    private int calculateColConsistency(int colIdx) {
        int[] assignment = new int[problemSize];
        for (int i=0; i<problemSize; i++){
            assignment[i] = board[i][colIdx];
        }

        return calculator.evaluate(assignment);
    }

    private int calculateAreaConsistency(int areaIdx) {
        int[] assignment = new int[problemSize];

        int baseRowIndex = areaIdx/problemSizeSqrt * problemSizeSqrt;
        int baseColIndex = areaIdx%problemSizeSqrt * problemSizeSqrt;
        for(int j=0; j<problemSize;j++){
            int rowIndexOffset = j/problemSizeSqrt;
            int rowIndex = baseRowIndex + rowIndexOffset;

            int colIndexOffset = j%problemSizeSqrt;
            int colIndex = baseColIndex + colIndexOffset;

            assignment[j] = board[rowIndex][colIndex];
        }

        return calculator.evaluate(assignment);
    }

}
