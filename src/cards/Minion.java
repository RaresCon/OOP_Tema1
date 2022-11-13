package cards;

import fileio.CardInput;

public class Minion extends Card {
    private int attackStat;
    private int healthStat;
    private int frozenStat;
    private MinionType minionType;
    private boolean isTank = false;

    public Minion(final CardInput cardInput, final CardType cardType, final MinionType minionType) {
        name = cardInput.getName();
        description = cardInput.getDescription();
        mana = cardInput.getMana();
        colors = cardInput.getColors();
        attackStat = cardInput.getAttackDamage();
        healthStat = cardInput.getHealth();
        this.cardType = cardType;
        this.minionType = minionType;
        if (minionType.equals(MinionType.WARDEN) || minionType.equals(MinionType.GOLIATH)) {
            isTank = true;
        }
        frozenStat = 0;
    }

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
     * @param cardAttacked
     */
    public void minionAbility(final Minion cardAttacked) {
        switch (minionType) {
            case RIPPER -> {
                cardAttacked.attackStat -= 2;
                if (cardAttacked.attackStat < 0) {
                    cardAttacked.attackStat = 0;
                }
            }
            case MIRAJ -> {
                healthStat -= cardAttacked.healthStat;
                cardAttacked.healthStat += healthStat;
                healthStat = cardAttacked.healthStat - healthStat;
            }
            case CURSED -> {
                cardAttacked.attackStat -= cardAttacked.healthStat;
                cardAttacked.healthStat += cardAttacked.attackStat;
                cardAttacked.attackStat = cardAttacked.healthStat - cardAttacked.attackStat;
            }
            default -> {

            }
        }
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
    public MinionType getMinionType() {
        return minionType;
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
     * @param attackStat
     */
    public void setTank(final boolean tank) {
        isTank = tank;
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
