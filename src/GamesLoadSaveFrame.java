import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;


public class GamesLoadSaveFrame extends JOptionPane{
    private JTextField fileNameTextF = new JTextField(), dirTextF = new JTextField();

    public GamesLoadSaveFrame(boolean save){
        JFileChooser c = new JFileChooser("E:\\Projekty\\Java\\Arkanoid\\saves");
        if (save) {
            // Demonstrate "Save" dialog:
            int rVal = c.showSaveDialog(GamesLoadSaveFrame.this);
            if (rVal == JFileChooser.APPROVE_OPTION) {
                fileNameTextF.setText(c.getSelectedFile().getName());
                dirTextF.setText(c.getCurrentDirectory().toString());
            }
            if (rVal == JFileChooser.CANCEL_OPTION) {
                fileNameTextF.setText("You pressed cancel");
                dirTextF.setText("");
            }
        } else {
            // Demonstrate "Open" dialog:
            int rVal = c.showOpenDialog(GamesLoadSaveFrame.this);
            if (rVal == JFileChooser.APPROVE_OPTION) {
                fileNameTextF.setText(c.getSelectedFile().getName());
                dirTextF.setText(c.getCurrentDirectory().toString());
                System.out.println(dirTextF.getText() + "\\" + fileNameTextF.getText());
            }
            if (rVal == JFileChooser.CANCEL_OPTION) {
                fileNameTextF.setText("You pressed cancel");
                dirTextF.setText("");
            }
        }
    }

//get/set///////////////////////////////////////
    String getDirectory(){
        return dirTextF.getText() + "\\" + fileNameTextF.getText();
    }
}