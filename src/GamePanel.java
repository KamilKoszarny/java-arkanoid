import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Objects;
import javax.swing.*;


public class GamePanel extends JPanel {

    double speed;
    private int score;
    private String highscore;
    private Color backgroundColor, blocksColor;
    private int roundFinished;
    private boolean gameOver = false;
    private int lives;
    private boolean lifeLoosen = false;
    private boolean ball1exists = true;
    private boolean ball2exists = false;
    private boolean newBallSend = false;
    private boolean pause = true;
    private boolean firstPause = true;

    Racquet racquet;
    Ball ball;
    Ball ball2;
    Blocks blocks;


    public GamePanel(int nr, int score, String highscore, int lives) {
        this.score = score;
        this.highscore = highscore;
        this.lives = lives;
        this.roundFinished = nr - 1;
        this.backgroundColor = Color.getHSBColor((float)(0.3+(nr - 1)*0.06), 0.25f, 0.9f);
        this.blocksColor = Color.getHSBColor((float)(0.3+(nr - 1)*0.06), 0.6f, 0.6f);
        this.speed = (double)(nr - 1)*.35 + 2.5;

        this.racquet = new Racquet(this);
        this.ball = new Ball(this, racquet, this.speed, 1);
        this.ball2 = new Ball(this, racquet, this.speed, 2);
        this.blocks = new Blocks(this, 5*(nr + 4), nr + 3, nr * 5);

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put
                (KeyStroke.getKeyStroke("LEFT"), "move left");
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put
                (KeyStroke.getKeyStroke("RIGHT"), "move right");
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put
                (KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, true), "stop");
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put
                (KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, true), "stop");
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put
                (KeyStroke.getKeyStroke(KeyEvent.VK_P, 0, false), "pause");
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put
                (KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false), "menuPanel");

        this.getActionMap().put("move left", new MoveAction("left"));
        this.getActionMap().put("move right", new MoveAction("right"));
        this.getActionMap().put("stop", new MoveAction("stop"));
        this.getActionMap().put("pause", new MoveAction("pause"));
        this.getActionMap().put("menuPanel", new MoveAction("menuPanel"));
    }

    void move() {
        if (ball1exists)
            ball.move();
        if (ball2exists)
            ball2.move();
        racquet.move();
        blocks.move();
        if (!blocks.getExists()) {
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
            if (Objects.equals(direction, "pause")) {
                pause = !pause;
                firstPause = false;
            }
            else if (Objects.equals(direction, "menuPanel")) {
                App.openMenu(true);
            } else
                racquet.directionSet(direction);
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

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
        blocks.paint(g2d);

        //strings
        g2d.setColor(Color.GRAY);
        g2d.setFont(new Font("Verdana", Font.BOLD, 20));
        FontMetrics fontMetrics = g2d.getFontMetrics();
        g2d.drawString(String.valueOf("Score: " + score), 20, 550);
        g2d.drawString(String.valueOf("Round: " + (roundFinished + 1)), 20 + fontMetrics.stringWidth
                ("Score: " + score) + 20, 550);
        g2d.drawString(String.valueOf("Lifes: " + (lives)), 20 + fontMetrics.stringWidth
                ("Score: " + score + "Round: " + (roundFinished + 1)) + 40, 550);
        g2d.drawString("High Score: " + highscore, 800 - fontMetrics.stringWidth
                ("High Score: " + highscore) - 20, 550);

            //pause
        if (pause) {
            System.out.println("pause");
            g2d.setFont(new Font(g2d.getFont().getName(), g2d.getFont().getStyle(), g2d.getFont().getSize() + 20));
            if (firstPause)
                g2d.drawString("Press P to start", App.FRAME.getWidth() / 2 - fontMetrics.stringWidth("Press P to start"), 300);
            else
                g2d.drawString("PAUSE", 325, 300);
        }
    }




    void addBall(){
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

    void lifeLost(int number) {
        if (number == 2) {
            lives--;
            ball2exists = false;
            if (!ball1exists) {
                lifeLoosen = true;
            }
        }
        if (number == 1) {
            lives--;
            ball1exists = false;
            if (!ball2exists) {
                lifeLoosen = true;
            }
        }

        if (lives == -1)
            gameOver = true;
    }


////get/set////////////

    int getScore() {
        return score;
    }

    void setScore(int score) {
        this.score = score;
    }

    int getLives() {
        return lives;
    }

    int getRoundFinished() {
        return roundFinished;
    }

    boolean isNewBallSend() {
        return newBallSend;
    }

    void setNewBallSend() {
        this.newBallSend = true;
    }

    boolean isBall1exists() {
        return ball1exists;
    }

    public void setBall1exists(boolean ball1exists) {
        this.ball1exists = ball1exists;
    }

    boolean isBall2exists() {
        return ball2exists;
    }

    void setBall2exists(boolean ball2exists) {
        this.ball2exists = ball2exists;
    }

    boolean isGameOver() {
        return gameOver;
    }

    boolean isPause() {
        return pause;
    }

    boolean isLifeLoosen() {
        return lifeLoosen;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}