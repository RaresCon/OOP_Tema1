package cards;

import fileio.CardInput;

public class Minion extends Card {
    private int attackStat;
    private int healthStat;
    private boolean frozenStat = false;
    private CardType minionType;

    public Minion(CardInput cardInput, CardType cardType) {
        name = cardInput.getName();
        description = cardInput.getDescription();
        mana = cardInput.getMana();
        colors = cardInput.getColors();
        attackStat = cardInput.getAttackDamage();
        healthStat = cardInput.getHealth();
        minionType = cardType;
    }

    public int getAttackStat() {
        return attackStat;
    }

    public void setAttackStat(int attackStat) {
        this.attackStat = attackStat;
    }

    public int getHealthStat() {
        return healthStat;
    }

    public void setHealthStat(int healthStat) {
        this.healthStat = healthStat;
    }

    public CardType getCardType() {
        return minionType;
    }

    public void setFrozenStat(boolean frozenStat) {
        this.frozenStat = frozenStat;
    }

    public boolean isFrozenStat() {
        return frozenStat;
    }

    @Override
    public String toString() {
        return "CardInput{"
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
                + '}';
    }
}
