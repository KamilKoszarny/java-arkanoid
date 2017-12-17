import java.io.*;
import java.util.ArrayList;

public class GamesLoadSaver {
    // An arraylist of the type "GamePanel" we will use to work with the gamePanels inside the class
    private ArrayList<GamePanel> gamePanels;

    // The name of the folder where the games will be saved
    private static final String GAMES_FOLDER = "E:\\Projekty\\Java\\Arkanoid\\games.dat";

    //Initialising an in and outputStream for working with the file
    private ObjectOutputStream outputStream = null;

    public GamesLoadSaver() {
        //initialising the gamePanels-arraylist
        gamePanels = new ArrayList<GamePanel>();
    }

    private void saveGame(GamePanel game) {

        try {
            outputStream = new ObjectOutputStream(new FileOutputStream(GAMES_FOLDER));
            outputStream.writeObject(gamePanels);
        } catch (FileNotFoundException e) {
            System.out.println("[Update] FNF Error: " + e.getMessage() + ",the program will try and make a new file");
        } catch (IOException e) {
            System.out.println("[Update] IO Error: " + e.getMessage());
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (IOException e) {
                System.out.println("[Update] Error: " + e.getMessage());
            }
        }
    }

    private void loadGame(GamePanel game) {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(GAMES_FOLDER));
            gamePanels = (ArrayList<GamePanel>) inputStream.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("[Laad] FNF Error: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("[Laad] IO Error: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("[Laad] CNF Error: " + e.getMessage());
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (IOException e) {
                System.out.println("[Laad] IO Error: " + e.getMessage());
            }
        }
    }
}