import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

class MenuPanel extends JPanel{

    private String[] menuNames = new String[6];
    private boolean isOpen = true;
    private int choosenOption = 0;


    MenuPanel(){
        App.FRAME.setLayout(new BoxLayout(App.FRAME.getContentPane(), BoxLayout.Y_AXIS));

        menuNames[0] = "START GAME";
        menuNames[1] = "SAVE GAME";
        menuNames[2] = "LOAD GAME";
        menuNames[3] = "HIGH SCORES";
        menuNames[4] = "HELP";
        menuNames[5] = "EXIT";

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put
                (KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "down");
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put
                (KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "up");
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put
                (KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "enter");
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put
                (KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "escape");

        this.getActionMap().put("down", new MoveAction("down"));
        this.getActionMap().put("up", new MoveAction("up"));
        this.getActionMap().put("enter", new MoveAction("enter"));
        this.getActionMap().put("escape", new MoveAction("escape"));
    }


    private class MoveAction extends AbstractAction {

        String keyPressed;

        MoveAction(String keyPressed){
            this.keyPressed = keyPressed;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (keyPressed.equals("down")) {
                choosenOption++;
                repaint();
            }
            if (keyPressed.equals("up")) {
                choosenOption--;
                repaint();
            }
            choosenOption = (choosenOption + menuNames.length) % menuNames.length;
            if (keyPressed.equals("enter")) {
                switch (choosenOption) {
                    case 0: //start/resume
                        System.out.println(choosenOption);
                        App.openGame(true);
                        menuNames[0] = "RESUME GAME";
                        break;
                    case 1: //save
                        System.out.println(choosenOption);
                        break;
                    case 2: //load
                        System.out.println(choosenOption);
                        break;
                    case 3: //high scores
                        System.out.println(choosenOption);
                        App.openHighScores(true);
                        break;
                    case 4: //help
                        System.out.println(choosenOption);
                        App.openHelp(true);
                        break;
                    case 5: //exit
                        System.out.println(choosenOption);
                        int quit = JOptionPane.showConfirmDialog(App.FRAME, "Do you want to quit?");
                        if (quit == JOptionPane.OK_OPTION)
                            System.exit(1);
                        break;
                }
                App.openMenu(false);
                isOpen = false;
            }
//            if (keyPressed.equals("escape")){
//                App.openMenu(false);
//                isOpen = false;
//            }
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.GRAY);
        g2d.setFont(new Font("Verdana", Font.BOLD, 50));
        FontMetrics fontMetrics = g2d.getFontMetrics();
        for (int i = 0; i < menuNames.length; i++) {
            if (i == choosenOption)
                g2d.setColor(Color.BLACK);
                g2d.drawString(menuNames[i], App.FRAME.getWidth() / 2 - fontMetrics.stringWidth(menuNames[i]) / 2, 100 + i * 80);
                g2d.setColor(Color.GRAY);
        }
    }



//get/set///////////////////////////////////////////////////////////////////////////////////

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        System.out.println("setOpen");
        isOpen = !isOpen;
    }

}


