package chess;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class BoardGUI {
    private final JPanel mainPanel = new JPanel(new BorderLayout(1, 1));
    private JButton[][] chessBoardSquares = new JButton[8][8];
    private JPanel chessBoard;
    private String[][] pieces;

    private final Color darkTile = new Color(140, 162, 173);
    private final Color lightTile = new Color(230, 227, 222);

    private int xCheck;
    private int yCheck;

    private ImageIcon[][] icons = new ImageIcon[2][];
    private Map<String, Integer> iconMap;

    BoardGUI() {
        pieces = new String[8][8];
        for (int i = 0; i < 8; i++) {
            pieces[i] = new String[8];
            for (int j = 0; j < 8; j++) {
                pieces[i][j] = "";
            }
        }
        loadIcons();
        initialiseGui();
    }

    public final void initialiseGui() {
        mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        chessBoard = new JPanel(new GridLayout(0, 8));
        chessBoard.setBorder(new LineBorder(darkTile));
        chessBoard.setMinimumSize(chessBoard.getSize());
        chessBoard.setMinimumSize(chessBoard.getSize());
        mainPanel.add(chessBoard);
        Insets buttonMargin = new Insets(0, 0, 0, 0);
        for (int i = 0; i < chessBoardSquares.length; i++) {
            for (int j = 0; j < chessBoardSquares[i].length; j++) {
                JButton b = new JButton();
                b.setMargin(buttonMargin);
                b.setIcon(getIcon(i, j));
                if ((j % 2 == 1 && i % 2 == 1) || (j % 2 == 0 && i % 2 == 0))
                    b.setBackground(lightTile);
                else
                    b.setBackground(darkTile);

                b.setActionCommand(Integer.toString(i) + " " + Integer.toString(j));
                chessBoardSquares[j][i] = b;
            }
        }

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                chessBoard.add(chessBoardSquares[j][i]);
            }
        }
    }

    public void addActionListener(int i, int j, ActionListener listener) {
        chessBoardSquares[i][j].addActionListener(listener);
    }

    public final void refreshBoard() {
        for (int i = 0; i < chessBoardSquares.length; i++) {
            for (int j = 0; j < chessBoardSquares[i].length; j++) {
                if ((j % 2 == 1 && i % 2 == 1) || (j % 2 == 0 && i % 2 == 0)) {
                    chessBoardSquares[j][i].setBackground(lightTile);
                } else {
                    chessBoardSquares[j][i].setBackground(darkTile);
                }
                chessBoardSquares[j][i].setIcon(getIcon(i, j));
            }
        }
        if (xCheck != -1 && yCheck != -1) {
            chessBoardSquares[xCheck][yCheck].setBackground(new Color(190, 40, 60));
        }
    }

    public void setCheckSquare(int x, int y) {
        xCheck = x;
        yCheck = y;
    }

    public final JComponent getChessBoardGUI() {
        return chessBoard;
    }

    public final JComponent getGui() {
        return mainPanel;
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
        for(int i = 0; i < 2; i++) {
            for(int j = 0; j < 6; j++) {
                icons[i][j] = new ImageIcon("chess/src/main/resources/" + Integer.toString(i) + "/" + Integer.toString(j) + ".png");
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
}