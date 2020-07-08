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

//        // builds opponent's side
//        for (; i<boardSize/2-1; i++) {
//            for (int j=0; j<boardSize; j++)
//                board[i][j] = j%2==startAt0 ? Square.EMPTY : Square.OPPON;
//            startAt0 = startAt0==0 ? 1 : 0;
//        }
//
//        // builds middle of board
//        for (; i<boardSize/2+1; i++)
//            for (int j=0; j<boardSize; j++)
//                board[i][j] = Square.EMPTY;
//
//        // builds self's side
//        for (; i<boardSize; i++) {
//            for (int j=0; j<boardSize; j++)
//                board[i][j] = j%2==startAt0 ? Square.EMPTY : Square.SELF;
//            startAt0 = startAt0==0 ? 1 : 0;
//        }

        for (; i<boardSize; i++)
            for (int j=0; j<boardSize; j++)
                board[i][j]=Square.EMPTY;
        board[5][0]=Square.SELF; board[4][1]=Square.OPPON;

        System.out.println(this);
//        printBoardSquare(new int[]{7,7});
        List<int[]> dList = getDiagSquares(5,0);
        for (int[] sq : dList)
            printBoardSquare(sq);

        System.out.println(isOther(5,0,4,1));
        System.out.println(canCapture(5,0,4,1));
    }

    private void printBoardSquare(int[] s) {
        System.out.printf("(%c,%d)\n", (char)('A'+s[1]), boardSize-s[0]);
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

    private List<int[]> getDiagSquares(int i, int j) {
        List<int[]> dList = new LinkedList<>();
        if (i+1<boardSize) {
            if (j+1<boardSize)
                dList.add(new int[]{i + 1, j + 1});
            if (j-1>=0)
                dList.add(new int[]{i + 1, j - 1});
        }
        if (i-1>=0) {
            if (j+1<boardSize)
                dList.add(new int[]{i - 1, j + 1});
            if (j-1>=0)
                dList.add(new int[]{i - 1, j - 1});
        }
        return dList;
    }

    private boolean isOther(int i, int j, int k, int l) {
        Square s1 = board[i][j], s2 = board[k][l];
        return (
                // can move to other square
                // if am self and other is oppon
                ((s1==Square.SELF || s1==Square.SELFKING)
                    && (s2==Square.OPPON || s2==Square.OPPONKING))
                // or if am oppon and other is self
                || ((s1==Square.OPPON || s1==Square.OPPONKING)
                    && (s2==Square.SELF || s2==Square.SELFKING))
        );
    }

    private boolean canCapture(int i, int j, int k, int l) {
        int dx = 2*(k-i), dy = 2*(l-j);
        int destx = i+dx, desty = j+dy;
        return (
                // can capture only if other's square
                isOther(i, j, k ,l)
                // and if next square diagonally is on board
                && destx>=0 && destx<boardSize
                && desty>=0 && desty<boardSize
                // and if it is empty
                && board[destx][desty]==Square.EMPTY
                );
    }

    private List<int[]> getMovesRecur(int i, int j) {
        List<int[]> move = new LinkedList<>();

        for (int[] diag : getDiagSquares(i,j)) {
            // move over and capture opponent square
            if (canCapture(i, j, diag[0], diag[1])) {
                int destx = i+2*(diag[0]-i), desty = j+2*(diag[1]-j);
                move.add(new int[]{destx, desty});
                // temporarily set board as if move was taken
                board[destx][desty] = board[i][j];
                // recurse from the new position
                // to find consecutive moves
                move.addAll(getMovesRecur(destx, desty));
                // reset board to original state
                board[destx][desty] = Square.EMPTY;

                return move;
            }

            // move to empty square
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
