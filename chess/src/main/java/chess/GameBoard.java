package chess;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import com.github.bhlangonijr.chesslib.*;
import com.github.bhlangonijr.chesslib.move.*;

public class GameBoard implements ActionListener {
    private static String promotionPiece;
    private static JFrame promotionWindow;
    private Board board;
    private BoardGUI gui;

    private Square sq1;
    private Square sq2;
    private Move lastPlayerMove;
    private JFrame frame;

    public GameBoard(boolean isPlayerInput) {
        board = new Board();
        gui = new BoardGUI();
        frameSetup();
        if(isPlayerInput)
            addActionListeners();

    }

    private void addActionListeners() {
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                gui.addActionListener(i, j, this);
            }
        }
    }

    private void frameSetup(){
        frame = new JFrame("Chess");
        frame.add(gui.getGui());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationByPlatform(true);
        frame.pack();
        frame.setMinimumSize(frame.getSize());
        frame.setVisible(true);
    }

    public void setFen(String fen) {
        board.loadFromFen(fen);
    }

    public void updateBoard() {
        Move move = lastPlayerMove;
        lastPlayerMove = null;
        if (move != null) {
            if (board.legalMoves().contains(move))
                board.doMove(move);
            else
                board = attemptMove(board, testForPromotion(board, move));
        }
        testForChecks();
        gui.setFen(board.getFen());
        gui.refreshBoard();
        frame.repaint();
    }

    private void testForChecks() {
        if (board.isKingAttacked()) {
            Square kingSquare = board.getKingSquare(board.getSideToMove());
            int x = kingSquare.ordinal() % 8;
            int y = 7 - kingSquare.ordinal() / 8;
            gui.setCheckSquare(x, y);
        } else {
            gui.setCheckSquare(-1, -1);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String[] s = e.getActionCommand().split(" ");
        int y = Integer.parseInt(s[0]);
        int x = Integer.parseInt(s[1]);
        Square lastClicked = Square.squareAt(56 + x - 8 * y);
        //Update squares
        if(sq1 == null) {
            sq1 = lastClicked;
        } else {
            sq2 = lastClicked;
            lastPlayerMove = new Move(sq1, sq2);
            sq1 = null;
            sq2 = null;
        }
        updateBoard();
    }

    //STATIC METHODS

    private static Piece getPromotionPiece(Side side) {
        showPromotionMenu(side == Side.BLACK);
        do {
            App.waitMillis(100);
        } while (promotionPiece == null);
        int promotionId = Integer.parseInt(promotionPiece);
        promotionPiece = null;
        promotionWindow.setVisible(false);
        if (side == Side.WHITE)
            return new Piece[] { Piece.WHITE_QUEEN, Piece.WHITE_ROOK, Piece.WHITE_BISHOP,
                    Piece.WHITE_KNIGHT }[promotionId];

        return new Piece[] { Piece.BLACK_QUEEN, Piece.BLACK_ROOK, Piece.BLACK_BISHOP, Piece.BLACK_KNIGHT }[promotionId];
    }

    private static void showPromotionMenu(boolean black) {
        promotionWindow = new JFrame("Promotion");
        JPanel grid = new JPanel(new GridLayout(2, 2));
        promotionWindow.add(grid);
        promotionWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        promotionWindow.setLocationByPlatform(true);
        promotionWindow.pack();
        promotionWindow.setMinimumSize(new Dimension(140, 140));
        String[] pieceList = new String[] { "q", "r", "b", "n" };
        for (int i = 0; i < 4; i++) {
            String path = "chess/src/main/resources/white_pieces_small/";
            if (black)
                path = "chess/src/main/resources/black_pieces_small/";
            JButton b = new JButton();
            b.setIcon(new ImageIcon(path + pieceList[i] + ".png"));
            b.setActionCommand(Integer.toString(i));
            b.setBackground(new Color(230, 227, 222));
            b.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    promotionPiece = e.getActionCommand();
                }
            });
            grid.add(b);
        }
        Point p = MouseInfo.getPointerInfo().getLocation();
        p.translate(-70, -80);
        promotionWindow.setLocation(p);
        promotionWindow.setVisible(true);
    }

    private static Move testForPromotion(Board board, Move move) {
        Move finalMove = null;
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
        if (found)
            finalMove = new Move(move.getFrom(), move.getTo(), getPromotionPiece(board.getSideToMove()));
        return finalMove;
    }

    private static Board attemptMove(Board board, Move move) {
        if (move != null) {
            if (board.legalMoves().contains(move)) {
                board.doMove(move);
            }
        }
        return board;
    }

}
