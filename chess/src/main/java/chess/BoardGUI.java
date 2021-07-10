package chess;

import javax.swing.*;
import javax.swing.border.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class BoardGUI {
    private final JPanel mainPanel = new JPanel(new FlowLayout());
    private final JPanel buttonPanel = new JPanel(new CardLayout());
    private JButton[][] chessBoardSquares = new JButton[8][8];
    private JButton[] promotionSquares = new JButton[4];
    private JPanel chessBoard;
    private JPanel promotionGrid;
    private Component flipButton;
    private String[][] pieces;

    public static Color darkTile = new Color(140, 162, 173);
    public static Color lightTile = new Color(230, 227, 222);

    private int xCheck;
    private int yCheck;

    private ImageIcon[][] icons = new ImageIcon[2][];
    private Map<String, Integer> iconMap;

    BoardGUI(boolean showPlayerUI) {
        pieces = new String[8][];
        for (int i = 0; i < 8; i++) {
            pieces[i] = new String[8];
            for (int j = 0; j < 8; j++) {
                pieces[i][j] = "";
            }
        }
        loadIcons();
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        buttonPanel.setBorder(BorderFactory.createLineBorder(darkTile, 1));
        initialiseChessboard();
        if (showPlayerUI) {
            initialisePromotionGrid();
            initialiseFlipButton();
            mainPanel.add(buttonPanel);
        }
    }

    private void initialiseFlipButton() {
        flipButton = new JButton();
        buttonPanel.add(flipButton);

    }

    private void initialiseChessboard() {
        chessBoard = new JPanel(new GridLayout(8, 8));
        chessBoard.setBorder(new LineBorder(darkTile));
        chessBoard.setMinimumSize(chessBoard.getSize());
        chessBoard.setMinimumSize(chessBoard.getSize());
        mainPanel.add(chessBoard);
        Insets buttonMargin = new Insets(-3, -3, -3, -3);
        for (int i = 0; i < chessBoardSquares.length; i++) {
            for (int j = 0; j < chessBoardSquares[i].length; j++) {
                JButton b = new JButton();
                b.setMargin(buttonMargin);
                b.setIcon(getIcon(i, j));
                if ((j % 2 == 1 && i % 2 == 1) || (j % 2 == 0 && i % 2 == 0))
                    b.setBackground(lightTile);
                else
                    b.setBackground(darkTile);
                b.setFocusPainted(false);
                b.setActionCommand(Integer.toString(i) + " " + Integer.toString(j));
                chessBoardSquares[j][i] = b;
            }
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++)
                chessBoard.add(chessBoardSquares[j][i]);
        }
    }

    private void initialisePromotionGrid() {
        promotionGrid = new JPanel(new GridLayout(2, 2));
        buttonPanel.add(promotionGrid);
        Insets buttonMargin = new Insets(-2, -2, -2, -2);
        for (int i = 0; i < 4; i++) {
            JButton b = new JButton();
            if (i == 0 || i == 3)
                b.setBackground(lightTile);
            else
                b.setBackground(darkTile);
            b.setMargin(buttonMargin);
            b.setActionCommand("p " + Integer.toString(i));
            b.setFocusPainted(false);
            promotionGrid.add(b);
            promotionSquares[i] = b;
        }
        promotionGrid.setBorder(new LineBorder(darkTile));
        promotionGrid.setMinimumSize(new Dimension(400, 400));
        updatePromotionGrid(true);
        promotionGrid.setVisible(true);
    }

    public void updatePromotionGrid(boolean white) {
        String[] pieceList = new String[] { "q", "r", "b", "n" };
        for (int i = 0; i < 4; i++) {
            String path = "chess/src/main/resources/black_pieces_small/";
            if (white)
                path = "chess/src/main/resources/white_pieces_small/";
            promotionSquares[i].setIcon(new ImageIcon(path + pieceList[i] + ".png"));
        }
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
        if (xCheck != -1 && yCheck != -1)
            chessBoardSquares[xCheck][yCheck].setBackground(new Color(190, 40, 60));
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

    public void setTurnColour(boolean white) {
        updatePromotionGrid(white);
        if (white)
            buttonPanel.setBackground(lightTile);
        else
            buttonPanel.setBackground(darkTile);
    }

    public void setCheckSquare(int x, int y) {
        xCheck = x;
        yCheck = y;
    }

    public final void setPromotionColour(Color colour) {
        promotionGrid.setBorder(BorderFactory.createLineBorder(colour, 2));
    }

    public final JComponent getChessBoardGUI() {
        return chessBoard;
    }

    public final JComponent getGui() {
        return mainPanel;
    }

    private ImageIcon getIcon(int i, int j) {
        String c = pieces[i][j];
        if (c == "")
            return new ImageIcon(new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB));
        String lower = c.toLowerCase();
        int colour = c == lower ? 0 : 1;
        return icons[colour][iconMap.get(lower)];
    }

    public void addBoardActionListener(int i, int j, ActionListener listener) {
        chessBoardSquares[i][j].addActionListener(listener);
    }

    public void addPromotionActionListener(int i, ActionListener listener) {
        promotionSquares[i].addActionListener(listener);
    }
}