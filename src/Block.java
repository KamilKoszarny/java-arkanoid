import java.awt.*;
import java.util.Random;

public class Block {
    private static final int HEIGHT = 50;
    private static final int SPAN = 2;
    private int bonusesNumber = 1;
    private int toughtsNumber = 1;

    public int blocksNumber = 1000;
    private int blocksInRow;
    private int blockWidth;
    private final int x[] = new int [blocksNumber];
    private final int y[] = new int [blocksNumber];
    private boolean exists[] = new boolean[blocksNumber];
    private boolean hasBonus[] = new boolean[blocksNumber];
    private boolean tought[] = new boolean[blocksNumber];
//    private int[] fallenDistance = new int[blocksNumber];

    private Game game;
    private Bonus[] bonus = new Bonus[blocksNumber];

    public Block(Game game, int blocksNumber, int bonusesNumber, int toughtsNumber) {
        this.game = game;
        this.blocksNumber = blocksNumber;
        this.bonusesNumber = bonusesNumber;
        this.toughtsNumber = toughtsNumber;
        blocksInRow = blocksNumber/5;
        blockWidth = 800/blocksInRow;
    }

    public void init(){
        x[0] = 0;
        for (int i = 0; i < blocksNumber; i++)
            exists[i] = true;

        int bonuses = 0;
        while (bonuses != bonusesNumber) {
            bonuses = 0;
            for (int i = 0; i < blocksNumber; i++)
                hasBonus[i] = false;
            Random r = new Random();
            for (int i = 0; i < bonusesNumber; i++) {
                hasBonus[r.nextInt(blocksNumber)] = true;
            }
            for (int i = 0; i < blocksNumber; i++)
                if (hasBonus[i])
                    bonuses++;
        }

        Random r = new Random();
        for (int i = 0; i < toughtsNumber; i++)
            tought[r.nextInt(blocksNumber)] = true;


        int j = 0;
        for (int i = 0; i < blocksNumber; i++) {
            if (hasBonus[i]) {
                bonus[i] = new Bonus(game);
            }
        }
    }


    public void paint(Graphics2D g) {
        for (int i = 0; i < blocksNumber; i++) {
            x[i] = i% blocksInRow * blockWidth;
            y[i] = Math.abs(i/ blocksInRow)*HEIGHT;
        }
        for (int i = 0; i < blocksNumber; i++) {
            if (exists[i]) {
                    g.fillRect(x[i] + SPAN / 2, y[i] + SPAN / 2, blockWidth - SPAN, HEIGHT - SPAN);
                    if (tought[i]) {
                        Color tmpColor = g.getColor();
                        g.setColor(tmpColor.darker());
                        g.fillRect(x[i] + SPAN / 2, y[i] + SPAN / 2, blockWidth - SPAN, HEIGHT - SPAN);
                        g.setColor(tmpColor);
                    }
            }
            else if (hasBonus[i]){
                bonus[i].paint(g, x[i] + SPAN / 2, y[i] + SPAN / 2,
                        blockWidth - SPAN, HEIGHT - SPAN);
            }
        }
    }

    public void move() {
        for (int i = 0; i < blocksNumber; i++) {
            if (!exists[i] && hasBonus[i])
                bonus[i].move();
        }
    }

    public Rectangle getBounds(int i) {
        if (exists[i]) {
            return new Rectangle(x[i], y[i], blockWidth, HEIGHT);
        }
        return new Rectangle(0,0,0,0);
    }

    public void destroy(int i){
        game.setScore(game.getScore() + 1);
        if (!tought[i]) {
            exists[i] = false;
        }
        else
            tought[i] = false;
    }

    public boolean getExists() {
        for (int i = 0; i < blocksNumber; i++)
            if (exists[i])
                return true;
        return false;
    }

    public void setTought(int i) {
        this.tought[i] = true;
    }

    public int getBlocksInRow() {
        return blocksInRow;
    }
}