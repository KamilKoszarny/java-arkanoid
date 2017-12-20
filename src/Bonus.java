import javax.swing.text.html.HTML;
import java.awt.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.Random;

public class Bonus implements Serializable{

    private static final long serialVersionUID = 1L;

    private static final int DURATION = 1500;
    private static final Color[] COLORS = {Color.GREEN, Color.RED, Color.BLUE, Color.ORANGE, Color.BLACK, Color.WHITE,
            Color.MAGENTA, Color.CYAN, Color.LIGHT_GRAY, Color.PINK, Color.YELLOW};
    private static final String[] TYPES = {"LONG", "SHORT", "SLOW", "FAST", "THRU", "WEAK",
            "SMALL", "BIG", "NEW_BALL", "CREATOR", "EXPLOSIVE"};
    private static final Character[] LETTERS = {'L','S','S','F','T','W',
            'S','B','N','C','X'};
    private static final String[] DESCRIPTIONS = {
            "LONG - makes your bar longer for some time. Position of green letter 'L' on bar shows duration.",
                    "This bonus can be increased with another LONG or neutralized with SHORT.",
            "SHORT - makes your bar shorted for some time. Position of red letter 'S' on bar shows duration.",
                    "This bonus can be increased with another SHORT or neutralized with LONG.",
            "SLOW - slows ball for some time. Size of blue dot on ball shows duration.",
                    "This bonus can be increased with another SLOW or neutralized with FAST.",
            "FAST - speeds up ball for some time. Size of orange dot on ball shows duration.",
                    "This bonus can be increased with another FAST or neutralized with SLOW.",
            "THROUGH - gives ball ability to go break through the blocks destroying them much faster",
                    "Size of black dot on ball shows duration, which is shorter with every round",
            "WEAK - ball cannot destroy the blocks and is just bouncing off them.",
                    "Size of white dot on ball shows duration.",
            "SMALL - ball will be smaller with each second, till it reaches 1/3 of its original size,",
                    "then it will slowly go back to normal size.",
            "BIG - ball will be bigger with each second, till it reaches 2X its original size,",
                    "then it will slowly go back to normal size.",
            "NEW BALL - gives you another ball and life. If you loose one of balls you loose this extra life,",
                    "but if you manage to keep both balls till the end of round you keep extra life",
            "CREATOR - ball will darken blocks instead of destroying them. Dark blocks must be hit twice to destroy,",
                    "but they provide two times more points. Size of pink dot on ball shows duration.",
            "EXPLOSIVE - ball can destroy dark blocks with one hit. Adjoining blocks will be destroyed as well",
                    "Size of yellow dot on ball shows duration."};

    private int bonusTypeNr = -1;
    private double duration = 0;
    private int x, y, diameter;
    private String bonusType;
    private int fallenDistance  = 0;
    private boolean exists, caught;

    private GamePanel gamePanel;

    Bonus(GamePanel gamePanel) {
        this.gamePanel = gamePanel;

        exists = true;
        caught = false;
        Random r = new Random();
        bonusTypeNr = r.nextInt(TYPES.length);
        bonusType = TYPES[bonusTypeNr];
    }

    void paint(Graphics2D g, int x, int y, int width, int height) {
        this.diameter = height*3/4;
        this.x = x + width/2 - diameter/2;
        this.y = y + height/2 - diameter/2 + fallenDistance;

        Color tmpColor = g.getColor();
        g.setFont(new Font("Verdana", Font.BOLD, diameter * 2 / 3));
        FontMetrics fontMetrics = g.getFontMetrics();

//falling
        if (exists && (!TYPES[bonusTypeNr].equals("NEW_BALL") || !gamePanel.isNewBallSend())) {
            bonusPaint(COLORS[bonusTypeNr], LETTERS[bonusTypeNr], g);
        }

//marking active
        if(caught) {
            if (Objects.equals(bonusType, "LONG")) {
                g.setColor(Color.GREEN);
                g.drawString("L", (int) (gamePanel.racquet.getX() + (gamePanel.racquet.getWidth() - fontMetrics.stringWidth("L"))
                        * (1 -  duration / DURATION)), Racquet.Y + Racquet.HEIGHT);
            }
            if (Objects.equals(bonusType, "SHORT")) {
                g.setColor(Color.RED);
                g.drawString("S", (int) (gamePanel.racquet.getX() + (gamePanel.racquet.getWidth() - fontMetrics.stringWidth("S"))
                        * (1 -  duration / DURATION)), Racquet.Y + Racquet.HEIGHT);
            }
            if (Objects.equals(bonusType, "SLOW")) {
                ballPaint(Color.BLUE, g);
            }
            if (Objects.equals(bonusType, "FAST")) {
                ballPaint(Color.ORANGE, g);
            }
            if (Objects.equals(bonusType, "THRU")) {
                ballPaint(Color.BLACK, g);
                duration += 3 + gamePanel.getRoundFinished()/2;
            }
            if (Objects.equals(bonusType, "WEAK")) {
                ballPaint(Color.WHITE, g);
                duration ++;
            }
            if (Objects.equals(bonusType, "CREATOR")) {
                ballPaint(Color.PINK, g);
                duration ++;
            }
            if (Objects.equals(bonusType, "EXPLOSIVE")) {
                ballPaint(Color.YELLOW, g);
                duration += 1 + gamePanel.getRoundFinished()/3;
            }

            if (Objects.equals(bonusType, "SMALL")) {
                if (duration < DURATION/2) {
                    gamePanel.ball.setDiameter((gamePanel.ball.getDiameter() - .01));
                    gamePanel.ball2.setDiameter((gamePanel.ball2.getDiameter() - .01));
                }
                if (duration > DURATION/2) {
                    gamePanel.ball.setDiameter((gamePanel.ball.getDiameter() + .01));
                    gamePanel.ball2.setDiameter((gamePanel.ball2.getDiameter() + .01));
                }
                if (gamePanel.ball.getDiameter() < 3)
                    gamePanel.ball.setDiameter(3);
                if (gamePanel.ball2.getDiameter() < 3)
                    gamePanel.ball2.setDiameter(3);
                duration -= 0.3;
            }
            if (Objects.equals(bonusType, "BIG")) {
                if (duration < DURATION/2) {
                    gamePanel.ball.setDiameter((gamePanel.ball.getDiameter() + .03));
                    gamePanel.ball2.setDiameter((gamePanel.ball2.getDiameter() + .03));
                }
                if (duration > DURATION/2) {
                    gamePanel.ball.setDiameter((gamePanel.ball.getDiameter() - .03));
                    gamePanel.ball2.setDiameter((gamePanel.ball2.getDiameter() - .03));
                }
                duration -= 0.3;
            }
        }

        g.setColor(tmpColor);
    }


    void move(){
        this.fallenDistance += gamePanel.speed;
        double tmpDiameter = gamePanel.ball.getDiameter();

//activity
        if (!caught && gamePanel.racquet.getBounds().intersects(getBounds())){
            if (Objects.equals(bonusType, "LONG")) {
                gamePanel.racquet.setX(gamePanel.racquet.getX() - gamePanel.racquet.getWidth()*.25);
                gamePanel.racquet.setWidth(gamePanel.racquet.getWidth() * 1.5);
                if (gamePanel.racquet.getX() + gamePanel.racquet.getWidth() > 800)
                    gamePanel.racquet.setX(800 - gamePanel.racquet.getWidth() - 1);
                if (gamePanel.racquet.getX() < 0)
                    gamePanel.racquet.setX(0);
            }
            if (Objects.equals(bonusType, "SHORT")) {
                gamePanel.racquet.setWidth(gamePanel.racquet.getWidth() * 2 / 3);
                gamePanel.racquet.setX(gamePanel.racquet.getX() + gamePanel.racquet.getWidth() * .25);
            }
            if (Objects.equals(bonusType, "SLOW")) {
                gamePanel.speed /= 1.3;
            }
            if (Objects.equals(bonusType, "FAST")) {
                gamePanel.speed *= 1.3;
            }
            if (Objects.equals(bonusType, "THRU")) {
                gamePanel.ball.changeThru(true);
                gamePanel.ball2.changeThru(true);
            }
            if (Objects.equals(bonusType, "WEAK")) {
                gamePanel.ball.changeWeak(true);
                gamePanel.ball2.changeWeak(true);
            }
            if (Objects.equals(bonusType, "SMALL")) {
                tmpDiameter = gamePanel.ball.getDiameter();
            }
            if (Objects.equals(bonusType, "BIG")) {
                tmpDiameter = gamePanel.ball.getDiameter();
            }
            if (Objects.equals(bonusType, "NEW_BALL") && !gamePanel.isNewBallSend()) {
                gamePanel.setNewBallSend();
                gamePanel.addBall();
            }
            if (Objects.equals(bonusType, "CREATOR")) {
                gamePanel.ball.changeCreator(true);
                gamePanel.ball2.changeCreator(true);
            }
            if (Objects.equals(bonusType, "EXPLOSIVE")) {
                gamePanel.ball.changeExplosive(true);
                gamePanel.ball2.changeExplosive(true);
            }

            caught = true;
            exists = false;
        }

//finish
        if (caught)
            if(duration < DURATION) {
                bonusActive();
            }
            else {
                if (Objects.equals(bonusType, "LONG")) {
                    gamePanel.racquet.setWidth(gamePanel.racquet.getWidth() * 2 / 3);
                    gamePanel.racquet.setX(gamePanel.racquet.getX() + gamePanel.racquet.getWidth() * .25);
                }
                if (Objects.equals(bonusType, "SHORT")) {
                    gamePanel.racquet.setX(gamePanel.racquet.getX() - gamePanel.racquet.getWidth()*.25);
                    gamePanel.racquet.setWidth(gamePanel.racquet.getWidth() * 1.5);
                    if (gamePanel.racquet.getX() + gamePanel.racquet.getWidth() > 800)
                        gamePanel.racquet.setX(800 - gamePanel.racquet.getWidth() - 1);
                    if (gamePanel.racquet.getX() < 0)
                        gamePanel.racquet.setX(0);
                }
                if (Objects.equals(bonusType, "SLOW")) {
                    gamePanel.speed *= 1.3;
                }
                if (Objects.equals(bonusType, "FAST")) {
                    gamePanel.speed /= 1.3;
                }
                if (Objects.equals(bonusType, "THRU")) {
                    gamePanel.ball.changeThru(false);
                    gamePanel.ball2.changeThru(false);
                }
                if (Objects.equals(bonusType, "WEAK")) {
                    gamePanel.ball.changeWeak(false);
                    gamePanel.ball2.changeWeak(false);
                }
                if (Objects.equals(bonusType, "SMALL")) {
                    gamePanel.ball.setDiameter(tmpDiameter);
                    gamePanel.ball2.setDiameter(tmpDiameter);
                }
                if (Objects.equals(bonusType, "BIG")) {
                    gamePanel.ball.setDiameter(tmpDiameter);
                    gamePanel.ball2.setDiameter(tmpDiameter);
                }
                if (Objects.equals(bonusType, "CREATOR")) {
                    gamePanel.ball.changeCreator(false);
                    gamePanel.ball2.changeCreator(false);
                }
                if (Objects.equals(bonusType, "EXPLOSIVE")) {
                    gamePanel.ball.changeExplosive(false);
                    gamePanel.ball2.changeExplosive(false);
                }
                caught = false;
            }
    }

   private void bonusActive(){
        duration ++;
   }

   private void bonusPaint(Color color, Character letter, Graphics2D g) {
        FontMetrics fontMetrics = g.getFontMetrics();
        g.setColor(color);
        g.fillOval(this.x, this.y, diameter, diameter);
        if (color != Color.BLACK && color != Color.BLUE)
            g.setColor(Color.BLACK);
        else
            g.setColor(Color.WHITE);
        g.drawString(Character.toString(letter), this.x + diameter / 2 - fontMetrics.charWidth(letter) / 2,
                this.y + diameter / 2 + fontMetrics.getHeight() / 2 - 6);
    }

    static void helpBonusPaint(Graphics2D g) {
        int DIAMETER = 40;
        int FONT_SIZE = 25;
        int SPAN = 50;

        g.setFont(new Font("Verdana", Font.BOLD, FONT_SIZE));
        FontMetrics fontMetrics = g.getFontMetrics();
        for (int i = 0; i < TYPES.length; i++) {
            g.setColor(COLORS[i]);
            g.fillOval(50, 10 + i*SPAN, DIAMETER, DIAMETER);
            if (COLORS[i] != Color.BLACK && COLORS[i] != Color.BLUE)
                g.setColor(Color.BLACK);
            else
                g.setColor(Color.WHITE);
            g.drawString(Character.toString(LETTERS[i]), 50 + DIAMETER / 2 - fontMetrics.charWidth(LETTERS[i]) / 2,
                    10 + i*SPAN + DIAMETER / 2 + fontMetrics.getHeight() / 2 - 6);
        }
        g.setFont(new Font("Verdana", Font.BOLD, 12));
        g.setColor(Color.BLACK);
        for (int i = 0; i < DESCRIPTIONS.length; i++) {
            g.drawString(DESCRIPTIONS[i], 100,
                    10 + i/2 * SPAN + i%2 * SPAN/3 + 15);
        }
    }

    private void ballPaint(Color color, Graphics2D g){
        g.setColor(color);
        if (gamePanel.isBall1exists())
            g.fillOval((int)(gamePanel.ball.getX() + gamePanel.ball.getDiameter()/2*(duration/DURATION)),
                    (int)(gamePanel.ball.getY() + gamePanel.ball.getDiameter()/2*(duration/DURATION)),
                    (int)(gamePanel.ball.getDiameter() * (1 - duration/DURATION)),
                    (int)(gamePanel.ball.getDiameter() * (1 - duration/DURATION)));
        if (gamePanel.isBall2exists())
            g.fillOval((int)(gamePanel.ball2.getX() + gamePanel.ball2.getDiameter()/2*(duration/DURATION)),
                    (int)(gamePanel.ball2.getY() + gamePanel.ball2.getDiameter()/2*(duration/DURATION)),
                    (int)(gamePanel.ball2.getDiameter() * (1 - (duration/DURATION))),
                    (int)(gamePanel.ball2.getDiameter() * (1 - duration/DURATION)));
    }


    private Rectangle getBounds() {
        return new Rectangle(x, y, diameter, diameter);
    }

}
