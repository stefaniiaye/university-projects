import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class PacmanController extends KeyAdapter {
    private boolean ctrl = false;
    private boolean shift = false;
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_UP) {
            Game.pacDir = 'U';
        } else if (keyCode == KeyEvent.VK_DOWN) {
            Game.pacDir = 'D';
        } else if (keyCode == KeyEvent.VK_LEFT) {
            Game.pacDir = 'L';
        } else if (keyCode == KeyEvent.VK_RIGHT) {
            Game.pacDir = 'R';
        }else if (keyCode == KeyEvent.VK_CONTROL) {
            ctrl = true;
        } else if (keyCode == KeyEvent.VK_SHIFT) {
            shift = true;
        } else if (keyCode == KeyEvent.VK_Q && ctrl && shift) {
            int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to return to the main menu?", "Game Interrupt", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                GameLoop.newMenu = true;
            }
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
            ctrl = false;
        } else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            shift = false;
        }
    }
}