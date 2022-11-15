package tableplayers;

import cards.Minion;

import java.util.ArrayList;
import java.util.List;

public class GameConfig {
    private Player playerOne = new Player();
    private Player playerTwo = new Player();
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

    public Minion getCardFromTable(final int x, final int y, final GameConfig gameConfig) {
        if (x < 2 && y < gameConfig.getPlayerTwo().getPlayerRows().get(x).size()) {
            return gameConfig.getPlayerTwo().getPlayerRows().get(x).get(y);
        } else if (x > 1 && y < gameConfig.getPlayerOne().getPlayerRows().get(-(x - 3)).size()) {
            return gameConfig.getPlayerOne().getPlayerRows().get(-(x - 3)).get(y);
        }

        return null;
    }

    public List<Minion> getAttackedRow(GameConfig gameConfig, int currentIdx) {
        if (currentIdx > 1) {
            return gameConfig.getPlayerOne().getPlayerRows().get(-(currentIdx - 3));
        } else {
            return gameConfig.getPlayerTwo().getPlayerRows().get(currentIdx);
        }
    }

    /**
     *
     * @param player
     * @return
     */
    public boolean checkTankOnRows(final Player player) {
        for (Minion minion : player.getPlayerRows().get(1)) {
            if (minion.isTank()) {
                return true;
            }
        }
        return false;
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

    public Player getInactivePlayer() {
        return inactivePlayer;
    }

    public void setInactivePlayer(Player inactivePlayer) {
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
