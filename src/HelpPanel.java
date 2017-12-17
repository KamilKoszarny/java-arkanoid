import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class HelpPanel extends JPanel {

    private boolean isOpen = false;

    HelpPanel(){
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put
                (KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "escape");

        this.getActionMap().put("escape", new MoveAction("escape"));
    }

    private class MoveAction extends AbstractAction {

        String keyPressed;

        MoveAction(String keyPressed) {
            this.keyPressed = keyPressed;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (keyPressed.equals("escape")) {
                App.openMenu(true);
                isOpen = false;
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        Bonus.helpBonusPaint(g2d);
    }

//get/set////////////////////////////////////////////////////////////

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }
}
