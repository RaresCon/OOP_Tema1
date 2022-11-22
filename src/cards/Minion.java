package cards;

public abstract class Minion extends Card {
    protected int attackStat;
    protected int healthStat;
    protected int frozenStat;
    protected boolean isTank;
    protected int homeRow;

    /**
     *
     * @param minion
     */
    public void minionAttack(final Minion minion) {
        minion.healthStat -= attackStat;
        isActive = false;
    }

    /**
     *
     * @param hero
     */
    public void minionAttack(final Hero hero) {
        hero.setHealthStat(hero.getHealthStat() - attackStat);
        isActive = false;
    }

    /**
     *
     * @return
     */
    public int getAttackStat() {
        return attackStat;
    }

    /**
     *
     * @param attackStat
     */
    public void setAttackStat(final int attackStat) {
        this.attackStat = attackStat;
    }

    /**
     *
     * @param attackStat
     */
    public int getHealthStat() {
        return healthStat;
    }

    /**
     *
     * @param attackStat
     */
    public void setHealthStat(final int healthStat) {
        this.healthStat = healthStat;
    }

    /**
     *
     * @param attackStat
     */
    public void setFrozenStat(final int frozenStat) {
        this.frozenStat = frozenStat;
    }

    /**
     *
     * @param attackStat
     */
    public int isFrozenStat() {
        return frozenStat;
    }

    /**
     *
     * @param attackStat
     */
    public boolean isTank() {
        return isTank;
    }

    /**
     *
     * @return
     */
    public int getHomeRow() {
        return homeRow;
    }

    /**
     *
     * @param attackStat
     */
    public String toString() {
        return "Minion{"
                +  "mana="
                + mana
                +  ", attackDamage="
                + attackStat
                + ", health="
                + healthStat
                +  ", description='"
                + description
                + '\''
                + ", colors="
                + colors
                + ", name='"
                +  ""
                + name
                + '\''
                + '}'
                + '\n';
    }
}
