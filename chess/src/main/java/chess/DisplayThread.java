package chess;

import javax.swing.*;

import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;

import java.util.concurrent.TimeUnit;

public class DisplayThread implements Runnable {
    private BoardGUI gui = new BoardGUI();
    private boolean isRunning = true;
    public boolean isDetectClicks = false;

    private Square sq1;
    private Square sq2;
    private Move lastPlayerMove;

    public void run() {
        JFrame frame = new JFrame("Chess");
        frame.add(gui.getGui());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationByPlatform(true);
        frame.pack();
        frame.setMinimumSize(frame.getSize());
        frame.setVisible(true);

        while (isRunning) {
            // gui.setFen(App.fen);
            gui.refreshBoard();
            frame.repaint();

            if(isDetectClicks) detectClicks();


            try {
                TimeUnit.MILLISECONDS.sleep(50);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void setFen(String fen) {
        gui.setFen(fen);
    }

    private void detectClicks() {
        Square lastClicked = gui.getLastClicked();
        if (sq1 == null) {
            sq1 = lastClicked;
        } else if(sq1 != lastClicked) {
            sq2 = lastClicked;
            lastPlayerMove = new Move(sq1, sq2, Piece.WHITE_QUEEN);
            sq1 = null;
            sq2 = null;
        }
    }

    public Move popLastPlayerMove(){
        Move move = lastPlayerMove;
        lastPlayerMove = null;
        return move;
    }
}
