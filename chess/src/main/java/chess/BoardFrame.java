package chess;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class BoardFrame extends JFrame {
    private final JPanel mainPanel = new JPanel(new FlowLayout());
    private final JPanel buttonPanel = new JPanel(new GridLayout(0, 1));
    private JButton[] promotionSquares = new JButton[4];
    private JButton flipButton;
    private ChessboardPanel chessboardPanel;
    private JPanel promotionGrid;
    public static Color darkTile = new Color(140, 162, 173);
    public static Color lightTile = new Color(230, 227, 222);
    
    BoardFrame(boolean showPlayerUI) {
        frameSetup();
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        initialiseChessboard();
        if (showPlayerUI) {
            initialisePromotionGrid();
            initialiseFlipButton();
            mainPanel.add(buttonPanel);
        }
    }

    private void frameSetup() {
        setTitle("Chess");
        add(mainPanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationByPlatform(true);
        pack();
        setMinimumSize(new Dimension(810, 715));
        setMaximumSize(new Dimension(810, 715));
        setVisible(true);
    }

    private void initialiseFlipButton() {
        flipButton = new JButton();
        JPanel flipButtonPanel = new JPanel();
        flipButtonPanel.add(flipButton);
        flipButton.setText("    Flip board    ");
        flipButton.setForeground(darkTile);
        flipButton.setFont(flipButton.getFont().deriveFont(12f));
        flipButton.setBackground(lightTile);
        flipButton.setBorder(BorderFactory.createLineBorder(darkTile, 1));
        flipButton.setSize(new Dimension(50, 20));
        flipButton.setActionCommand("f");
        buttonPanel.add(flipButtonPanel);
    }
    
    private void initialiseChessboard() {
        chessboardPanel = new ChessboardPanel(new GridLayout(8, 8));
        mainPanel.add(chessboardPanel);
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
    
    public final void setPromotionColour(Color colour) {
        promotionGrid.setBorder(BorderFactory.createLineBorder(colour, 2));
    }

    public void addPromotionActionListener(int i, ActionListener listener) {
        promotionSquares[i].addActionListener(listener);
    }

    public void addFlipButtonActionListener(ActionListener listener) {
        flipButton.addActionListener(listener);
    }

    public void addBoardActionListener(int i, int j, ActionListener listener) {
        chessboardPanel.addActionListener(i, j, listener);
    }

    public void setCheckSquare(int x, int y) {
        chessboardPanel.setCheckSquare(x, y);
    }

    public void setFen(String fen) {
        chessboardPanel.setFen(fen);
    }

    public void refreshBoard(boolean isFlipped) {
        chessboardPanel.refreshBoard(isFlipped);
    }
}