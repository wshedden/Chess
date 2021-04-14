package chess;

// import weka.*;
// import weka.classifiers.pmml.consumer.NeuralNetwork;
import com.github.bhlangonijr.chesslib.*;
import com.github.bhlangonijr.chesslib.move.*;
import java.util.concurrent.TimeUnit;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class App {
    private static String promotionPiece;
    private static JFrame promotionWindow;
    public static void main(String[] args) {
        Board board = new Board();
        board.loadFromFen("8/1P6/4k3/8/8/4K3/8/8 w - - 0 1");
        DisplayThread displayThread = new DisplayThread();
        Thread thread = new Thread(displayThread);
        thread.start();
        displayThread.isDetectClicks = true;

        while (true) {
            Move move = displayThread.popLastPlayerMove();
            if (move != null) {
                if (board.legalMoves().contains(move)) {
                    board.doMove(move);
                } else {
                    int count = 0;
                    List<Move> moves = board.legalMoves();
                    int size = moves.size();
                    boolean found = false;
                    while (count < size && !found) {
                        Move legalMove = moves.get(count);
                        if (legalMove.getFrom() == move.getFrom() || legalMove.getTo() == move.getTo()) {
                            if (move.getPromotion() != legalMove.getPromotion())
                                found = true;
                        }
                        count++;
                    }
                    if (found)
                        board.doMove(new Move(move.getFrom(), move.getTo(), getPromotionPiece(board.getSideToMove())));
                }
            }
            displayThread.setFen(board.getFen());
            try {
                TimeUnit.MILLISECONDS.sleep(50);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private static Piece getPromotionPiece(Side side) {
        showPromotionMenu(side == Side.BLACK);
        do {
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
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
        promotionWindow.setMinimumSize(new Dimension(155, 155));
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
                    System.out.println(e.getSource());
                    promotionPiece = e.getActionCommand();
                }
            });
            grid.add(b);
        }
        promotionWindow.setVisible(true);
    }

}
