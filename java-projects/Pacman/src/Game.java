import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;

public class Game extends JFrame implements Runnable{
    public static JTable board;
    public Thread ghosts,timerT;
    private int boardC, boardR;
    public int lives;
    private static JPanel livesP;
    private int pacmanRow;
    private int pacmanCol;
    public static char pacDir;
    private List<Integer[]> ghostPositions = new ArrayList<Integer[]>();
    private List<Integer[]> visitedCells = new ArrayList<Integer[]>();
    public int score;
    private JLabel scoreP;
    public int timeM, timeS;
    private JLabel timer;
    private JPanel scoreAndLives;
    public static int picGhost = 0;
    private JPanel game;
    public static List<Integer> bonuses = new ArrayList<>();
    public static List<Integer> bonusesOnBoard = new ArrayList<>();
    public static boolean plusLive;
    public static boolean plusScore;

    private static JLabel heart1 = new JLabel (new ImageIcon("heart.png"));
    private static JLabel heart2 = new JLabel (new ImageIcon("heart.png"));
    private static JLabel heart3 = new JLabel (new ImageIcon("heart.png"));
    private static JLabel heart4 = new JLabel (new ImageIcon("heart.png"));

    public static int scoreX = 1;
    public static int pacSpeedX = 1;
    public static int ghostSpeedX = 1;
    public BonusThread bonusThread;
    public BonusProbabilityThread bonusProbabilityThread;

    public Game(int c, int r) {
        plusLive = false;
        plusScore = false;
        timeS = 0;
        timeM = 0;
        pacDir = ' ';
        lives = 3;
        boardC = c;
        boardR = r;
        createBoard(c,r);
        scoreP = new JLabel("Score: "+ this.score);
        scoreP.setFont(new Font("Impact", Font.PLAIN, 18));
        scoreP.setForeground(Color.BLACK);

        timer = new JLabel(timeM+" min "+timeS+" sec");
        timer.setFont(new Font("Impact", Font.PLAIN, 18));
        timer.setForeground(Color.BLACK);

        scoreAndLives = new JPanel(new GridLayout(1,3));
        scoreAndLives.setBackground(new Color(78, 91, 97));

        scoreAndLives.add(scoreP);
        scoreP.setHorizontalAlignment(SwingConstants.CENTER);
        scoreAndLives.add(timer);
        timer.setHorizontalAlignment(SwingConstants.CENTER);
        drawLives();
        livesP.setBackground(new Color(78, 91, 97));
        scoreAndLives.add(livesP);

        game = new JPanel(new BorderLayout());
        game.add(scoreAndLives, BorderLayout.PAGE_START);
        game.add(board);

        add(game);
        pack();
        bonusThread = new BonusThread();

        findPacmanPosition();
        board.addKeyListener(new PacmanController());
        ghosts = new Thread(new Runnable() {
            @Override
            public void run() {
                while (lives > 0){
                    findGhostPositions();
                    findPacmanPosition();
                    picGhost++;
                    for(Integer [] g:ghostPositions){
                        moveGhosts(g[0],g[1]);
                    }
                    try {
                        Thread.sleep(1500*ghostSpeedX);
                    } catch (InterruptedException e) {
                        System.err.print(e.getMessage());
                    }
                }
            }
        });
        ghosts.start();
        bonusProbabilityThread = new BonusProbabilityThread();
        bonusProbabilityThread.start();

        timerT = new Thread(new Runnable() {
            @Override
            public void run() {
                while (lives>0){
                    if(timeS < 59){
                        timeS++;
                    }else{
                        timeM++;
                        timeS = 0;
                    }
                    timer.setText(timeM+" min "+timeS+" sec");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        timerT.start();

        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("Pacman");
        board.requestFocus();
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeBoard();
            }
        });


    }

    public void resizeBoard(){
        int width = board.getWidth();
        int height = board.getHeight();
        int cellSize = Math.min(width/boardC,height/boardR);
        board.setRowHeight(cellSize);
        for (int i = 0; i < board.getColumnCount(); i++) {
            board.getColumnModel().getColumn(i).setPreferredWidth(cellSize);
            board.getColumnModel().getColumn(i).setMinWidth(cellSize);
            board.getColumnModel().getColumn(i).setMaxWidth(cellSize);
        }
        board.setDefaultRenderer(Object.class, new BoardTableCellRenderer(cellSize));
    }


    public void createBoard(int c, int r){
        board = new JTable(new BoardModel(c,r));
        int cellSize = Math.min(900/c,800/r);
        board.setRowHeight(cellSize);
        for (int i = 0; i < board.getColumnCount(); i++) {
            board.getColumnModel().getColumn(i).setPreferredWidth(cellSize);
            board.getColumnModel().getColumn(i).setMinWidth(cellSize);
            board.getColumnModel().getColumn(i).setMaxWidth(cellSize);
        }
        board.setDefaultRenderer(Object.class, new BoardTableCellRenderer(cellSize));
        board.setBackground(Color.black);
        board.setShowGrid(false);
    }

    public void drawLives() {
        livesP = new JPanel(new FlowLayout());
        livesP.add(heart1);
        livesP.add(heart2);
        livesP.add(heart3);
    }
    public static void plusLiveBonus(){
        if(livesP.getComponentCount() < 4){
            livesP.add(heart4);
            livesP.repaint();
            plusLive = true;
        }
    }
    public void findPacmanPosition(){
        for (int i = 0; i < board.getRowCount(); i++) {
            for (int j = 0; j < board.getColumnCount(); j++) {
                if (board.getValueAt(i, j).equals(boardFigures.PACMAN)) {
                    pacmanRow = i;
                    pacmanCol = j;
                    break;
                }
            }
        }
        visitedCells.add(new Integer[]{pacmanRow,pacmanCol});
    }
    public synchronized void movePacman(){
        switch (pacDir){
            case 'U' ->{
                if(!board.getValueAt(pacmanRow-1,pacmanCol).equals(boardFigures.WALL)){
                    if(board.getValueAt(pacmanRow-1,pacmanCol).equals(boardFigures.EMPTY)){
                        board.setValueAt(boardFigures.PACMAN,pacmanRow-1,pacmanCol);
                        board.setValueAt(boardFigures.EMNODOT,pacmanRow,pacmanCol);
                        score+=scoreX;
                        scoreP.setText("Score: "+ this.score);
                    }
                    else if(board.getValueAt(pacmanRow-1,pacmanCol).equals(boardFigures.EMNODOT)){
                        board.setValueAt(boardFigures.PACMAN,pacmanRow-1,pacmanCol);
                        board.setValueAt(boardFigures.EMNODOT,pacmanRow,pacmanCol);
                    }
                    else if(board.getValueAt(pacmanRow-1,pacmanCol).equals(boardFigures.FRUIT)){
                        board.setValueAt(boardFigures.PACMAN,pacmanRow-1,pacmanCol);
                        board.setValueAt(boardFigures.EMNODOT,pacmanRow,pacmanCol);
                        score+=5;
                    }
                    else if(board.getValueAt(pacmanRow-1,pacmanCol).equals(boardFigures.CANDY)){
                        board.setValueAt(boardFigures.PACMAN,pacmanRow-1,pacmanCol);
                        board.setValueAt(boardFigures.EMNODOT,pacmanRow,pacmanCol);
                        bonusThread = new BonusThread();
                        bonusThread.start();
                    }
                }
            }
            case 'D' ->{
                if(!board.getValueAt(pacmanRow+1,pacmanCol).equals(boardFigures.WALL)){
                    if(board.getValueAt(pacmanRow+1,pacmanCol).equals(boardFigures.EMPTY)){
                        board.setValueAt(boardFigures.PACMAN,pacmanRow+1,pacmanCol);
                        board.setValueAt(boardFigures.EMNODOT,pacmanRow,pacmanCol);
                        score+=scoreX;
                        scoreP.setText("Score: "+ this.score);
                    }
                    else if(board.getValueAt(pacmanRow+1,pacmanCol).equals(boardFigures.EMNODOT)){
                        board.setValueAt(boardFigures.PACMAN,pacmanRow+1,pacmanCol);
                        board.setValueAt(boardFigures.EMNODOT,pacmanRow,pacmanCol);
                    }
                    else if(board.getValueAt(pacmanRow+1,pacmanCol).equals(boardFigures.FRUIT)){
                        board.setValueAt(boardFigures.PACMAN,pacmanRow+1,pacmanCol);
                        board.setValueAt(boardFigures.EMNODOT,pacmanRow,pacmanCol);
                        score+=5;
                    }
                    else if(board.getValueAt(pacmanRow+1,pacmanCol).equals(boardFigures.CANDY)){
                        board.setValueAt(boardFigures.PACMAN,pacmanRow+1,pacmanCol);
                        board.setValueAt(boardFigures.EMNODOT,pacmanRow,pacmanCol);
                        bonusThread = new BonusThread();
                        bonusThread.start();
                    }
                }
            }
            case 'L' ->{
                if(!board.getValueAt(pacmanRow,pacmanCol-1).equals(boardFigures.WALL)){
                    if(board.getValueAt(pacmanRow,pacmanCol-1).equals(boardFigures.EMPTY)){
                        board.setValueAt(boardFigures.PACMAN,pacmanRow,pacmanCol-1);
                        board.setValueAt(boardFigures.EMNODOT,pacmanRow,pacmanCol);
                        score+=scoreX;
                        scoreP.setText("Score: "+ this.score);
                    }
                    else if(board.getValueAt(pacmanRow,pacmanCol-1).equals(boardFigures.EMNODOT)){
                        board.setValueAt(boardFigures.PACMAN,pacmanRow,pacmanCol-1);
                        board.setValueAt(boardFigures.EMNODOT,pacmanRow,pacmanCol);
                    }
                    else if(board.getValueAt(pacmanRow,pacmanCol-1).equals(boardFigures.FRUIT)){
                        board.setValueAt(boardFigures.PACMAN,pacmanRow,pacmanCol-1);
                        board.setValueAt(boardFigures.EMNODOT,pacmanRow,pacmanCol);
                        score+=5;
                    }
                    else if(board.getValueAt(pacmanRow,pacmanCol-1).equals(boardFigures.CANDY)){
                        board.setValueAt(boardFigures.PACMAN,pacmanRow,pacmanCol-1);
                        board.setValueAt(boardFigures.EMNODOT,pacmanRow,pacmanCol);
                        bonusThread = new BonusThread();
                        bonusThread.start();
                    }
                }
            }
            case 'R' ->{
                if(!board.getValueAt(pacmanRow,pacmanCol+1).equals(boardFigures.WALL)){
                    if(board.getValueAt(pacmanRow,pacmanCol+1).equals(boardFigures.EMPTY)){
                        board.setValueAt(boardFigures.PACMAN,pacmanRow,pacmanCol+1);
                        board.setValueAt(boardFigures.EMNODOT,pacmanRow,pacmanCol);
                        score+=scoreX;
                        scoreP.setText("Score: "+ this.score);

                    }
                    else if(board.getValueAt(pacmanRow,pacmanCol+1).equals(boardFigures.EMNODOT)){
                        board.setValueAt(boardFigures.PACMAN,pacmanRow,pacmanCol+1);
                        board.setValueAt(boardFigures.EMNODOT,pacmanRow,pacmanCol);
                    }
                    else if(board.getValueAt(pacmanRow,pacmanCol+1).equals(boardFigures.FRUIT)){
                        board.setValueAt(boardFigures.PACMAN,pacmanRow,pacmanCol+1);
                        board.setValueAt(boardFigures.EMNODOT,pacmanRow,pacmanCol);
                        score+=5;
                    }
                    else if(board.getValueAt(pacmanRow,pacmanCol+1).equals(boardFigures.CANDY)){
                        board.setValueAt(boardFigures.PACMAN,pacmanRow,pacmanCol+1);
                        board.setValueAt(boardFigures.EMNODOT,pacmanRow,pacmanCol);
                        bonusThread = new BonusThread();
                        bonusThread.start();
                    }
                }
            }
        }
        repaint();
    }
    public synchronized void findGhostPositions(){
        if(ghostPositions.size()>0){
            ghostPositions.clear();
        }
        for (int i = 0; i < board.getRowCount(); i++) {
            for (int j = 0; j < board.getColumnCount(); j++) {
                if (board.getValueAt(i, j).equals(boardFigures.GHOST)) {
                    ghostPositions.add(new Integer[]{i,j});
                }
            }
        }
    }
    public synchronized void moveGhosts(int x, int y){
        double minDistToPac = Double.MAX_VALUE;
        int minX = x;
        int minY = y;
        String dir = "";
        if(board.getValueAt(x - 1,y).equals(boardFigures.EMPTY) || board.getValueAt(x - 1,y).equals(boardFigures.PACMAN)
                || board.getValueAt(x - 1,y).equals(boardFigures.EMNODOT)){
            double dist = Math.sqrt(Math.pow((x-1)-pacmanRow,2)+Math.pow(y-pacmanCol,2));
            if(dist<minDistToPac){
                minDistToPac = dist;
                dir = "up";
            }
        }
        if(board.getValueAt(x + 1,y).equals(boardFigures.EMPTY) || board.getValueAt(x + 1,y).equals(boardFigures.PACMAN)
                || board.getValueAt(x + 1,y).equals(boardFigures.EMNODOT)){
            double dist = Math.sqrt(Math.pow((x+1)-pacmanRow,2)+Math.pow(y-pacmanCol,2));
            if(dist<minDistToPac){
                minDistToPac = dist;
                dir = "down";
            }
        }
        if(board.getValueAt(x,y-1).equals(boardFigures.EMPTY) || board.getValueAt(x,y-1).equals(boardFigures.PACMAN)
                || board.getValueAt(x,y-1).equals(boardFigures.EMNODOT)){
            double dist = Math.sqrt(Math.pow(x-pacmanRow,2)+Math.pow(y-1-pacmanCol,2));
            if(dist<minDistToPac){
                minDistToPac = dist;
                dir = "left";
            }
        }
        if(board.getValueAt(x,y+1).equals(boardFigures.EMPTY) || board.getValueAt(x,y+1).equals(boardFigures.PACMAN)
                || board.getValueAt(x,y+1).equals(boardFigures.EMNODOT)){
            double dist = Math.sqrt(Math.pow(x-pacmanRow,2)+Math.pow(y+1-pacmanCol,2));
            if(dist<minDistToPac){
                minDistToPac = dist;
                dir = "right";
            }
        }
        switch (dir){
            case "up" -> minX = x - 1;

            case "down" -> minX = x + 1;

            case "left" -> minY = y - 1;
            case "right" -> minY = y + 1;
        }
        if(x != minX || y != minY) {
            if (board.getValueAt(minX, minY).equals(boardFigures.PACMAN)) {
                lives--;
                if(livesP.getComponentCount()>0) {
                    livesP.remove(0);
                }
                repaint();
                    board.setValueAt(boardFigures.EMNODOT,pacmanRow,pacmanCol);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    board.setValueAt(boardFigures.PACMAN,pacmanRow,pacmanCol);
            } else {
                if(bonuses.size()>0){
                    board.setValueAt(boardFigures.CANDY, x, y);
                    board.setValueAt(boardFigures.GHOST, minX, minY);
                    bonusesOnBoard.add(bonuses.get(0));
                    bonuses.remove(0);

                }else {
                    boolean isVisited = false;
                    for (Integer[] v : visitedCells) {
                        if (v[0] == x && v[1] == y) {
                            board.setValueAt(boardFigures.EMNODOT, x, y);
                            board.setValueAt(boardFigures.GHOST, minX, minY);
                            isVisited = true;
                            break;
                        }
                    }
                    if (!isVisited) {
                        board.setValueAt(boardFigures.EMPTY, x, y);
                        board.setValueAt(boardFigures.GHOST, minX, minY);
                    }
                }
                repaint();
            }
        }
    }

    @Override
    public void run() {
        while (lives > 0){
            if(plusLive){
                lives++;
                plusLive=false;
            }
            if(plusScore){
                score+=100;
                plusScore = false;
            }
            findPacmanPosition();
            movePacman();
            repaint();
            try {
                Thread.sleep(300/pacSpeedX);
            } catch (InterruptedException e) {
                System.err.print(e.getMessage());
            }
        }
    }
}
