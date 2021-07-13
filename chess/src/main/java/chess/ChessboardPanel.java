package chess;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.awt.image.BufferedImage;
import java.awt.event.*;

public class ChessboardPanel extends JPanel {
    private JButton[][] chessboardSquares = new JButton[8][8];
    private ImageIcon[][] icons = new ImageIcon[2][];
    private Map<String, Integer> iconMap;
    private String[][] pieces;
    private int xCheck;
    private int yCheck;

    public ChessboardPanel(GridLayout layout) {
        super(layout);
        initialisePieces();
        loadIcons();
        initialisePanel();
    }

    private void initialisePieces() {
        pieces = new String[8][];
        for (int i = 0; i < 8; i++) {
            pieces[i] = new String[8];
            for (int j = 0; j < 8; j++) {
                pieces[i][j] = "";
            }
        }
    }

    private void initialisePanel() {
        setBorder(new LineBorder(BoardFrame.darkTile));
        setMinimumSize(getSize());
        setMinimumSize(getSize());
        Insets buttonMargin = new Insets(-3, -3, -3, -3);
        for (int i = 0; i < chessboardSquares.length; i++) {
            for (int j = 0; j < chessboardSquares[i].length; j++) {
                JButton b = new JButton();
                b.setMargin(buttonMargin);
                b.setIcon(getIcon(i, j));
                if ((j % 2 == 1 && i % 2 == 1) || (j % 2 == 0 && i % 2 == 0))
                    b.setBackground(BoardFrame.lightTile);
                else
                    b.setBackground(BoardFrame.darkTile);
                b.setFocusPainted(false);
                b.setActionCommand("m " + Integer.toString(i) + " " + Integer.toString(j));
                chessboardSquares[j][i] = b;
            }
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++)
                add(chessboardSquares[j][i]);
        }
    }

    private void loadIcons() {
        iconMap = new HashMap<String, Integer>();
        iconMap.put("q", 0);
        iconMap.put("k", 1);
        iconMap.put("r", 2);
        iconMap.put("b", 3);
        iconMap.put("n", 4);
        iconMap.put("p", 5);

        icons[0] = new ImageIcon[6];
        icons[1] = new ImageIcon[6];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 6; j++) {
                icons[i][j] = new ImageIcon(
                        "chess/src/main/resources/" + Integer.toString(i) + "/" + Integer.toString(j) + ".png");
            }
        }
    }

    private ImageIcon getIcon(int i, int j) {
        String c = pieces[i][j];
        if (c == "")
            return new ImageIcon(new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB));
        String lower = c.toLowerCase();
        int colour = c == lower ? 0 : 1;
        return icons[colour][iconMap.get(lower)];
    }

    public final void refreshBoard(boolean isFlipped) {
        for (int i = 0; i < chessboardSquares.length; i++) {
            for (int j = 0; j < chessboardSquares[i].length; j++) {
                if ((j % 2 == 1 && i % 2 == 1) || (j % 2 == 0 && i % 2 == 0)) {
                    chessboardSquares[j][i].setBackground(BoardFrame.lightTile);
                } else {
                    chessboardSquares[j][i].setBackground(BoardFrame.darkTile);
                }
                if(!isFlipped) {
                    chessboardSquares[j][i].setIcon(getIcon(i, j));
                } else {
                    chessboardSquares[j][i].setIcon(getIcon(7-i, 7-j));
                }
            }
        }
        if (xCheck != -1 && yCheck != -1) {
            chessboardSquares[xCheck][yCheck].setBackground(new Color(190, 40, 60));
        }
    }

    public void setFen(String fen) {
        for (int i = 0; i < 8; i++) {
            pieces[i] = new String[8];
            for (int j = 0; j < 8; j++)
                pieces[i][j] = "";
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

    public void addActionListener(int i, int j, ActionListener listener) {
        chessboardSquares[i][j].addActionListener(listener);
    }

    public void setCheckSquare(int x, int y) {
        xCheck = x;
        yCheck = y;
    }

}