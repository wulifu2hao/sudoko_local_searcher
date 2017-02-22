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

