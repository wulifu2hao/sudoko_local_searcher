/**
 * Created by lifu.wu on 20/2/17.
 */
public class Position {
    int i, j;

    public Position(int i, int j){
        this.i = i;
        this.j = j;
    }

    public boolean equals(Position p){
        return (this.i == p.i && this.j == p.j);
    }
}

class PositionPair {
    Position p1, p2;

    public PositionPair(Position p1, Position p2){
        this.p1 = p1;
        this.p2 = p2;
    }

    public boolean equals(PositionPair pair){
        return (this.p1.equals(pair.p1) && this.p2.equals(pair.p2));
    }
}
