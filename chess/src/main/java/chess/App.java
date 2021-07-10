package chess;

// import weka.*;
// import weka.classifiers.pmml.consumer.NeuralNetwork;
import java.util.concurrent.TimeUnit;

public class App {

    public static void main(String[] args) {
        GameBoard gb = new GameBoard(true);
        gb.setFen("8/1P6/3k2q1/8/8/4K3/8/8 w - - 0 1");
        gb.setFen("1Q6/6K1/8/8/8/2k5/5p2/8 b - - 0 1");
        
        gb.updateBoard();
    }

    public static void waitMillis(int millis) {
        try {
            TimeUnit.MILLISECONDS.sleep(millis);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }
}
