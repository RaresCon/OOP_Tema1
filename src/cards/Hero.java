package cards;

import fileio.CardInput;

public class Hero extends Card {
    private int healthStat;
    private CardType heroType;

    public Hero(CardInput cardInput, CardType cardType) {
        name = cardInput.getName();
        description = cardInput.getDescription();
        colors = cardInput.getColors();
        mana = cardInput.getMana();
        healthStat = 30;
        heroType = cardType;
    }

    public int getHealthStat() {
        return healthStat;
    }

    public void setHealthStat(int healthStat) {
        this.healthStat = healthStat;
    }

    public CardType getHeroType() {
        return heroType;
    }

    @Override
    public String toString() {
        return "CardInput{"
                +  "mana="
                + mana
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
