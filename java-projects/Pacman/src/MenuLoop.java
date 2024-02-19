import javax.swing.*;

public class MenuLoop extends Thread{
    Menu menu;
    public MenuLoop(){
        SwingUtilities.invokeLater(() -> menu = new Menu());
    }

    @Override
    public void run() {
        while (!Thread.interrupted()){
            if(GameLoop.newMenu){
                GameLoop.newMenu = false;
                menu.pacman.game.ghosts.interrupt();
                menu.pacman.gameT.interrupt();
                menu.pacmanT.interrupt();
                menu.pacman.game.timerT.interrupt();
                menu.pacman.game.bonusThread.interrupt();
                menu.pacman.game.bonusProbabilityThread.interrupt();
                menu.pacman.game.dispose();
                SwingUtilities.invokeLater(() -> menu = new Menu());
            }
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
