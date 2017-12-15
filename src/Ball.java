import java.awt.*;

public class Ball {
    private double diameter = 24;

    private double x = 384;
    private double y = 400;
    private double xa = 3;
    private double ya = -3;

    private Game game;
    private Racquet racquet;
    private Block block;
    private int number;
    private double angle;
    private int thru = 0;
    private int weak = 0;
    private int creator = 0;
    private int explosive = 0;

    public Ball(Game game, Racquet racquet, double speed, int number) {
        this.game = game;
        this.racquet = racquet;
        xa = speed/2;
        ya = -speed*Math.sqrt(3)/2;
        this.number = number;
    }

    public void move() {
        boolean changeDirection = true;

        //frame collisions
        if (x + xa < 0)
            xa = -xa;
        else if (x + xa > game.getWidth() - diameter)
            xa = -xa;
        else if (y + ya < 0)
            ya = -ya;
        else if (y + ya > game.getHeight() - diameter)
            game.lifeLost(number);

        //racquet collisions
        else if (racquetCollision()){
            y = game.racquet.getTopY() - diameter;
            angle = ballAngle() + racquetAngle();
            if (angle > 70)
                angle = 70;
            if (angle < -70)
                angle = -70;
            xa = Math.sin(Math.toRadians(angle))*game.speed*Math.sqrt(2);
            ya = -Math.cos(Math.toRadians(angle))*game.speed*Math.sqrt(2);

            game.speed += 0.01;
        }

        //block collisions
        else for (int i = 0; i < game.block.blocksNumber; i++)
            if (blockCollision(i)){
                //up
                if (thru == 0) {
                    if (y + diameter - Math.abs(ya) - 1 <= game.block.getBounds(i).y)
                        ya = -Math.abs(ya);
                    //down
                    else if (y + Math.abs(ya) + 1 >= game.block.getBounds(i).y + game.block.getBounds(i).height)
                        ya = Math.abs(ya);
                    //left
                    else if (x + diameter - Math.abs(xa) - 1 <= game.block.getBounds(i).x)
                        xa = -Math.abs(xa);
                    //right
                    else if (x + Math.abs(xa) + 1 >= game.block.getBounds(i).x + game.block.getBounds(i).width)
                        xa = Math.abs(xa);
                }

                if (weak == 0 && creator == 0)
                    game.block.destroy(i);

                if (creator != 0)
                    game.block.setTought(i);

                if (explosive != 0){
                    game.block.destroy(i);
                    if (i%game.block.getBlocksInRow() != 0)
                        game.block.destroy(i - 1);
                    if (i >= game.block.getBlocksInRow())
                        game.block.destroy(i - game.block.getBlocksInRow());
                    if (i%game.block.getBlocksInRow() != game.block.getBlocksInRow() - 1)
                        game.block.destroy(i + 1);
                    if (i < game.block.getBlocksInRow() * 4)
                        game.block.destroy(i + game.block.getBlocksInRow());
                }
            }
            else
                 changeDirection = false;

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
        if (game.block.getBounds(i).intersects(getBounds()))
            return game.block.getBounds(i).intersects(getBounds());
        else return false;
    }

    public void paint(Graphics2D g) {
        g.fillOval((int)x, (int)y, (int)diameter, (int)diameter);
    }

    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, (int)diameter, (int)diameter);
    }

    private double ballAngle(){
        return Math.toDegrees(Math.atan(xa/ya));
    }

    private double racquetAngle(){
        return (x + diameter /2 - racquet.getX())/(racquet.getWidth())*90 - 45;

    }

    ///get/set////////////////////////////////////////////////////////////////////////
    public double getDiameter() {
        return diameter;
    }

    public void setDiameter(double diameter) {
        this.diameter = diameter;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setXa(double xa) {
        this.xa = xa;
    }

    public void setYa(double ya) {
        this.ya = ya;
    }

    public void changeThru(boolean thruAdd) {
        if (thruAdd)
            thru++;
        else
            thru--;
    }

    public void changeWeak(boolean weakAdd) {
        if (weakAdd)
            weak++;
        else
            weak--;
    }

    public void changeCreator(boolean creatorAdd) {
        if (creatorAdd)
            creator++;
        else
            creator--;
    }

    public void changeExplosive(boolean explosiveAdd) {
        if (explosiveAdd)
            explosive++;
        else
            explosive--;
    }
}