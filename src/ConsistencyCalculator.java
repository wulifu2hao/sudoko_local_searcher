/**
 * Created by lifu.wu on 21/2/17.
 * A0105661M e0045348@u.nus.edu
 */
public class ConsistencyCalculator {
    int[] utilArray;
    int fullConsistencyScoreByUniqueNumber;

    public ConsistencyCalculator(int problemSize){
        this.utilArray = new int[problemSize];
        this.resetUtilArray();

        fullConsistencyScoreByUniqueNumber = 3 * getProblemSize() * getProblemSize();
    }

    private int getProblemSize(){
        return utilArray.length;
    }

    private void resetUtilArray(){
        for (int i=0; i<getProblemSize(); i++) {
            utilArray[i] = 0;
        }
    }

    public int evaluate(int[] assignment){
        return evaluateUniqueNumber(assignment);
    }

    public int getFullConsistencyScore(){
        return getFullConsistencyScoreByUniqueNumber();
    }

    private int evaluateUniqueNumber(int[] assignment){
        resetUtilArray();
        for (int i=0; i<getProblemSize(); i++) {
            utilArray[assignment[i]-1]++;
        }

        int score = 0;
        for (int i=0; i<getProblemSize(); i++) {
            if (utilArray[i] == 1) {
                score ++;
            }
        }

        return score;
    }

    private int getFullConsistencyScoreByUniqueNumber(){
        return fullConsistencyScoreByUniqueNumber;
    }

}
