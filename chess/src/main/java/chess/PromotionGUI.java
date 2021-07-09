package chess;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class PromotionGUI {
    public JPanel grid;
    private JButton[] buttons;
    private final Color darkTile = new Color(140, 162, 173);

    public PromotionGUI() {
        grid = new JPanel(new GridLayout(2, 2));
        createGrid();
    }

    private void createGrid() {
        buttons = new JButton[4];
        Insets buttonMargin = new Insets(1, 1, 1, 1);
        for (int i = 0; i < 4; i++) {
            buttons[i] = new JButton();
            buttons[i].setActionCommand(Integer.toString(i));
            buttons[i].setBackground(new Color(230, 227, 222));
            buttons[i].setMargin(buttonMargin);
            buttons[i].setVisible(true);
            // TODO: Add action listeners
            grid.add(buttons[i]);
        }
        grid.setBorder(new LineBorder(darkTile));
        grid.setMinimumSize(grid.getSize());
        grid.setMinimumSize(grid.getSize());
        grid.setVisible(true);
        System.out.println("hello");

    }

    public void refreshIcons(boolean black) {
        String[] pieceList = new String[] { "q", "r", "b", "n" };
        for (int i = 0; i < 4; i++) {
            String path = "chess/src/main/resources/white_pieces_small/";
            if (black)
                path = "chess/src/main/resources/black_pieces_small/";
            buttons[i].setIcon(new ImageIcon(path + pieceList[i] + ".png"));
        }
    }
}
