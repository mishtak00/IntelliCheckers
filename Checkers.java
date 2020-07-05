public class Checkers {

    public enum Square {
        EMPTY,
        SELF,
        OPPON,
        SELFKING,
        OPPONKING
    }

    private Square[][] board;
    private int boardSize;

    public Checkers() {

        boardSize = 8;
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

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<boardSize; i++) sb.append(" _");
        sb.append("\n");

        for (int i=0; i<boardSize; i++) {
            sb.append("|");
            for (Square s : board[i])
                sb.append(squareToString(s) + "|");
            sb.append("\n");
        }
        return sb.toString();
    }

}
