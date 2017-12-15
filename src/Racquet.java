import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Racquet {
    public static final int Y = 500;
    public static final int HEIGHT = 20;
    private double width = 120;
    private double x = 340;
    private double xa = 0;
    private Game game;

    public Racquet(Game game) {
        this.game = game;
    }

    public void move() {
        if (x + xa > 0 && x + xa < game.getWidth() - width)
            x += xa;
    }

    public void directionSet(String direction){
        if (direction == "left")
            xa = -game.speed - 3;
        if (direction == "right")
            xa = game.speed + 3;
        if (direction == "stop")
            xa = 0;
    }

    public void paint(Graphics2D g) {
        g.fillRect((int)x, Y, (int)width, HEIGHT);
    }


////get/set////////////////////////////////////////////////////////////////////////////////////////////
    public Rectangle getBounds() {
        return new Rectangle((int)x, Y, (int)width, HEIGHT);
    }

    public int getTopY() {
        return Y - HEIGHT;
    }

    public static int getY() {
        return Y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }
}