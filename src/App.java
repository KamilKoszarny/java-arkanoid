import highscores.HighscoreManager;

import javax.swing.*;
import java.awt.*;

public class App {
    private final static int MAX_ROUNDS = 15;
    static final JFrame FRAME = new JFrame("Arkanoid Kamilowy");
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
                Game game = new Game(i + 1, score, highscore, lives);
                game.blocks.init();
                FRAME.add(game);
                FRAME.validate();

//main thread////////////////////////////////////////////////////////////////////////////
                while (game.getRoundFinished() == i && !game.isGameOver() && !game.isLifeLoosen()) {
                    Thread.sleep(timeIteration);
                    game.move();
                    game.repaint();
                    while(game.isPause() || game.menu.isOpen())
                        Thread.sleep(timeIteration);
                }

////restart round if life was lost////////////////////////////////////////////////////////////////////////////
                if (game.isLifeLoosen()) {
                    i--;
                    lives--;
                    score = game.getScore();
                    game.blocks.init();
                    FRAME.add(game);
                    FRAME.setVisible(true);
                }

//next round if round is finished////////////////////////////////////////////////////////////////////////////
                if (game.getRoundFinished() == i + 1) {
                    score = game.getScore();
                    score += game.getRoundFinished() * 10;
                    lives = game.getLives();
                }

//game over////////////////////////////////////////////////////////////////////////////
                if (game.isGameOver()) {
                    name = JOptionPane.showInputDialog(game, "Your score is: " + score + " Please state your name: ",
                            "Game Over", JOptionPane.INFORMATION_MESSAGE);
                    highscoreManager.addScore(name, score);
                    again = JOptionPane.showConfirmDialog(game, "Play again?");
                    i = MAX_ROUNDS;
                }

//game finished////////////////////////////////////////////////////////////////////////////
                if (i + 1 == MAX_ROUNDS){
                    JOptionPane.showMessageDialog(FRAME, "Congratulations, you have finished the game!");
                    name = JOptionPane.showInputDialog(game, "Your score is: " + score + " Please state your name: ",
                            "Game Over", JOptionPane.INFORMATION_MESSAGE);
                    highscoreManager.addScore(name, score);
                    again = JOptionPane.showConfirmDialog(game, "Play again?");
                }
                game.setBall2exists(false);

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

}

