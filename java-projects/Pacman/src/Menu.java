import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class Menu extends JFrame {
    Thread pacmanT;
    GameLoop pacman;
    public static HighScores highScores;
    private DefaultListModel<PlayerScore> listModel;
    private JList<PlayerScore> highScoresL;

    public Menu(){
        highScores = new HighScores();
        listModel = new DefaultListModel<>();
        highScoresL = new JList<>(listModel);

        ImageIcon logo = new ImageIcon("pics/logo2.gif");
        Image image = logo.getImage();
        Image scaledImage = image.getScaledInstance(300,125 , Image.SCALE_SMOOTH);
        JLabel title = new JLabel(new ImageIcon(scaledImage));
        title.setHorizontalAlignment(SwingConstants.CENTER);

        JButton newGameButton = new JButton("New Game");
        newGameButton.setFont(new Font("Impact", Font.PLAIN, 25));
        newGameButton.setBorder(new LineBorder(Color.PINK, 3));
        newGameButton.setBackground(Color.BLACK);
        newGameButton.setForeground(Color.YELLOW);
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showNewGameDialog();
                dispose();
            }
        });

        JButton highScoresButton = new JButton("High Scores");
        highScoresButton.setFont(new Font("Impact", Font.PLAIN, 25));
        highScoresButton.setBorder(new LineBorder(Color.RED, 3));
        highScoresButton.setBackground(Color.BLACK);
        highScoresButton.setForeground(Color.YELLOW);

        highScoresButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                highScores.loadScores();
                displayHighScores();
            }
        });

        JButton exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Impact", Font.PLAIN, 25));
        exitButton.setBorder(new LineBorder(Color.CYAN, 3));
        exitButton.setBackground(Color.BLACK);
        exitButton.setForeground(Color.YELLOW);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });


        JPanel mainMenuPanel = new JPanel(new GridLayout(4, 1));
        mainMenuPanel.setBackground(Color.BLACK);
        mainMenuPanel.add(title);
        mainMenuPanel.add(newGameButton);
        mainMenuPanel.add(highScoresButton);
        mainMenuPanel.add(exitButton);

        add(mainMenuPanel);


        setSize(500, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        setBackground(Color.BLACK);
    }
    public void displayHighScores() {
        listModel.clear();
        List<PlayerScore> scores = highScores.getScores();
        scores.forEach(listModel::addElement);

        highScoresL.setBackground(Color.BLACK);
        highScoresL.setFont(new Font("Impact", Font.PLAIN, 15));
        highScoresL.setForeground(Color.YELLOW);
        highScoresL.setBorder(new LineBorder(Color.CYAN,5));


        JScrollPane scrollPane = new JScrollPane(highScoresL);
        scrollPane.setPreferredSize(new Dimension(300, 300));

        JFrame frame = new JFrame("High Scores");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(scrollPane);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void showNewGameDialog() {

        JLabel widthLabel = new JLabel("Enter board width (10-100):");
        widthLabel.setFont(new Font("Impact", Font.PLAIN, 20));
        widthLabel.setForeground(Color.YELLOW);
        widthLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JTextField widthField = new JTextField();
        widthField.setPreferredSize(new Dimension(100, 30));
        widthField.setBackground(Color.PINK);
        widthField.setForeground(Color.BLACK);
        widthField.setFont(new Font("Impact", Font.PLAIN, 20));
        widthField.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel heightLabel = new JLabel("Enter board height (10-100):");
        heightLabel.setFont(new Font("Impact", Font.PLAIN, 20));
        heightLabel.setForeground(Color.YELLOW);
        heightLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JTextField heightField = new JTextField();
        heightField.setPreferredSize(new Dimension(100, 30));
        heightField.setBackground(Color.CYAN);
        heightField.setForeground(Color.BLACK);
        heightField.setFont(new Font("Impact", Font.PLAIN, 20));
        heightField.setHorizontalAlignment(SwingConstants.CENTER);

        JButton startButton = new JButton("Start Game!");
        startButton.setFont(new Font("Impact", Font.PLAIN, 20));
        startButton.setBackground(Color.YELLOW);
        startButton.setForeground(Color.BLACK);


        ImageIcon pic1 = new ImageIcon("logo2.jpg");
        Image image1 = pic1.getImage();
        Image scaledImage1 = image1.getScaledInstance(150,50, Image.SCALE_SMOOTH);
        JLabel ghosts1 = new JLabel(new ImageIcon(scaledImage1));
        ghosts1.setHorizontalAlignment(SwingConstants.CENTER);



        JPanel newGamePanel = new JPanel(new GridLayout(3, 2));
        newGamePanel.add(widthLabel);
        newGamePanel.add(widthField);
        newGamePanel.add(heightLabel);
        newGamePanel.add(heightField);
        newGamePanel.add(ghosts1);
        newGamePanel.add(startButton);
        newGamePanel.setSize(new Dimension(550,200));
        newGamePanel.setBackground(Color.BLACK);


        JFrame newGameFrame = new JFrame();
        newGameFrame.add(newGamePanel);
        newGameFrame.pack();
        newGameFrame.setLocationRelativeTo(null);
        newGameFrame.setVisible(true);
        newGameFrame.setBackground(Color.BLACK);
        newGameFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int width, height;
                width = Integer.parseInt(widthField.getText());
                height = Integer.parseInt(heightField.getText());
                    if (width < 10 || width > 100 || height < 10 || height > 100) {
                        JOptionPane.showMessageDialog(null, "Width and height must be between 10 and 100, try again.", "Error", JOptionPane.ERROR_MESSAGE);
                        showNewGameDialog();
                        newGameFrame.dispose();
                        return;
                    }
                    pacman = new GameLoop(width,height);;
                    pacmanT = pacman;
                    pacmanT.start();
                newGameFrame.dispose();
            }
        });
    }
    }

