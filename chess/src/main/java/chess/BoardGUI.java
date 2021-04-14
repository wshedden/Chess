package chess;

import javax.swing.*;
import javax.swing.border.*;
import com.github.bhlangonijr.chesslib.Square;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class BoardGUI implements ActionListener {
    private final JPanel gui = new JPanel(new BorderLayout(3, 3));
    private JButton[][] chessBoardSquares = new JButton[8][8];
    private JPanel chessBoard;
    private String[][] pieces;
    private Square lastClicked;

    BoardGUI() {
        pieces = new String[8][8];
        for (int i = 0; i < 8; i++) {
            pieces[i] = new String[8];
            for (int j = 0; j < 8; j++) {
                pieces[i][j] = "";
            }
        }
        initialiseGui();
    }

    public final void initialiseGui() {
        final Color darkTile = new Color(140, 162, 173);
        final Color lightTile = new Color(230, 227, 222);

        // set up the main GUI
        gui.setBorder(new EmptyBorder(5, 5, 5, 5));
        JToolBar tools = new JToolBar();
        tools.setFloatable(false);
        gui.add(tools, BorderLayout.PAGE_START);
        tools.addSeparator();

        chessBoard = new JPanel(new GridLayout(0, 8));
        chessBoard.setBorder(new LineBorder(darkTile));
        gui.add(chessBoard);
        Insets buttonMargin = new Insets(0, 0, 0, 0);
        for (int i = 0; i < chessBoardSquares.length; i++) {
            for (int j = 0; j < chessBoardSquares[i].length; j++) {
                JButton b = new JButton();
                b.setMargin(buttonMargin);
                b.setIcon(getIcon(i, j));
                if ((j % 2 == 1 && i % 2 == 1) || (j % 2 == 0 && i % 2 == 0)) {
                    b.setBackground(lightTile);
                } else {
                    b.setBackground(darkTile);
                }
                b.setActionCommand(Integer.toString(i) + " " + Integer.toString(j));
                b.addActionListener(this);
                chessBoardSquares[j][i] = b;
            }
        }

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                chessBoard.add(chessBoardSquares[j][i]);
            }
        }
    }

    public final void refreshBoard() {
        for (int i = 0; i < chessBoardSquares.length; i++) {
            for (int j = 0; j < chessBoardSquares[i].length; j++) {
                chessBoardSquares[j][i].setIcon(getIcon(i, j));
            }
        }
    }

    public final JComponent getChessBoardGUI() {
        return chessBoard;
    }

    public final JComponent getGui() {
        return gui;
    }

    public void setFen(String fen) {
        for (int i = 0; i < 8; i++) {
            pieces[i] = new String[8];
            for (int j = 0; j < 8; j++) {
                pieces[i][j] = "";
            }
        }
        fen = fen.split(" ")[0];
        String[] columns = fen.split("/");
        for (int i = 0; i < columns.length; i++) {
            String[] row = columns[i].split("");
            int tileCount = 0;
            for (int rowCount = 0; rowCount < row.length; rowCount++) {
                String c = row[rowCount];
                try {
                    int n = Integer.parseInt(c);
                    tileCount += n - 1;
                } catch (Exception e) {
                    pieces[i][tileCount] = row[rowCount];
                }
                tileCount++;
            }
        }
    }

    private ImageIcon getIcon(int i, int j) {
        String c = pieces[i][j];
        if (c == "") {
            return new ImageIcon(new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB));
        } else {
            if (c == c.toLowerCase()) {
                return new ImageIcon("chess/src/main/resources/black_pieces/" + c.toLowerCase() + ".png");
            } else {
                return new ImageIcon("chess/src/main/resources/white_pieces/" + c.toLowerCase() + ".png");
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        String[] s = e.getActionCommand().split(" ");
        int y = Integer.parseInt(s[0]);
        int x = Integer.parseInt(s[1]);
        lastClicked = Square.squareAt(56+x-8*y);
    }

    public Square getLastClicked(){
        return lastClicked;
    }
}
