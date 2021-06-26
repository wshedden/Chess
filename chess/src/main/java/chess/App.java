package chess;

// import weka.*;
// import weka.classifiers.pmml.consumer.NeuralNetwork;
import java.util.concurrent.TimeUnit;

public class App {

    public static void main(String[] args) {
        GameBoard gb = new GameBoard(true);
        gb.setFen("1Q6/6K1/8/8/8/8/1k3p2/8 b - - 0 1");
        gb.updateBoard();
        while (true) {
            // gb.updateBoard();
            // waitMillis(40);
        }
    }

    public static void waitMillis(int millis) {
        try {
            TimeUnit.MILLISECONDS.sleep(millis);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }
}