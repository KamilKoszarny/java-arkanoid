import highscores.HighscoreManager;

import javax.swing.*;
import java.awt.*;

public class App {
    private final static int MAX_ROUNDS = 15;
    static final JFrame FRAME = new JFrame("Arkanoid Kamilowy");
    private static MenuPanel menuPanel = new MenuPanel();
    private static boolean openGame = false;
    static HelpPanel helpPanel = new HelpPanel();
//main/////////////////////////////////////////////////////////////////////////////////
    public static void main(String[] args) throws InterruptedException {

//first run settings////////////////////////////////////////////////////////////////////
        setFrameSettings(FRAME);

        HighscoreManager highscoreManager = new HighscoreManager();
        String name;

        int again = JOptionPane.YES_OPTION;
        int timeIteration = 10;

//all runs settings////////////////////////////////////////////////////////////////////
        while (again == JOptionPane.YES_OPTION) {
            again = JOptionPane.NO_OPTION;
            String highscore = highscoreManager.getHighscoreString();
            int score = 0;
            int lives = 3;

//rounds - each round as new game////////////////////////////////////////////////////////
            for (int i = 0; i < MAX_ROUNDS; i++) {
                GamePanel gamePanel = new GamePanel(i + 1, score, highscore, lives);
                gamePanel.blocks.init();
                FRAME.add(gamePanel);
                FRAME.add(menuPanel);
                FRAME.validate();

//main thread////////////////////////////////////////////////////////////////////////////
                while (gamePanel.getRoundFinished() == i && !gamePanel.isGameOver() && !gamePanel.isLifeLoosen()) {
                    Thread.sleep(timeIteration);
                    if(helpPanel.isOpen()) {
                        System.out.println("helpPanel");
                        helpPanel.repaint();
                    } else
                    if(menuPanel.isOpen()) {
                        System.out.println("menuPanel");
                        menuPanel.repaint();
                    } else {
                        if (openGame) {
                            FRAME.add(gamePanel);
                            openGame = false;
                            FRAME.validate();
                        }
                        System.out.println("gamePanel");
                        if (!gamePanel.isPause()) {
                            gamePanel.move();
                        }
                        gamePanel.repaint();
                    }
                }

////restart round if life was lost////////////////////////////////////////////////////////////////////////////
                if (gamePanel.isLifeLoosen()) {
                    i--;
                    lives--;
                    score = gamePanel.getScore();
                    gamePanel.blocks.init();
                    FRAME.add(gamePanel);
                    FRAME.setVisible(true);
                }

//next round if round is finished////////////////////////////////////////////////////////////////////////////
                if (gamePanel.getRoundFinished() == i + 1) {
                    score = gamePanel.getScore();
                    score += gamePanel.getRoundFinished() * 10;
                    lives = gamePanel.getLives();
                }

//gamePanel over////////////////////////////////////////////////////////////////////////////
                if (gamePanel.isGameOver()) {
                    name = JOptionPane.showInputDialog(gamePanel, "Your score is: " + score + " Please state your name: ",
                            "GamePanel Over", JOptionPane.INFORMATION_MESSAGE);
                    highscoreManager.addScore(name, score);
                    again = JOptionPane.showConfirmDialog(gamePanel, "Play again?");
                    i = MAX_ROUNDS;
                }

//gamePanel finished////////////////////////////////////////////////////////////////////////////
                if (i + 1 == MAX_ROUNDS){
                    JOptionPane.showMessageDialog(FRAME, "Congratulations, you have finished the gamePanel!");
                    name = JOptionPane.showInputDialog(gamePanel, "Your score is: " + score + " Please state your name: ",
                            "GamePanel Over", JOptionPane.INFORMATION_MESSAGE);
                    highscoreManager.addScore(name, score);
                    again = JOptionPane.showConfirmDialog(gamePanel, "Play again?");
                }
                gamePanel.setBall2exists(false);

            }

        }
        System.exit(1);
    }



    private static void setFrameSettings(JFrame frame){
        frame.setLayout(new BorderLayout());
        frame.setLocation(300,100);
        frame.setSize(800, 600);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    static void openMenu(boolean open){
        if (open)
            FRAME.add(menuPanel);
        else
            FRAME.remove(menuPanel);
        menuPanel.setOpen(open);
        FRAME.validate();
    }

    static void openHelp(boolean open){
        if (open)
            FRAME.add(helpPanel);
        else
            FRAME.add(menuPanel);
        helpPanel.setOpen(open);
        FRAME.validate();
    }

    static void openGame(boolean open){
        openGame = open;
    }
}

