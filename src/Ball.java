import java.awt.*;

class Ball {
    private double diameter = 24;

    private double x = 384;
    private double y = 400;
    private double xa = 3;
    private double ya = -3;

    private GamePanel gamePanel;
    private Racquet racquet;
//    private Blocks blocks;

    private int number;
    private int thru = 0;
    private int weak = 0;
    private int creator = 0;
    private int explosive = 0;

    Ball(GamePanel gamePanel, Racquet racquet, double speed, int number) {
        this.gamePanel = gamePanel;
        this.racquet = racquet;
        xa = speed/2;
        ya = -speed*Math.sqrt(3)/2;
        this.number = number;
    }

    void move() {
//        boolean changeDirection = true;

        //FRAME collisions
        if (x + xa < 0)
            xa = -xa;
        else if (x + xa > gamePanel.getWidth() - diameter)
            xa = -xa;
        else if (y + ya < 0)
            ya = -ya;
        else if (y + ya > gamePanel.getHeight() - diameter)
            gamePanel.lifeLost(number);

        //racquet collisions
        else if (racquetCollision()){
            y = gamePanel.racquet.getTopY() - diameter;
            double angle = ballAngle() + racquetAngle();
            if (angle > 70)
                angle = 70;
            if (angle < -70)
                angle = -70;
            xa = Math.sin(Math.toRadians(angle))* gamePanel.speed*Math.sqrt(2);
            ya = -Math.cos(Math.toRadians(angle))* gamePanel.speed*Math.sqrt(2);

            gamePanel.speed += 0.02;
        }

        //blocks collisions
        else for (int i = 0; i < gamePanel.blocks.blocksNumber; i++)
            if (blockCollision(i)){
                //up
                if (thru == 0) {
                    if (y + diameter - Math.abs(ya) - 1 <= gamePanel.blocks.getBounds(i).y)
                        ya = -Math.abs(ya);
                    //down
                    else if (y + Math.abs(ya) + 1 >= gamePanel.blocks.getBounds(i).y + gamePanel.blocks.getBounds(i).height)
                        ya = Math.abs(ya);
                    //left
                    else if (x + diameter - Math.abs(xa) - 1 <= gamePanel.blocks.getBounds(i).x)
                        xa = -Math.abs(xa);
                    //right
                    else if (x + Math.abs(xa) + 1 >= gamePanel.blocks.getBounds(i).x + gamePanel.blocks.getBounds(i).width)
                        xa = Math.abs(xa);
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
//            else
//                 changeDirection = false;

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
        //else if ()

        return false;
    }

    private boolean blockCollision(int i) {
        return gamePanel.blocks.getBounds(i).intersects(getBounds());
    }

    void paint(Graphics2D g) {
        g.fillOval((int)x, (int)y, (int)diameter, (int)diameter);
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

    void changeThru(boolean thruAdd) {
        if (thruAdd)
            thru++;
        else
            thru--;
    }

    void changeWeak(boolean weakAdd) {
        if (weakAdd)
            weak++;
        else
            weak--;
    }

    void changeCreator(boolean creatorAdd) {
        if (creatorAdd)
            creator++;
        else
            creator--;
    }

    void changeExplosive(boolean explosiveAdd) {
        if (explosiveAdd)
            explosive++;
        else
            explosive--;
    }
}