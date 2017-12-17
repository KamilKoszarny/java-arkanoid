package highscores;

import java.util.*;
import java.io.*;

public class HighscoreManager {
    // An arraylist of the type "score" we will use to work with the scores inside the class
    private ArrayList<Score> scores;

    // The name of the file where the highscores will be saved
    private static final String HIGHSCORE_FILE = "D:\\Informatyka\\Projekty\\ArkanoidJava\\Arkanoid2\\scores.dat";

    //Initialising an in and outputStream for working with the file
    private ObjectOutputStream outputStream = null;

    public HighscoreManager() {
        //initialising the scores-arraylist
        scores = new ArrayList<Score>();
    }

    private ArrayList<Score> getScores() {
        loadScoreFile();
        sort();
        return scores;
    }

    private void loadScoreFile() {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(HIGHSCORE_FILE));
            scores = (ArrayList<Score>) inputStream.readObject();
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

    public void addScore(String name, int score) {
        loadScoreFile();
        scores.add(new Score(name, score));
        updateScoreFile();
    }

    private void updateScoreFile() {
        try {
            outputStream = new ObjectOutputStream(new FileOutputStream(HIGHSCORE_FILE));
            outputStream.writeObject(scores);
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

    public String[] getHighscoresTable(int count) {
        StringBuilder highscoreString = new StringBuilder();
        String[] highscoreStrings = new String[count];

        ArrayList<Score> scores;
        scores = getScores();

        int i = 0;
        int x = scores.size();
        if (x > count) {
            x = count;
        }
        while (i < x) {
            highscoreString.append(scores.get(i).getNaam()).append(" \t  ").append(scores.get(i).getScore());
            highscoreStrings[i] = highscoreString.toString();
            i++;
            highscoreString = new StringBuilder();
        }

        return highscoreStrings;
    }

    public String getHighscore() {
        String highscoreString = getHighscoresTable(1)[0];
        return highscoreString;
    }


////////////////////////////////////////////////////////////////////////
    private void sort() {
        ScoreComparator comparator = new ScoreComparator();
        scores.sort(comparator);
    }


}