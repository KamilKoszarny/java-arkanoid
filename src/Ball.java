import java.awt.*;
import java.io.Serializable;
import java.text.DecimalFormat;

class Ball implements Serializable {

    private static final long serialVersionUID = 1L;

    private double diameter = 24;
    private double x = 384;
    private double y = 400;
    private double xa;
    private double ya;
    private double speed;
    private double angle;

    private GamePanel gamePanel;
    private Racquet racquet;
//    private Blocks blocks;

    private int number;
    private int thru = 0;
    private int weak = 0;
    private int creator = 0;
    private int explosive = 0;
    private int rotAngle = 0;
    private int rotation = 1; //-1000:1000

    Ball(GamePanel gamePanel, Racquet racquet, double speed, int number) {
        this.gamePanel = gamePanel;
        this.racquet = racquet;
        xa = speed/2;
        ya = -speed*Math.sqrt(3)/2;
        this.number = number;
    }

    void move() {
        speed = Math.sqrt(xa*xa + ya*ya);
//rotation influence
        rotate();

//FRAME collisions
        if(frameCollide()){}

//racquet collisions
        else if (racquetCollision()){
            racquetCollide();
        }

//blocks collisions
        else for (int i = 0; i < gamePanel.blocks.blocksNumber; i++)
            if (blockCollision(i)){
                blockCollide(i);
            }

//position update
        x += xa;
        y += ya;
    }

    private boolean racquetCollision() {

        if (diameter /2 > Point.distance(x + diameter /2, y + diameter /2,
                racquet.getX(), Racquet.getY()))
            //System.out.println(Point.distance(x, y, racquet.getX(), racquet.getY()));
            return true;
        else if (diameter /2 > Point.distance(x + diameter /2, y + diameter /2,
                racquet.getX() + racquet.getWidth(), Racquet.getY()))
            return true;
        else if (Racquet.getY() - y - diameter <  2
                && x + diameter /2 > racquet.getX() && x + diameter /2 < racquet.getX() + racquet.getWidth())
            return true;

        return false;
    }

    private boolean blockCollision(int i) {
        return gamePanel.blocks.getBounds(i).intersects(getBounds());
    }

    void paint(Graphics2D g) {
        g.setColor(Color.getHSBColor(0f, 0f, 0.3f));
        g.fillArc((int)x, (int)y, (int)diameter, (int)diameter, rotAngle, 90);
        g.fillArc((int)x, (int)y, (int)diameter, (int)diameter, rotAngle + 180, 90);
        g.setColor(Color.getHSBColor(0f, 0f, 0.2f));
        g.fillArc((int)x, (int)y, (int)diameter, (int)diameter, rotAngle + 90, 90);
        g.fillArc((int)x, (int)y, (int)diameter, (int)diameter, rotAngle + 270, 90);
    }

    private Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, (int)diameter, (int)diameter);
    }

    private double ballAngle(){
        return Math.toDegrees(Math.atan(xa/ya));
    }

    private double racquetAngle(){
        return (x + diameter /2 - racquet.getX())/(racquet.getWidth())*90 - 45;
    }

    private boolean frameCollide(){
        boolean collision = false;
        if (x + xa < 0) {
            xa = -xa;
            collision = true;
        }else if (x + xa > gamePanel.getWidth() - diameter) {
            xa = -xa;
            collision = true;
        }
        else if (y + ya < 0) {
            ya = -ya;
            collision = true;
        }
        else if (y + ya > gamePanel.getHeight() - diameter) {
            gamePanel.lifeLost(number);
        }
        return collision;
    }

    private void racquetCollide(){
        y = gamePanel.racquet.getTopY() - diameter;
        double rHitAngle = ballAngle() + racquetAngle();
        if (rHitAngle > 70)
            rHitAngle = 70;
        if (rHitAngle < -70)
            rHitAngle = -70;
        xa = Math.sin(Math.toRadians(rHitAngle))* gamePanel.speed*Math.sqrt(2);
        ya = -Math.cos(Math.toRadians(rHitAngle))* gamePanel.speed*Math.sqrt(2);
        System.out.print(xa + "\t" + ya);

        gamePanel.speed += 0.02;
    }

    private void blockCollide(int i){

        if (thru == 0) {
            angle = 180 - angle + rotation/50.;
//up
            if (y + diameter - Math.abs(ya) - 1 <= gamePanel.blocks.getBounds(i).y) {
                rotation -= (int)(xa*100 + rotation/10.);
    //0-90deg
                if (xa >=0) {
                    xa = Math.sin(Math.toRadians(angle)) * speed;
                    ya = -Math.cos(Math.toRadians(angle)) * speed;
    //270-360deg
                } else /*if (xa < 0) */{
                    xa = -Math.cos(Math.toRadians(angle - 270)) * speed;
                    ya = -Math.sin(Math.toRadians(angle - 270)) * speed;
                }
            }
//down
            else if (y + Math.abs(ya) + 1 >= gamePanel.blocks.getBounds(i).y + gamePanel.blocks.getBounds(i).height) {
                rotation -= (int)(xa*100 + rotation/10.);
    //0-90deg
                if (xa >=0 ) {
                    xa = Math.sin(Math.toRadians(angle)) * speed;
                    ya = -Math.cos(Math.toRadians(angle)) * speed;
    //270-360deg
                } else /*if (xa < 0) */{
                    xa = -Math.cos(Math.toRadians(angle - 270)) * speed;
                    ya = -Math.sin(Math.toRadians(angle - 270)) * speed;
                }
            }
//left
            else if (x + diameter - Math.abs(xa) - 1 <= gamePanel.blocks.getBounds(i).x) {
                rotation -= (int)(ya*100 + rotation/10.);
    //90-180deg
                if (ya >= 0) {
                    xa = Math.cos(Math.toRadians(angle - 90)) * speed;
                    ya = Math.sin(Math.toRadians(angle - 90)) * speed;
    //180-270deg
                } else /*if (ya < 0)*/ {
                    xa = -Math.sin(Math.toRadians(angle - 180)) * speed;
                    ya = Math.cos(Math.toRadians(angle - 180)) * speed;
                }
            }
//right
            else if (x + Math.abs(xa) + 1 >= gamePanel.blocks.getBounds(i).x + gamePanel.blocks.getBounds(i).width) {
                rotation -= (int)(ya*100 + rotation/10.);
                //90-180deg
                if (ya >= 0) {
                    xa = Math.cos(Math.toRadians(angle - 90)) * speed;
                    ya = Math.sin(Math.toRadians(angle - 90)) * speed;
                    //180-270deg
                } else /*if (ya < 0)*/ {
                    xa = -Math.sin(Math.toRadians(angle - 180)) * speed;
                    ya = Math.cos(Math.toRadians(angle - 180)) * speed;
                }
            }
        }

        if (weak == 0 && creator == 0)
            gamePanel.blocks.destroy(i);

        if (creator != 0)
            gamePanel.blocks.setTought(i);

        if (explosive != 0){
            gamePanel.blocks.destroy(i);
            if (i% gamePanel.blocks.getBlocksInRow() != 0)
                gamePanel.blocks.destroy(i - 1);
            if (i >= gamePanel.blocks.getBlocksInRow())
                gamePanel.blocks.destroy(i - gamePanel.blocks.getBlocksInRow());
            if (i% gamePanel.blocks.getBlocksInRow() != gamePanel.blocks.getBlocksInRow() - 1)
                gamePanel.blocks.destroy(i + 1);
            if (i < gamePanel.blocks.getBlocksInRow() * 4)
                gamePanel.blocks.destroy(i + gamePanel.blocks.getBlocksInRow());
        }
    }

    private void rotate(){
        angle = Math.toDegrees(Math.atan(xa/ya));
        if(ya >= 0)
            angle = 180 - angle;
        else if (xa >= 0)
            angle = -angle;
        else if (xa < 0)
            angle = 360 - angle;

        System.out.print("speed: " + new DecimalFormat(".##").format(speed));
        angle += rotation/5000.*speed;
        System.out.print("\tangle: " + new DecimalFormat(".#").format(angle));
        System.out.println("\trot: " + rotation);
//0-90deg
        if (xa >=0 && ya < 0) {
            xa = Math.sin(Math.toRadians(angle)) * speed;
            ya = -Math.cos(Math.toRadians(angle)) * speed;
//90-180deg
        } else if (xa >= 0 && ya >= 0) {
            xa = Math.cos(Math.toRadians(angle - 90)) * speed;
            ya = Math.sin(Math.toRadians(angle - 90)) * speed;
//180-270deg
        } else if (xa < 0 && ya >= 0) {
            xa = -Math.sin(Math.toRadians(angle - 180)) * speed;
            ya = Math.cos(Math.toRadians(angle - 180)) * speed;
//270-360deg
        } else /*if (xa < 0 && ya < 0) */{
            xa = -Math.cos(Math.toRadians(angle - 270)) * speed;
            ya = -Math.sin(Math.toRadians(angle - 270)) * speed;
        }
//rotation paint
        rotAngle -= rotation/50.;
        rotAngle = rotAngle%360;
//rotation loose
        if (rotation > 0)
            rotation -= 1;
        if (rotation < 0)
            rotation += 1;
    }

    ///get/set////////////////////////////////////////////////////////////////////////
    double getDiameter() {
        return diameter;
    }

    void setDiameter(double diameter) {
        this.diameter = diameter;
    }

    double getX() {
        return x;
    }

    double getY() {
        return y;
    }

    void setX(double x) {
        this.x = x;
    }

    void setY(double y) {
        this.y = y;
    }

    void setXa(double xa) {
        this.xa = xa;
    }

    void setYa(double ya) {
        this.ya = ya;
    }

    void addThru(boolean thruAdd) {
        if (thruAdd)
            thru++;
        else
            thru--;
    }

    void addWeak(boolean weakAdd) {
        if (weakAdd)
            weak++;
        else
            weak--;
    }

    void addCreator(boolean creatorAdd) {
        if (creatorAdd)
            creator++;
        else
            creator--;
    }

    void addExplosive(boolean explosiveAdd) {
        if (explosiveAdd)
            explosive++;
        else
            explosive--;
    }
}