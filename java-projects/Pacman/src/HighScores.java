import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class HighScores implements Serializable {
    private List<PlayerScore> scores;
    private static final String name = "HighScoresData.txt";

    public HighScores() {
        scores = new ArrayList<>();
        loadScores();
    }

    public void addScore(String playerName, int score) {
        PlayerScore newScore = new PlayerScore(playerName, score);
        scores.add(newScore);
        Collections.sort(scores, (s1, s2) -> Integer.compare(s2.getScore(), s1.getScore()));
        saveScores();
    }

    public List<PlayerScore> getScores() {
        return scores;
    }

    public void saveScores() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(name))) {
            outputStream.writeObject(scores);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadScores() {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(name))) {
            scores = (List<PlayerScore>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

