import highscores.HighscoreManager;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.*;

@SuppressWarnings("serial")
public class Game extends JPanel {
    private final static int MAX_ROUNDS = 15;

    double speed;
    private int score;
    private String highscore;
    private Color backgroundColor, blocksColor;
    private int roundFinished;
    private boolean gameOver = false;
    private int lives;
    private boolean lifeLoosen = false;
    private boolean ball2exists = false;
    private boolean ball1exists = true;
    private boolean newBallSend = false;
    private boolean pause = false;

    Racquet racquet;
    Ball ball;
    Ball ball2;
    Block block;


    public Game(int nr, int score, String highscore, int lifes) {
        for (int i = 0; i < MAX_ROUNDS; i++) {
            if (nr == i + 1) {
                this.score = score;
                this.highscore = highscore;
                this.lives = lifes;
                this.roundFinished = i;
                this.backgroundColor = Color.getHSBColor((float)(0.3+i*0.06), 0.25f, 0.9f);
                this.blocksColor = Color.getHSBColor((float)(0.3+i*0.06), 0.6f, 0.6f);
                this.speed = (double)i*.5 + 2.5;
            }
        }

        this.racquet = new Racquet(this);
        this.ball = new Ball(this, racquet, this.speed, 1);
        this.ball2 = new Ball(this, racquet, this.speed, 2);
        this.block = new Block(this, 5*(nr + 4), nr + 3, nr * 5);

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put
                (KeyStroke.getKeyStroke("LEFT"), "move left");
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put
                (KeyStroke.getKeyStroke("RIGHT"), "move right");
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put
                (KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, true), "stop");
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put
                (KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, true), "stop");
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put
                (KeyStroke.getKeyStroke("P"), "pause");

        this.getActionMap().put("move left", new MoveAction("left"));
        this.getActionMap().put("move right", new MoveAction("right"));
        this.getActionMap().put("stop", new MoveAction("stop"));
        this.getActionMap().put("pause", new MoveAction("pause"));
    }

    private void move() {
        if (ball1exists)
            ball.move();
        if (ball2exists)
            ball2.move();
        racquet.move();
        block.move();
        if (!block.getExists()) {
            roundFinished += 1;
            repaint();
        }
    }

    private class MoveAction extends AbstractAction {

        String direction;

        MoveAction(String direction){
            this.direction = direction;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            racquet.directionSet(direction);
            if (direction == "pause")
                if (!pause)
                    pause = true;
                else if (pause)
                    pause = false;
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        //ball, racquet
        g2d.setColor(Color.getHSBColor(0f, 0f, 0.3f));
        if (ball1exists)
            ball.paint(g2d);
        if (ball2exists)
            ball2.paint(g2d);
        g2d.setColor(Color.BLACK);
        racquet.paint(g2d);

        //round
        this.setBackground(backgroundColor);
        g2d.setColor(blocksColor);
        block.paint(g2d);

        //strings
        g2d.setColor(Color.GRAY);
        g2d.setFont(new Font("Verdana", Font.BOLD, 20));
        FontMetrics fontMetrics = g2d.getFontMetrics();
        g2d.drawString(String.valueOf("Score: " + score), 20, 550);
        g2d.drawString(String.valueOf("Round: " + (roundFinished + 1)), 20 + fontMetrics.stringWidth
                ("Score: " + score) + 20, 550);
        g2d.drawString(String.valueOf("Lifes: " + (lives)), 20 + fontMetrics.stringWidth
                ("Score: " + score + "Round: " + (roundFinished + 1)) + 40, 550);
        g2d.drawString("High Score: "+ highscore, 800 - fontMetrics.stringWidth
                ("High Score: "+ highscore) - 20, 550);

        //pause
        if (pause) {
            g2d.setFont(new Font(g2d.getFont().getName(), g2d.getFont().getStyle(), g2d.getFont().getSize() + 20));
            g2d.drawString("PAUSE", 325, 300);
        }
    }


//main/////////////////////////////////////////////////////////////////////
    public static void main(String[] args) throws InterruptedException {

        JFrame frame = new JFrame("Arkanoid Kamilowy");
        frame.setLocation(300,100);
        frame.setSize(800, 600);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        HighscoreManager highscoreManager = new HighscoreManager();
        String name;

        int again = JOptionPane.YES_OPTION;
        int timeIteration = 10;


        while (again == JOptionPane.YES_OPTION) {
            again = JOptionPane.NO_OPTION;
            String highscore = highscoreManager.getHighscoreString();
            int score = 0;
            int lifes = 3;

            for (int i = 0; i < MAX_ROUNDS; i++) {
                Game game = new Game(i + 1, score, highscore, lifes);
                game.block.init();
                frame.add(game);
                frame.setVisible(true);


                while (game.getRoundFinished() == i && !game.gameOver) {
                    Thread.sleep(timeIteration);
                    game.move();
                    game.repaint();
                    while(game.pause)
                        Thread.sleep(timeIteration);

                    if (game.lifeLoosen)
                        break;
                }

                if (game.lifeLoosen) {
                    i--;
                    lifes--;
                    score = game.getScore();
                    game.block.init();
                    frame.add(game);
                    frame.setVisible(true);
                } else {
                    score = game.getScore();
                    score += game.getRoundFinished() * 10;
                    lifes = game.getLives();
                }

                if (game.gameOver) {
                    name = JOptionPane.showInputDialog(game, "Your score is: " + score + " Please state your name: ",
                            "Game Over", JOptionPane.OK_OPTION);
                    highscoreManager.addScore(name, score);
                    again = JOptionPane.showConfirmDialog(game, "Play again?");
                    i = MAX_ROUNDS;
                }

                if (i + 1 == MAX_ROUNDS){
                    JOptionPane.showMessageDialog(frame, "Congratulations, you have finished the game!");
                    name = JOptionPane.showInputDialog(game, "Your score is: " + score + " Please state your name: ",
                            "Game Over", JOptionPane.OK_OPTION);
                    highscoreManager.addScore(name, score);
                    again = JOptionPane.showConfirmDialog(game, "Play again?");
                }
                game.ball2exists = false;
            }
        }
        System.exit(ABORT);
    }

    public void addBall(){
        lives++;
        if (ball1exists) {
            ball2exists = true;
            ball2.setX(384);
            ball2.setY(400);
            ball2.setXa(1);
            ball2.setYa(-2);
            //ball2caught = true;
        }
        else if (ball2exists) {
            ball1exists = true;
            ball.setX(384);
            ball.setY(400);
            ball.setXa(1);
            ball.setYa(-2);
            //ball1caught = true;
        }

    }

    public void lifeLost(int number) {
        if (number == 2) {
            lives--;
            ball2exists = false;
            if (!ball1exists) {
                // lives ++;
                lifeLoosen = true;
            }
        }
        if (number == 1) {
            lives--;
            ball1exists = false;
            if (!ball2exists) {
                //  lives ++;
                lifeLoosen = true;
            }
        }

        if (lives == -1)
            gameOver = true;
    }


////get/set////////////

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getLives() {
        return lives;
    }

    public int getRoundFinished() {
        return roundFinished;
    }

    public boolean isLifeLoosen() {
        return lifeLoosen;
    }

    public void setLifeLoosen(boolean lifeLoosen) {
        this.lifeLoosen = lifeLoosen;
    }

    public boolean isNewBallSend() {
        return newBallSend;
    }

    public void setNewBallSend(boolean newBallSend) {
        this.newBallSend = newBallSend;
    }

    public boolean isBall1exists() {
        return ball1exists;
    }

    public boolean isBall2exists() {
        return ball2exists;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }
}