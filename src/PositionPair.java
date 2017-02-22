/**
 * Created by lifu.wu on 22/2/17.
 */

public class PositionPair {
    Position p1, p2;

    public PositionPair(Position p1, Position p2){
        this.p1 = p1;
        this.p2 = p2;
    }

    public boolean equals(PositionPair pair){
        return (this.p1.equals(pair.p1) && this.p2.equals(pair.p2));
    }
}
