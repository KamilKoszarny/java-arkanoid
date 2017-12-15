import java.awt.*;
import java.util.Random;

public class Bonus{

private static final int LONG = 1;
private static final int SHORT = 2;
private static final int SLOW = 3;
private static final int FAST = 4;
private static final int THRU = 5;
private static final int WEAK = 6;
private static final int SMALL = 7;
private static final int BIG = 8;
private static final int NEW_BALL = 9;
private static final int CREATOR = 10;
private static final int EXPLOSIVE = 11;

private static final int DURATION = 1500;

private double duration = 0;
private int x, y, diameter;
private int bonusType;
private int fallenDistance  = 0;
private boolean exists, caught;

private Game game;

    Bonus(Game game) {
        this.game = game;

        exists = true;
        caught = false;
        Random r = new Random();
        bonusType = r.nextInt(11) + 1;
    }

    void paint(Graphics2D g, int x, int y, int width, int height) {
        this.diameter = height*3/4;
        this.x = x + width/2 - diameter/2;
        this.y = y + height/2 - diameter/2 + fallenDistance;

        Color tmpColor = g.getColor();
        g.setFont(new Font("Verdana", Font.BOLD, diameter * 2 / 3));
        FontMetrics fontMetrics = g.getFontMetrics();

        if (exists) {

            if (bonusType == LONG) {
                bonusPaint(Color.GREEN, "L", g);
            }
            if (bonusType == SHORT) {
                bonusPaint(Color.RED, "S", g);
            }
            if (bonusType == SLOW) {
                bonusPaint(Color.BLUE, "S", g);
            }
            if (bonusType == FAST) {
                bonusPaint(Color.ORANGE, "F", g);
            }
            if (bonusType == THRU) {
                bonusPaint(Color.BLACK, "T", g);
            }
            if (bonusType == WEAK) {
                bonusPaint(Color.WHITE, "W", g);
            }
            if (bonusType == SMALL) {
                bonusPaint(Color.MAGENTA, "S", g);
            }
            if (bonusType == BIG) {
                bonusPaint(Color.CYAN, "B", g);
            }
            if (bonusType == NEW_BALL && !game.isNewBallSend()) {
                bonusPaint(Color.LIGHT_GRAY, "N", g);
            }
            if (bonusType == CREATOR) {
                bonusPaint(Color.PINK, "C", g);
            }
            if (bonusType == EXPLOSIVE) {
                bonusPaint(Color.YELLOW, "X", g);
            }
        }

        if(caught) {
            if (bonusType == LONG) {
                g.setColor(Color.GREEN);
                g.drawString("L", (int) (game.racquet.getX() + (game.racquet.getWidth() - fontMetrics.stringWidth("L"))
                        * (1 -  duration / DURATION)), Racquet.Y + Racquet.HEIGHT);
            }
            if (bonusType == SHORT) {
                g.setColor(Color.RED);
                g.drawString("S", (int) (game.racquet.getX() + (game.racquet.getWidth() - fontMetrics.stringWidth("S"))
                        * (1 -  duration / DURATION)), Racquet.Y + Racquet.HEIGHT);
            }
            if (bonusType == SLOW) {
                ballPaint(Color.BLUE, g);
            }
            if (bonusType == FAST) {
                ballPaint(Color.ORANGE, g);
            }
            if (bonusType == THRU) {
                ballPaint(Color.BLACK, g);
                duration += 3 + game.getRoundFinished()/2;
            }
            if (bonusType == WEAK) {
                ballPaint(Color.WHITE, g);
                duration ++;
            }
            if (bonusType == CREATOR) {
                ballPaint(Color.PINK, g);
                duration ++;
            }
            if (bonusType == EXPLOSIVE) {
                ballPaint(Color.YELLOW, g);
                duration += 1 + game.getRoundFinished()/3;
            }

            if (bonusType == SMALL) {
                if (duration < DURATION/2) {
                    game.ball.setDiameter((game.ball.getDiameter() - .01));
                    game.ball2.setDiameter((game.ball2.getDiameter() - .01));
                }
                if (duration > DURATION/2) {
                    game.ball.setDiameter((game.ball.getDiameter() + .01));
                    game.ball2.setDiameter((game.ball2.getDiameter() + .01));
                }
                if (game.ball.getDiameter() < 3)
                    game.ball.setDiameter(3);
                if (game.ball2.getDiameter() < 3)
                    game.ball2.setDiameter(3);
                duration -= 0.3;
            }
            if (bonusType == BIG) {
                if (duration < DURATION/2) {
                    game.ball.setDiameter((game.ball.getDiameter() + .03));
                    game.ball2.setDiameter((game.ball2.getDiameter() + .03));
                }
                if (duration > DURATION/2) {
                    game.ball.setDiameter((game.ball.getDiameter() - .03));
                    game.ball2.setDiameter((game.ball2.getDiameter() - .03));
                }
                duration -= 0.3;
            }
        }

        g.setColor(tmpColor);
    }

    void move(){
        this.fallenDistance += game.speed;
        double tmpDiameter = game.ball.getDiameter();
        //start
        if (!caught && game.racquet.getBounds().intersects(getBounds())){
            if (bonusType == LONG) {
                game.racquet.setX(game.racquet.getX() - game.racquet.getWidth()*.25);
                game.racquet.setWidth(game.racquet.getWidth() * 1.5);
                if (game.racquet.getX() + game.racquet.getWidth() > 800)
                    game.racquet.setX(800 - game.racquet.getWidth() - 1);
                if (game.racquet.getX() < 0)
                    game.racquet.setX(0);
            }
            if (bonusType == SHORT) {
                game.racquet.setWidth(game.racquet.getWidth() * 2 / 3);
                game.racquet.setX(game.racquet.getX() + game.racquet.getWidth() * .25);
            }
            if (bonusType == SLOW) {
                game.speed /= 1.3;
            }
            if (bonusType == FAST) {
                game.speed *= 1.3;
            }
            if (bonusType == THRU) {
                game.ball.changeThru(true);
                game.ball2.changeThru(true);
            }
            if (bonusType == WEAK) {
                game.ball.changeWeak(true);
                game.ball2.changeWeak(true);
            }
            if (bonusType == SMALL) {
                tmpDiameter = game.ball.getDiameter();
            }
            if (bonusType == BIG) {
                tmpDiameter = game.ball.getDiameter();
            }
            if (bonusType == NEW_BALL && !game.isNewBallSend()) {
                game.setNewBallSend(true);
                game.addBall();
            }
            if (bonusType == CREATOR) {
                game.ball.changeCreator(true);
                game.ball2.changeCreator(true);
            }
            if (bonusType == EXPLOSIVE) {
                game.ball.changeExplosive(true);
                game.ball2.changeExplosive(true);
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
                if (bonusType == LONG) {
                    game.racquet.setWidth(game.racquet.getWidth() * 2 / 3);
                    game.racquet.setX(game.racquet.getX() + game.racquet.getWidth() * .25);
                }
                if (bonusType == SHORT) {
                    game.racquet.setX(game.racquet.getX() - game.racquet.getWidth()*.25);
                    game.racquet.setWidth(game.racquet.getWidth() * 1.5);
                    if (game.racquet.getX() + game.racquet.getWidth() > 800)
                        game.racquet.setX(800 - game.racquet.getWidth() - 1);
                    if (game.racquet.getX() < 0)
                        game.racquet.setX(0);
                }
                if (bonusType == SLOW) {
                    game.speed *= 1.3;
                }
                if (bonusType == FAST) {
                    game.speed /= 1.3;
                }
                if (bonusType == THRU) {
                    game.ball.changeThru(false);
                    game.ball2.changeThru(false);
                }
                if (bonusType == WEAK) {
                    game.ball.changeWeak(false);
                    game.ball2.changeWeak(false);
                }
                if (bonusType == SMALL) {
                    game.ball.setDiameter(tmpDiameter);
                    game.ball2.setDiameter(tmpDiameter);
                }
                if (bonusType == BIG) {
                    game.ball.setDiameter(tmpDiameter);
                    game.ball2.setDiameter(tmpDiameter);
                }
                if (bonusType == CREATOR) {
                    game.ball.changeCreator(false);
                    game.ball2.changeCreator(false);
                }
                if (bonusType == EXPLOSIVE) {
                    game.ball.changeExplosive(false);
                    game.ball2.changeExplosive(false);
                }
                caught = false;
            }
    }

   private void bonusActive(){
        duration ++;
   }

   private void bonusPaint(Color color, String letter, Graphics2D g) {
        FontMetrics fontMetrics = g.getFontMetrics();
        g.setColor(color);
        g.fillOval(this.x, this.y, diameter, diameter);
        if (color != Color.BLACK && color != Color.BLUE)
            g.setColor(Color.BLACK);
        else
            g.setColor(Color.WHITE);
        g.drawString(letter, this.x + diameter / 2 - fontMetrics.stringWidth(letter) / 2,
                this.y + diameter / 2 + fontMetrics.getHeight() / 2 - 6);
    }

    private void ballPaint(Color color, Graphics2D g){
        g.setColor(color);
        if (game.isBall1exists())
            g.fillOval((int)(game.ball.getX() + game.ball.getDiameter()/2*(duration/DURATION)),
                    (int)(game.ball.getY() + game.ball.getDiameter()/2*(duration/DURATION)),
                    (int)(game.ball.getDiameter() * (1 - duration/DURATION)),
                    (int)(game.ball.getDiameter() * (1 - duration/DURATION)));
        if (game.isBall2exists())
            g.fillOval((int)(game.ball2.getX() + game.ball2.getDiameter()/2*(duration/DURATION)),
                    (int)(game.ball2.getY() + game.ball2.getDiameter()/2*(duration/DURATION)),
                    (int)(game.ball2.getDiameter() * (1 - (duration/DURATION))),
                    (int)(game.ball2.getDiameter() * (1 - duration/DURATION)));
    }


    private Rectangle getBounds() {
        return new Rectangle(x, y, diameter, diameter);
    }

}
