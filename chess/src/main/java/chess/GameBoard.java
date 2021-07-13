package chess;

import java.awt.event.*;
import java.awt.*;
import java.util.List;
import com.github.bhlangonijr.chesslib.*;
import com.github.bhlangonijr.chesslib.move.*;

public class Gameboard extends Board implements ActionListener {
    private BoardFrame frame;
    private Square sq1;
    private Square sq2;
    private Move lastPlayerMove;
    private boolean isWaitingForPromotion = false;
    private Move partialPromotion;
    private boolean isFlipped = false;
    private boolean isPlayerInput;

    public Gameboard(boolean isPlayerInput) {
        this.isPlayerInput = isPlayerInput;
        frame = new BoardFrame(isPlayerInput);
        if (isPlayerInput) {
            addActionListeners();
        }
    }

    private void addActionListeners() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                frame.addBoardActionListener(i, j, this);
            }
        }
        for (int i = 0; i < 4; i++) {
            frame.addPromotionActionListener(i, this);
        }
        if (isPlayerInput) {
            frame.addFlipButtonActionListener(this);
        }
    }

    public void updateBoard() {
        Move move = lastPlayerMove;
        lastPlayerMove = null;
        if (move != null) {
            if (legalMoves().contains(move))
                doMove(move);
            else {
                boolean isPromotion = testForPromotion(move);
                if (!isPromotion)
                    attemptMove(move);
                else
                    isWaitingForPromotion = true;
            }
        }
        testForChecks();
        frame.setFen(getFen());
        frame.refreshBoard(isFlipped);
        if (isPlayerInput) {
            frame.updatePromotionGrid(getSideToMove() == Side.WHITE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String[] s = e.getActionCommand().split(" ");
        if (isWaitingForPromotion) {
            if (s[0].equals("p")) {
                selectPromotionPiece(s[1]);
            }
        } else {
            if (s[0].equals("f")) {
                // Flip board
                isFlipped = !isFlipped;
                updateBoard();
            } else if (s[0].equals("m")) {
                // Normal piece selection
                updateLastClicked(s);
                updateBoard();
            }
        }
        frame.updatePromotionGrid(getSideToMove() == Side.WHITE);
    }

    private void selectPromotionPiece(String id) {
        // Processes user selection from promotion grid
        int promotionID = Integer.parseInt(id);
        Piece piece = getPromotionPiece(promotionID);
        attemptMove(new Move(partialPromotion.getFrom(), partialPromotion.getTo(), piece));
        isWaitingForPromotion = false;
        partialPromotion = null;
        frame.setPromotionColour(BoardFrame.darkTile);
        testForChecks();
        frame.setFen(getFen());
        frame.refreshBoard(isFlipped);
    }

    private void updateLastClicked(String[] coordinates) {
        int y = Integer.parseInt(coordinates[1]);
        int x = Integer.parseInt(coordinates[2]);
        Square lastClicked;
        if (!isFlipped) {
            lastClicked = Square.squareAt(56 + x - 8 * y);
        } else {
            lastClicked = Square.squareAt(8 * y + 7 - x);
        }
        // Update squares
        if (sq1 == null) {
            if(lastClicked != Square.NONE){
                sq1 = lastClicked;
            }
        } else {
            sq2 = lastClicked;
            lastPlayerMove = new Move(sq1, sq2);
            sq1 = null;
            sq2 = null;
        }
    }

    private Piece getPromotionPiece(int promotionID) {
        Side side = getSideToMove();
        if (side == Side.WHITE)
            return new Piece[] { Piece.WHITE_QUEEN, Piece.WHITE_ROOK, Piece.WHITE_BISHOP,
                    Piece.WHITE_KNIGHT }[promotionID];
        return new Piece[] { Piece.BLACK_QUEEN, Piece.BLACK_ROOK, Piece.BLACK_BISHOP, Piece.BLACK_KNIGHT }[promotionID];
    }

    private boolean testForPromotion(Move move) {
        int count = 0;
        List<Move> moves = legalMoves();
        int size = moves.size();
        boolean found = false;
        while (count < size && !found) {
            Move legalMove = moves.get(count);
            if (legalMove.getFrom() == move.getFrom() && legalMove.getTo() == move.getTo()) {
                if (move.getPromotion() != legalMove.getPromotion())
                    found = true;
            }
            count++;
        }
        if (found) {
            partialPromotion = move;
            frame.setPromotionColour(Color.RED);
            return true;
        }
        return false;
    }

    private void testForChecks() {
        if (isKingAttacked()) {
            Square kingSquare = getKingSquare(getSideToMove());
            int x = kingSquare.ordinal() % 8;
            int y = 7 - kingSquare.ordinal() / 8;
            if (isFlipped) {
                x = 7 - x;
                y = 7 - y;
            }
            frame.setCheckSquare(x, y);
        } else {
            frame.setCheckSquare(-1, -1);
        }
    }

    private void attemptMove(Move move) {
        if (move != null && legalMoves().contains(move)) {
            doMove(move);
        }
    }
}
