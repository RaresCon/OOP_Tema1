package table_players;

import cards.Minion;

import java.util.ArrayList;
import java.util.List;

public class GameConfig {
    private Player playerOne = new Player();
    private Player playerTwo = new Player();
    private Player activePlayer = null;
    private int manaIncrement = 1;

    public void initRows(Player playerOne, Player playerTwo, int numRows) {
        for (int i = 0; i < numRows; i++) {
            playerOne.getPlayerRows().add(new ArrayList<>());
            playerTwo.getPlayerRows().add(new ArrayList<>());
        }
    }

    public Player getPlayerOne() {
        return playerOne;
    }

    public void setPlayerOne(Player playerOne) {
        this.playerOne = playerOne;
    }

    public Player getPlayerTwo() {
        return playerTwo;
    }

    public void setPlayerTwo(Player playerTwo) {
        this.playerTwo = playerTwo;
    }

    public Player getActivePlayer() {
        return activePlayer;
    }

    public void setActivePlayer(Player activePlayer) {
        this.activePlayer = activePlayer;
    }

    public int getManaIncrement() {
        return manaIncrement;
    }

    public void setManaIncrement(int manaIncrement) {
        this.manaIncrement = manaIncrement;
    }
}
