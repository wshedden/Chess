package chess;

public class App {

    public static void main(String[] args) {
        Engine engine = new Engine();
        Gameboard board = new Gameboard(true, engine);
        // board.loadFromFen("8/1P6/3k2q1/8/8/4K3/8/8 w - - 0 1");
        // board.loadFromFen("1Q6/6K1/8/8/8/2k5/5p2/8 b - - 0 1");
        board.loadFromFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        board.updateBoard();
    }

}


/*
Gameboard:
    Button for getting AI move
    Tick boxes for AI playing white and/or black moves

AI training UI
*/
