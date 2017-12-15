import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class Menu extends JPanel{

    String[] menuNames = new String[5];
    JButton[] menuButtons = new JButton[5];

    Menu(){
        menuNames[0] = "START GAME";
        menuNames[1] = "SAVE GAME";
        menuNames[2] = "LOAD GAME";
        menuNames[3] = "HIGH SCORES";
        menuNames[4] = "EXIT";

        for (int i = 0; i < menuButtons.length; i++) {
            menuButtons[i] = new JButton(menuNames[i]);
        }

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put
                (KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), "down");

        this.getActionMap().put("move left", new MoveAction("left"));
    }

    private class MoveAction extends AbstractAction {

        String keyPressed;

        MoveAction(String keyPressed){
            this.keyPressed = keyPressed;
        }

        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }

    private boolean open = true;

//    void paint(Graphics2D g){
//        g.setColor(Color.BLACK);
//        g.setFont(new Font("Verdana", Font.BOLD, 50));
//        FontMetrics fontMetrics = g.getFontMetrics();
//        g.drawString(menuNames[0], App.FRAME.getWidth()/2 - fontMetrics.stringWidth(menuNames[0])/2, 100);
//    }

    void paint(Graphics2D g){
        App.FRAME.setLayout(new BoxLayout(App.FRAME.getContentPane(), BoxLayout.Y_AXIS));
        for (JButton menuButton : menuButtons) {

            menuButton.setVisible(true);
            menuButton.setSize(200,100);
            menuButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            App.FRAME.add(menuButton);
            App.FRAME.validate();
        }
    }

//get/set///////////////////////////////////////////////////////////////////////////////////


    public boolean isOpen() {
        return open;
    }

    public void setOpen() {
        open = !open;
    }

}


