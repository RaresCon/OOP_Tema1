package table.players;

import java.util.ArrayList;

public class GameConfig {
    private Player playerOne = new Player();
    private Player playerTwo = new Player();
    private Player activePlayer = null;
    private int manaIncrement = 1;
    private final int manaMaxIncrement = 10;
    private final int maxCardsOnRow = 5;
    private int turnsNum;

    /**
     *
     * @param playerOne
     * @param playerTwo
     * @param numRows
     */
    public void initRows(final int numRows) {
        for (int i = 0; i < numRows; i++) {
            playerOne.getPlayerRows().add(new ArrayList<>());
            playerTwo.getPlayerRows().add(new ArrayList<>());
        }
    }

    /**
     *
     * @return
     */
    public Player getPlayerOne() {
        return playerOne;
    }

    /**
     *
     * @return
     */
    public Player getPlayerTwo() {
        return playerTwo;
    }

    /**
     *
     * @return
     */
    public Player getActivePlayer() {
        return activePlayer;
    }

    /**
     *
     * @param activePlayer
     */
    public void setActivePlayer(final Player activePlayer) {
        this.activePlayer = activePlayer;
    }

    /**
     *
     * @return
     */
    public int getManaIncrement() {
        return manaIncrement;
    }

    /**
     *
     * @param manaIncrement
     */
    public void setManaIncrement(final int manaIncrement) {
        this.manaIncrement = manaIncrement;
    }

    /**
     *
     * @return
     */
    public int getTurnsNum() {
        return turnsNum;
    }

    /**
     *
     * @param turnsNum
     */
    public void setTurnsNum(final int turnsNum) {
        this.turnsNum = turnsNum;
    }

    /**
     *
     * @return
     */
    public int getManaMaxIncrement() {
        return manaMaxIncrement;
    }

    /**
     *
     * @return
     */
    public int getMaxCardsOnRow() {
        return maxCardsOnRow;
    }
}
