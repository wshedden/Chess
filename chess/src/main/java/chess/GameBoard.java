package chess;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.List;
import com.github.bhlangonijr.chesslib.*;
import com.github.bhlangonijr.chesslib.move.*;

public class GameBoard implements ActionListener {
    private Board board;
    private BoardGUI gui;
    private Square sq1;
    private Square sq2;
    private Move lastPlayerMove;
    private JFrame frame;
    private boolean isWaitingForPromotion = false;
    private Move partialPromotion;

    public GameBoard(boolean playerInput) {
        board = new Board();
        gui = new BoardGUI(playerInput);
        frameSetup();
        if (playerInput)
            addActionListeners();
    }

    private void frameSetup() {
        frame = new JFrame("Chess");
        frame.add(gui.getGui());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationByPlatform(true);
        frame.pack();
        frame.setMinimumSize(new Dimension(810, 715));
        frame.setMaximumSize(new Dimension(810, 715));
        frame.setVisible(true);
    }

    private void addActionListeners() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++)
                gui.addBoardActionListener(i, j, this);
        }
        for (int i = 0; i < 4; i++)
            gui.addPromotionActionListener(i, this);
    }

    public void updateBoard() {
        Move move = lastPlayerMove;
        lastPlayerMove = null;
        if (move != null) {
            if (board.legalMoves().contains(move))
                board.doMove(move);
            else {
                boolean isPromotion = testForPromotion(board, move);
                if (!isPromotion)
                    board = attemptMove(board, move);
                else
                    isWaitingForPromotion = true;
            }
        }
        testForChecks();
        gui.setFen(board.getFen());
        gui.refreshBoard();
        frame.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        frame.repaint();
        gui.getChessBoardGUI().repaint();
        String[] s = e.getActionCommand().split(" ");
        if (!isWaitingForPromotion && s[0] != "p") {
            updateLastClicked(s);
            updateBoard();
        } else if (isWaitingForPromotion && s[0].equals("p")) {
            int promotionID = Integer.parseInt(s[1]);
            Piece piece = getPromotionPiece(promotionID);
            attemptMove(board, new Move(partialPromotion.getFrom(), partialPromotion.getTo(), piece));
            isWaitingForPromotion = false;
            partialPromotion = null;
            gui.setPromotionColour(BoardGUI.darkTile);
            testForChecks();
            gui.setFen(board.getFen());
            gui.refreshBoard();
            frame.repaint();
        } else {
            System.err.println("actionPerformed error");
        }
        gui.setTurnColour(board.getSideToMove() == Side.WHITE);
    }

    private void updateLastClicked(String[] s) {
        int y = Integer.parseInt(s[0]);
        int x = Integer.parseInt(s[1]);
        Square lastClicked = Square.squareAt(56 + x - 8 * y);
        // Update squares
        if (sq1 == null) {
            sq1 = lastClicked;
        } else {
            sq2 = lastClicked;
            lastPlayerMove = new Move(sq1, sq2);
            sq1 = null;
            sq2 = null;
        }
    }

    private Piece getPromotionPiece(int promotionID) {
        Side side = board.getSideToMove();
        if (side == Side.WHITE)
            return new Piece[] { Piece.WHITE_QUEEN, Piece.WHITE_ROOK, Piece.WHITE_BISHOP,
                    Piece.WHITE_KNIGHT }[promotionID];
        return new Piece[] { Piece.BLACK_QUEEN, Piece.BLACK_ROOK, Piece.BLACK_BISHOP,
            Piece.BLACK_KNIGHT }[promotionID];
    }

    private boolean testForPromotion(Board board, Move move) {
        int count = 0;
        List<Move> moves = board.legalMoves();
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
            gui.setPromotionColour(Color.RED);
            return true;
        }
        return false;
    }

    private void testForChecks() {
        if (board.isKingAttacked()) {
            Square kingSquare = board.getKingSquare(board.getSideToMove());
            int x = kingSquare.ordinal() % 8;
            int y = 7 - kingSquare.ordinal() / 8;
            gui.setCheckSquare(x, y);
        } else
            gui.setCheckSquare(-1, -1);
    }

    private static Board attemptMove(Board board, Move move) {
        if (move != null && board.legalMoves().contains(move))
            board.doMove(move);
        return board;
    }

    public void setFen(String fen) {
        board.loadFromFen(fen);
    }
}
