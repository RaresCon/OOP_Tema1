package cards;

import fileio.CardInput;

public class Minion extends Card {
    private int attackStat;
    private int healthStat;
    private boolean frozenStat = false;
    private MinionType minionType;

    public Minion(CardInput cardInput, CardType cardType, MinionType minionType) {
        name = cardInput.getName();
        description = cardInput.getDescription();
        mana = cardInput.getMana();
        colors = cardInput.getColors();
        attackStat = cardInput.getAttackDamage();
        healthStat = cardInput.getHealth();
        this.cardType = cardType;
        this.minionType = minionType;
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

    public void setFrozenStat(boolean frozenStat) {
        this.frozenStat = frozenStat;
    }

    public boolean isFrozenStat() {
        return frozenStat;
    }

    public MinionType getMinionType() {
        return minionType;
    }

    public void setMinionType(MinionType minionType) {
        this.minionType = minionType;
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
