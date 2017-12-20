import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;


public class GamesLoadSaver extends JFrame{

    public static void save(GamePanel game){
        GamesLoadSaveFrame glsFrame = new GamesLoadSaveFrame(true);
        String fileName = glsFrame.getDirectory();
        serialize(game, fileName);
    }

    public static GamePanel load(){
        GamesLoadSaveFrame glsFrame = new GamesLoadSaveFrame(false);
        String fileName = null;
        while (fileName == null) {
            fileName = glsFrame.getDirectory();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Object obj = deserialize(fileName);
        GamePanel game = (GamePanel) obj;
        return game;
    }

    private static void serialize(GamePanel game, String fileName){
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(fileName);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(game);
            oos.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private static Object deserialize(String fileName){
        FileInputStream fis = null;
        Object obj = null;
        try {
            fis = new FileInputStream(fileName);
            BufferedInputStream bis = new BufferedInputStream(fis);
            ObjectInputStream ois = new ObjectInputStream(bis);
            obj = ois.readObject();
            ois.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return obj;
    }
}