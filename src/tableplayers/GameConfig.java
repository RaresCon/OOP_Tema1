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
     * @param currentIdx
     * @return
     */
    public List<Minion> getAttackedRow(final int currentIdx) {
        if (currentIdx > 1) {
            return playerOne.getPlayerRows().get(-(currentIdx - 3));
        } else {
            return playerTwo.getPlayerRows().get(currentIdx);
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
    public Player getInactivePlayer() {
        return inactivePlayer;
    }

    /**
     *
     * @param inactivePlayer
     */
    public void setInactivePlayer(final Player inactivePlayer) {
        this.inactivePlayer = inactivePlayer;
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
