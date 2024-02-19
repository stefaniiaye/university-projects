import java.io.Serializable;

public class PlayerScore implements Serializable {
    private String playerName;
    private int playerScore;

    public PlayerScore(String playerName, int playerScore){
        this.playerName = playerName;
        this.playerScore = playerScore;
    }
    public String getPlayerName() {
        return playerName;
    }

    public int getScore() {
        return playerScore;
    }

    @Override
    public String toString() {
        return playerName + " score : " + playerScore;
    }
}
