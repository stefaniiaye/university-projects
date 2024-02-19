import javax.swing.*;
public class GameLoop extends Thread{
    private int score;
    private int s,m;
    public Game game;
    public Thread gameT;
    private int boardC;
    private int boardR;
    public static boolean newMenu;
    int dotCount;

    public GameLoop(int c, int r){
        this.boardC = c;
        this.boardR = r;
        newMenu = false;
        this.game = new Game(c,r);
        this.gameT = new Thread(game);
        gameT.start();
    }
    public void winCheck(){
        dotCount = 0;
        for (int i = 0; i < Game.board.getRowCount(); i++) {
            for (int j = 0; j < Game.board.getColumnCount(); j++) {
                if (Game.board.getValueAt(i, j).equals(boardFigures.EMPTY)) {
                    dotCount++;
                }
            }
        }
    }

    @Override
    public void run() {
        while(game.lives>0){
            winCheck();
            if(dotCount==0){
                game.lives=0;
                this.score += game.score;
                this.s += game.timeS;
                this.m += game.timeM;
                game.dispose();
                game.ghosts.interrupt();
                game.timerT.interrupt();
                game.bonusProbabilityThread.interrupt();
                game.bonusThread.interrupt();
                gameT.interrupt();
                this.game = new Game(boardC,boardR);
                game.timeS = s;
                game.timeM = m;
                game.score = score;
                this.score = 0;
                this.s = 0;
                this.m = 0;
                this.gameT = new Thread(game);
                gameT.start();
            }
        }
        if(game.lives==0){
            score = game.score;
            String name = JOptionPane.showInputDialog(null, "Your score is "+score+" enter name by which you want be save in High scores:", "Name Input", JOptionPane.PLAIN_MESSAGE);
            Menu.highScores.addScore(name,score);
            Menu.highScores.saveScores();
            newMenu = true;
        }else if(newMenu){
            game.lives = 0;
            game.dispose();
        }
    }
}
