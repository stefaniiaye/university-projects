import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class BoardTableCellRenderer extends JLabel implements TableCellRenderer {
    private ImageIcon pacman_R;
    private ImageIcon pacman_L;
    private ImageIcon pacman_U;
    private ImageIcon pacman_D;
    private ImageIcon pacman;

    private ImageIcon ghost1;
    private ImageIcon ghost2;
    private ImageIcon fruit;
    private ImageIcon wall;
    private ImageIcon dot;
    private ImageIcon candy;
    private ImageIcon scaledPacmanR, scaledPacmanL, scaledPacmanU,scaledPacmanD,scaledPacman,scaledGhost1,scaledGhost2,
            scaledFruit, scaledWall,scaledCandy;


    int cellSize;

    public BoardTableCellRenderer(int cellSize){
        this.cellSize = cellSize;
        pacman_R = new ImageIcon("pics/pacright.png");
        scaledPacmanR = new ImageIcon(pacman_R.getImage().getScaledInstance(cellSize, cellSize, Image.SCALE_SMOOTH));

        pacman_L = new ImageIcon("pics/pacleft.png");
        scaledPacmanL = new ImageIcon(pacman_L.getImage().getScaledInstance(cellSize, cellSize, Image.SCALE_SMOOTH));

        pacman_U = new ImageIcon("pics/pacup.png");
        scaledPacmanU = new ImageIcon(pacman_U.getImage().getScaledInstance(cellSize, cellSize, Image.SCALE_SMOOTH));

        pacman_D = new ImageIcon("pics/pacdown.png");
        scaledPacmanD = new ImageIcon(pacman_D.getImage().getScaledInstance(cellSize, cellSize, Image.SCALE_SMOOTH));

        pacman = new ImageIcon("pics/pacman.png");
        scaledPacman = new ImageIcon(pacman.getImage().getScaledInstance(cellSize, cellSize, Image.SCALE_SMOOTH));

        ghost1 = new ImageIcon("pics/ghost1.png");
        scaledGhost1 = new ImageIcon(ghost1.getImage().getScaledInstance(cellSize, cellSize, Image.SCALE_SMOOTH));

        ghost2 = new ImageIcon("pics/ghost2.png");
        scaledGhost2 = new ImageIcon(ghost2.getImage().getScaledInstance(cellSize, cellSize, Image.SCALE_SMOOTH));

        fruit = new ImageIcon("pics/fruit.png");
        scaledFruit = new ImageIcon(fruit.getImage().getScaledInstance(cellSize, cellSize, Image.SCALE_SMOOTH));

        wall = new ImageIcon("pics/brick.png");
        scaledWall = new ImageIcon(wall.getImage().getScaledInstance(cellSize, cellSize, Image.SCALE_SMOOTH));

        dot = new ImageIcon("pics/dot.png");
        candy = new ImageIcon("pics/candy.png");
        scaledCandy = new ImageIcon(candy.getImage().getScaledInstance(cellSize, cellSize, Image.SCALE_SMOOTH));

        setHorizontalAlignment(CENTER);
        setVerticalAlignment(CENTER);

    }
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value == boardFigures.PACMAN) {
            switch (Game.pacDir){
                case 'U' -> setIcon(scaledPacmanU);
                case 'D' -> setIcon(scaledPacmanD);
                case 'L' -> setIcon(scaledPacmanL);
                case 'R' -> setIcon(scaledPacmanR);
                default -> setIcon(scaledPacman);
            }
        } else if (value == boardFigures.GHOST) {
            if(Game.picGhost%2==0){
                setIcon(scaledGhost1);
            }else{
                setIcon(scaledGhost2);
            }
        } else if (value == boardFigures.FRUIT) {
            setIcon(scaledFruit);
        } else if (value == boardFigures.WALL) {
            setIcon(scaledWall);
        }
        else if (value == boardFigures.EMPTY) {
            setIcon(dot);
        }
        else if (value == boardFigures.CANDY) {
            setIcon(scaledCandy);
        }
        else {
            setIcon(null);
        }
        return this;
    }
}
