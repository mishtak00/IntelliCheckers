import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Checkers {

    public enum Square {
        EMPTY,
        SELF,
        OPPON,
        SELFKING,
        OPPONKING
    }

    private Square[][] board;
    private final int boardSize;

    public Checkers(int boardSize) {

        this.boardSize = boardSize;
        board = new Square[boardSize][boardSize];
        int i = 0, startAt0 = 0;

        // builds opponent's side
        for (; i<boardSize/2-1; i++) {
            for (int j=0; j<boardSize; j++)
                board[i][j] = j%2==startAt0 ? Square.EMPTY : Square.OPPON;
            startAt0 = startAt0==0 ? 1 : 0;
        }

        // builds middle of board
        for (; i<boardSize/2+1; i++)
            for (int j=0; j<boardSize; j++)
                board[i][j] = Square.EMPTY;

        // builds self's side
        for (; i<boardSize; i++) {
            for (int j=0; j<boardSize; j++)
                board[i][j] = j%2==startAt0 ? Square.EMPTY : Square.SELF;
            startAt0 = startAt0==0 ? 1 : 0;
        }

        System.out.println(this);
    }

    private static String squareToString(Square sq) {
        switch (sq) {
            case EMPTY:
                return "_";
            case SELF:
                return "s";
            case OPPON:
                return "o";
            case SELFKING:
                return "S";
            case OPPONKING:
                return "O";
            default:
                return "";
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("  ");
        for (int c=0; c<boardSize; c++)
            sb.append(" ").append((char)('A'+c));
        sb.append('\n');

        for (int i=0; i<boardSize; i++) {
            sb.append(boardSize-i).append(" |");
            for (Square s : board[i])
                sb.append(squareToString(s) + "|");
            sb.append(' ').append(boardSize-i).append('\n');
        }

        sb.append("  ");
        for (int c=0; c<boardSize; c++)
            sb.append(" ").append((char)('A'+c));

        return sb.toString();
    }

    private boolean isOther(int i, int j, int k, int l) {
        Square s1 = board[i][j], s2 = board[k][l];
        return (
                // can move to other square if am self
                // and other is opponent or empty
                ((s1==Square.SELF || s1==Square.SELFKING)
                    && (s2!=Square.SELF && s2!=Square.SELFKING))
                // or can move if am oppon and other is
                // self or empty square
                || ((s1==Square.OPPON || s1==Square.OPPONKING)
                    && (s2!=Square.OPPON && s2!=Square.OPPONKING))
        );
    }

    private List<int[]> getDiagSquares(int i, int j) {
        List<int[]> dList = new LinkedList<>();
        if (i+1<boardSize)
            if (j+1<boardSize)
                dList.add(new int[]{i+1,j+1});
            if (j-1>=0)
                dList.add(new int[]{i+1,j-1});
        if (i-1>=0)
            if (j+1<boardSize)
                dList.add(new int[]{i-1,j+1});
            if (j-1>=0)
                dList.add(new int[]{i-1,j-1});
        return dList;
    }

    private List<int[]> getMovesRecur(int i, int j) {
        List<int[]> move = new LinkedList<>();
        for (int[] diag : getDiagSquares(i,j)) {
            if (isOther(i, j, diag[0], diag[1]))
                ;
        }

        return move;
    }

    private Set<int[][]> getMovesSelf() {
        Set<int[][]> moves = new HashSet<>();
        for (int i=boardSize-1; i>=0; i--)
            for (int j=0; j<boardSize; j++)
                ;

        return moves;
    }


}
