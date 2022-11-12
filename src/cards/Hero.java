package cards;

import fileio.CardInput;

public class Hero extends Card {
    private int healthStat;
    private HeroType heroType;

    public Hero(CardInput cardInput, CardType cardType, HeroType heroType) {
        name = cardInput.getName();
        description = cardInput.getDescription();
        colors = cardInput.getColors();
        mana = cardInput.getMana();
        healthStat = 30;
        this.cardType = cardType;
        this.heroType = heroType;
    }

    public int getHealthStat() {
        return healthStat;
    }

    public void setHealthStat(int healthStat) {
        this.healthStat = healthStat;
    }

    public HeroType getHeroType() {
        return heroType;
    }

    public void setHeroType(HeroType heroType) {
        this.heroType = heroType;
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
