import java.awt.*;
import java.io.Serializable;
import java.util.Random;

class Blocks implements Serializable{

    private static final long serialVersionUID = 1L;

    private static final int HEIGHT = 50;
    private static final int SPAN = 2;
    private int bonusesNumber = 1;
    private int toughtsNumber = 1;

    int blocksNumber = 1000;
    private int blocksInRow;
    private int blockWidth;
    private final int x[] = new int [blocksNumber];
    private final int y[] = new int [blocksNumber];
    private boolean exists[] = new boolean[blocksNumber];
    private boolean hasBonus[] = new boolean[blocksNumber];
    private boolean tought[] = new boolean[blocksNumber];
//    private int[] fallenDistance = new int[blocksNumber];

    private GamePanel gamePanel;
    private Bonus[] bonus = new Bonus[blocksNumber];

    Blocks(GamePanel gamePanel, int blocksNumber, int bonusesNumber, int toughtsNumber) {
        this.gamePanel = gamePanel;
        this.blocksNumber = blocksNumber;
        this.bonusesNumber = bonusesNumber;
        this.toughtsNumber = toughtsNumber;
        blocksInRow = blocksNumber/5;
        blockWidth = 800/blocksInRow;
    }

    void init(){
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

        for (int i = 0; i < blocksNumber; i++) {
            if (hasBonus[i]) {
                bonus[i] = new Bonus(gamePanel);
            }
        }
    }


    void paint(Graphics2D g) {
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

    void move() {
        for (int i = 0; i < blocksNumber; i++) {
            if (!exists[i] && hasBonus[i])
                bonus[i].move();
        }
    }

    Rectangle getBounds(int i) {
        if (exists[i]) {
            return new Rectangle(x[i], y[i], blockWidth, HEIGHT);
        }
        return new Rectangle(0,0,0,0);
    }

    void destroy(int i){
        gamePanel.setScore(gamePanel.getScore() + 1);
        if (!tought[i]) {
            exists[i] = false;
        }
        else
            tought[i] = false;
    }

    boolean getExists() {
        for (int i = 0; i < blocksNumber; i++)
            if (exists[i])
                return true;
        return false;
    }

    void setTought(int i) {
        this.tought[i] = true;
    }

    int getBlocksInRow() {
        return blocksInRow;
    }
}