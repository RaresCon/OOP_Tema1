package cards;

import fileio.CardInput;

public abstract class Minion extends Card {
    protected int attackStat;
    protected int healthStat;
    protected int frozenStat;
    protected boolean isTank;
    protected int homeRow;

    protected Minion(final CardInput cardInput) {
        super(cardInput);
    }

    /**
     * function to use the attack on other minion
     * @param minion the attacked minion
     */
    public void minionAttack(final Minion minion) {
        minion.healthStat -= attackStat;
        isActive = false;
    }

    /**
     * function to use the attack on a hero
     * @param hero the attacked hero
     */
    public void minionAttack(final Hero hero) {
        hero.setHealthStat(hero.getHealthStat() - attackStat);
        isActive = false;
    }

    /**
     * getter
     * @return card's attack
     */
    public int getAttackStat() {
        return attackStat;
    }

    /**
     * setter
     * @param attackStat the new attack for the card
     */
    public void setAttackStat(final int attackStat) {
        this.attackStat = attackStat;
    }

    /**
     * getter
     * @return card's health
     */
    public int getHealthStat() {
        return healthStat;
    }

    /**
     * setter
     * @param healthStat the new health for the card
     */
    public void setHealthStat(final int healthStat) {
        this.healthStat = healthStat;
    }

    /**
     * setter
     * @param frozenStat how many rounds (one round = two turns) a card is frozen
     */
    public void setFrozenStat(final int frozenStat) {
        this.frozenStat = frozenStat;
    }

    /**
     * getter
     * @return the current frozenStat
     */
    public int isFrozenStat() {
        return frozenStat;
    }

    /**
     * getter
     * @return if a card is a tank
     */
    public boolean isTank() {
        return isTank;
    }

    /**
     * getter
     * @return the row on which a card is placed at first
     */
    public int getHomeRow() {
        return homeRow;
    }
}
