import javax.swing.*;

public class Test {
    private static final JFrame FRAME = new JFrame();
    static String[] menuNames = new String[5];
    static JButton[] menuButtons = new JButton[5];

    Test() {
        menuNames[0] = "START GAME";
        menuNames[1] = "SAVE GAME";
        menuNames[2] = "LOAD GAME";
        menuNames[3] = "HIGH SCORES";
        menuNames[4] = "HELP";
        menuNames[5] = "EXIT";
    }

    public static void main(String[] args) {
        for (int i = 0; i < menuButtons.length; i++) {
            menuButtons[i] = new JButton(menuNames[i]);
            FRAME.add(menuButtons[i]);
        }
        FRAME.setLocation(300,100);
        FRAME.setSize(800, 600);
        FRAME.setResizable(false);
        FRAME.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        FRAME.setVisible(true);
    }
}
