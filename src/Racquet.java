import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Objects;

class Racquet {
    static final int Y = 500;
    static final int HEIGHT = 20;
    private double width = 120;
    private double x = 340;
    private double xa = 0;
    private Game game;

    Racquet(Game game) {
        this.game = game;
    }

    void move() {
        if (x + xa > 0 && x + xa < game.getWidth() - width)
            x += xa;
    }

    void directionSet(String direction){
        if (Objects.equals(direction, "left"))
            xa = -game.speed - 3;
        if (Objects.equals(direction, "right"))
            xa = game.speed + 3;
        if (Objects.equals(direction, "stop"))
            xa = 0;
    }

    void paint(Graphics2D g) {
        g.fillRect((int)x, Y, (int)width, HEIGHT);
    }


////get/set////////////////////////////////////////////////////////////////////////////////////////////
    Rectangle getBounds() {
        return new Rectangle((int)x, Y, (int)width, HEIGHT);
    }

    int getTopY() {
        return Y - HEIGHT;
    }

    static int getY() {
        return Y;
    }

    double getX() {
        return x;
    }

    void setX(double x) {
        this.x = x;
    }

    double getWidth() {
        return width;
    }

    void setWidth(double width) {
        this.width = width;
    }
}