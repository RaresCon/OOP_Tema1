package cards;

import fileio.CardInput;

public class Minion extends Card {
    private int attackStat;
    private int healthStat;
    private int frozenStat;
    private MinionType minionType;
    private boolean isTank = false;

    public Minion(CardInput cardInput, CardType cardType, MinionType minionType) {
        name = cardInput.getName();
        description = cardInput.getDescription();
        mana = cardInput.getMana();
        colors = cardInput.getColors();
        attackStat = cardInput.getAttackDamage();
        healthStat = cardInput.getHealth();
        this.cardType = cardType;
        this.minionType = minionType;
        if (minionType.equals(MinionType.WARDEN) || minionType.equals(MinionType.GOLIATH))
            isTank = true;
        frozenStat = 0;
    }

    public void minionAttack(Minion minion) {
        minion.healthStat -= attackStat;
        isActive = false;
    }

    public void minionAttack(Hero hero) {
        hero.setHealthStat(hero.getHealthStat() - attackStat);
        isActive = false;
    }

    public void minionAbility(Minion cardAttacked) {
        switch (minionType) {
            case RIPPER -> {
                cardAttacked.attackStat -= 2;
                if (cardAttacked.attackStat < 0)
                    cardAttacked.attackStat = 0;
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
        }
        isActive = false;
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

    public void setFrozenStat(int frozenStat) {
        this.frozenStat = frozenStat;
    }

    public int isFrozenStat() {
        return frozenStat;
    }

    public MinionType getMinionType() {
        return minionType;
    }

    public void setMinionType(MinionType minionType) {
        this.minionType = minionType;
    }

    public boolean isTank() {
        return isTank;
    }

    public void setTank(boolean tank) {
        isTank = tank;
    }

    @Override
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
