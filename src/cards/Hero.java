package cards;

import fileio.CardInput;

import java.util.Comparator;
import java.util.List;

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

    public void heroAbility(List<Minion> affectedRow) {
        switch (heroType) {
            case ROYCE:
                Minion maxAttackMinion;

                if (affectedRow.stream().max(Comparator.comparing(Minion::getAttackStat)).isPresent())
                    maxAttackMinion = affectedRow.stream().max(Comparator.comparing(Minion::getAttackStat)).get();
                else
                    return;
                maxAttackMinion.setFrozenStat(2);
                break;
            case THORINA:
                Minion maxHealthMinion;

                if (affectedRow.stream().max(Comparator.comparing(Minion::getHealthStat)).isPresent())
                    maxHealthMinion = affectedRow.stream().max(Comparator.comparing(Minion::getHealthStat)).get();
                else
                    return;
                affectedRow.remove(maxHealthMinion);
                break;
            case MUDFACE:
                for (Minion minion : affectedRow)
                    minion.setHealthStat(minion.getHealthStat() + 1);
                break;
            case KOCIORAW:
                for (Minion minion : affectedRow)
                    minion.setAttackStat(minion.getAttackStat() + 1);
                break;
        }
        isActive = false;
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

    @Override
    public String toString() {
        return "Hero{"
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
