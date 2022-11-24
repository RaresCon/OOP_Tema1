package tableplayers;

import cards.Minion;

import java.util.ArrayList;
import java.util.List;

public class GameConfig {
    private final Player playerOne = new Player();
    private final Player playerTwo = new Player();
    private Player activePlayer = null;
    private Player inactivePlayer = null;
    private int manaIncrement = 1;
    private int turnsNum;

    /**
     * function to initialize the rows for each player
     * @param numRows number of rows available on the table
     */
    public void initRows(final int numRows) {
        for (int i = 0; i < numRows; i++) {
            playerOne.getPlayerRows().add(new ArrayList<>());
            playerTwo.getPlayerRows().add(new ArrayList<>());
        }
    }

    /**
     * function that resolves the index of the row for any player (arithmetically)
     * @param currentIdx the index given as input by the player
     * @return the row that is attacked
     */
    public List<Minion> getAttackedRow(final int currentIdx) {
        if (currentIdx > 1) {
            return playerOne.getPlayerRows().get(-(currentIdx - 3));
        } else {
            return playerTwo.getPlayerRows().get(currentIdx);
        }
    }

    /**
     * getter
     * @return player one object
     */
    public Player getPlayerOne() {
        return playerOne;
    }

    /**
     * getter
     * @return player two object
     */
    public Player getPlayerTwo() {
        return playerTwo;
    }

    /**
     * getter
     * @return active player object
     */
    public Player getActivePlayer() {
        return activePlayer;
    }

    /**
     * setter
     * @param activePlayer which player to set active
     */
    public void setActivePlayer(final Player activePlayer) {
        this.activePlayer = activePlayer;
    }

    /**
     * getter
     * @return inactive player object
     */
    public Player getInactivePlayer() {
        return inactivePlayer;
    }

    /**
     * setter
     * @param inactivePlayer which player to set inactive
     */
    public void setInactivePlayer(final Player inactivePlayer) {
        this.inactivePlayer = inactivePlayer;
    }

    /**
     * getter
     * @return current mana increment after every two turns
     */
    public int getManaIncrement() {
        return manaIncrement;
    }

    /**
     * setter
     * @param manaIncrement new mana increment
     */
    public void setManaIncrement(final int manaIncrement) {
        this.manaIncrement = manaIncrement;
    }

    /**
     * getter
     * @return number of turns that passed
     */
    public int getTurnsNum() {
        return turnsNum;
    }

    /**
     * setter
     * @param turnsNum number of turns that passed
     */
    public void setTurnsNum(final int turnsNum) {
        this.turnsNum = turnsNum;
    }
}
