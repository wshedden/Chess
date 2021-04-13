package chess;

// import weka.*;
// import weka.classifiers.pmml.consumer.NeuralNetwork;
import com.github.bhlangonijr.chesslib.*;
import com.github.bhlangonijr.chesslib.move.*;
import java.util.concurrent.TimeUnit;
import java.util.List;
import java.util.Random;

public class App {
    private static Random random = new Random();
    public static void main(String[] args) {
        Board board = new Board();
        board.loadFromFen("8/1P6/4k3/8/8/4K3/8/8 w - - 0 1");
        DisplayThread displayThread = new DisplayThread();
        Thread thread = new Thread(displayThread);
        thread.start();
        displayThread.isDetectClicks = true;
        
        while (true) {
            try {
                displayThread.setFen(board.getFen());
                // board.doMove(randomMove(board));
                Move move = displayThread.popLastPlayerMove();
                if(move != null && board.legalMoves().contains(move)){
                    board.doMove(move);
                }
                TimeUnit.MILLISECONDS.sleep(50);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
    } 

    private static Move randomMove(Board board) {
        List<Move> moves = board.legalMoves();
        int moveNum = random.nextInt(moves.size());
        return moves.get(moveNum);
    }


}
