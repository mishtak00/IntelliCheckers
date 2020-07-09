import java.util.*;

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
        board[2][1]=Square.OPPONKING; board[0][1]=Square.OPPON;

        System.out.println(this);

        List<int[]> dList = getDiagSquares(5,0);
        for (int[] sq : dList)
            printBoardSquare(sq);

        System.out.println(isOther(5,0,4,1));
        System.out.println(canCapture(5,0,4,1));
        List<int[]> mList = getMovesRecur(5,0);
        for (int[] mv : mList)
            printBoardSquare(mv);

        board[5][2]=Square.SELFKING;
        System.out.println(this);
        List<int[]> dList2 = getDiagSquares(5,2);
        for (int[] sq : dList2)
            printBoardSquare(sq);

        List<int[]> mList2 = getMovesRecur(5,2);
        for (int[] mv : mList2)
            printBoardSquare(mv);

        board[4][3]=Square.OPPONKING;
        System.out.println(this);
        Map<int[],List<int[]>> mvSet = getMovesSelf();
        for (Map.Entry<int[], List<int[]>> mvs : mvSet.entrySet()) {
            System.out.printf("Moves for %s : %s\n",
                    boardSquareToString(mvs.getKey()),
                    moveListToString(mvs.getValue()));
        }

    }

    private String boardSquareToString(int[] s) {
        return String.format("(%c,%d)", (char)('A'+s[1]), boardSize-s[0]);
    }

    private void printBoardSquare(int[] s) {
        System.out.println(boardSquareToString(s));
    }

    private String moveListToString(List<int[]> ml) {
        StringBuilder sb = new StringBuilder();
        sb.append("{ ");
        for (int[] mv : ml)
            sb.append(String.format("%s ", boardSquareToString(mv)));
        sb.append("}");
        return sb.toString();
    }

    private void printMoveList(List<int[]> ml) {
        System.out.println(moveListToString(ml));
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

    /*
     * Gets diagonal squares of the square at (i,j) depending
     * on its type. This expects a non-EMPTY square.
     * This function is the only one that knows to constrain
     * the normal pieces' moves as opposed to the kings'.
     */
    private List<int[]> getDiagSquares(int i, int j) {
        List<int[]> dList = new LinkedList<>();
        Square curr = board[i][j];

        // only forward diagonals for normals
        if (curr==Square.SELF || curr==Square.OPPON) {
            // find diags: towards lower indices for self
            // towards higher indices for opponent
            int selfOrOpp = board[i][j]==Square.SELF ? -1 : 1;
            if (i+selfOrOpp<boardSize) {
                if (j+1<boardSize)
                    dList.add(new int[]{i+selfOrOpp, j+1});
                if (j-1>=0)
                    dList.add(new int[]{i+selfOrOpp, j-1});
            }

        // both forward and backward diagonals for kings
        } else if (curr==Square.SELFKING || curr==Square.OPPONKING) {
            if (i-1>=0) {
                if (j+1<boardSize)
                    dList.add(new int[]{i-1, j+1});
                if (j-1>=0)
                    dList.add(new int[]{i-1, j-1});
            }
            if (i+1<boardSize) {
                if (j-1>=0)
                    dList.add(new int[]{i+1, j-1});
                if (j+1<boardSize)
                    dList.add(new int[]{i+1, j+1});
            }
        }
        return dList;
    }

    private boolean isOther(int i, int j, int k, int l) {
        Square s1 = board[i][j], s2 = board[k][l];
        return (
                // can move to other square
                // if am self and other is oppon
                ((isSelf(s1)) && (isOppon(s2)))
                // or if am oppon and other is self
                || ((isOppon(s1)) && (isSelf(s2)))
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
                && isEmpty(board[destx][desty])
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

    private static boolean isSelf(Square s) {
        return s==Square.SELF || s==Square.SELFKING;
    }

    private static boolean isOppon(Square s) {
        return s==Square.OPPON || s==Square.OPPONKING;
    }

    private static boolean isEmpty(Square s) {
        return s==Square.EMPTY;
    }

    private Map<int[], List<int[]>> getMovesSelf() {
        Map<int[], List<int[]>> moves = new HashMap<>();
        for (int i=boardSize-1; i>=0; i--)
            for (int j=0; j<boardSize; j++)
                if (isSelf(board[i][j])) {
                    moves.put(new int[]{i,j},getMovesRecur(i,j));
                }
        return moves;
    }


}
