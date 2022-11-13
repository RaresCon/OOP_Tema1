package cards;

import fileio.CardInput;

import java.util.Comparator;
import java.util.List;

public class Hero extends Card {
    private int healthStat = 30;
    private HeroType heroType;

    public Hero(final CardInput cardInput, final CardType cardType, final HeroType heroType) {
        name = cardInput.getName();
        description = cardInput.getDescription();
        colors = cardInput.getColors();
        mana = cardInput.getMana();
        this.cardType = cardType;
        this.heroType = heroType;
    }

    /**
     *
     * @param affectedRow
     */
    public void heroAbility(final List<Minion> affectedRow) {
        switch (heroType) {
            case ROYCE:
                Minion maxAttackMinion;

                if (affectedRow.stream().max(Comparator.comparing(Minion::getAttackStat))
                               .isPresent()) {
                    maxAttackMinion = affectedRow.stream()
                                      .max(Comparator.comparing(Minion::getAttackStat)).get();
                } else {
                    return;
                }
                maxAttackMinion.setFrozenStat(2);
                break;
            case THORINA:
                Minion maxHealthMinion;

                if (affectedRow.stream().max(Comparator.comparing(Minion::getHealthStat))
                               .isPresent()) {
                    maxHealthMinion = affectedRow.stream()
                                      .max(Comparator.comparing(Minion::getHealthStat)).get();
                } else {
                    return;
                }
                affectedRow.remove(maxHealthMinion);
                break;
            case MUDFACE:
                for (Minion minion : affectedRow) {
                    minion.setHealthStat(minion.getHealthStat() + 1);
                }
                break;
            case KOCIORAW:
                for (Minion minion : affectedRow) {
                    minion.setAttackStat(minion.getAttackStat() + 1);
                }
                break;
            default:
                break;
        }
        isActive = false;
    }

    /**
     *
     * @return
     */
    public int getHealthStat() {
        return healthStat;
    }

    /**
     *
     * @param healthStat
     */
    public void setHealthStat(final int healthStat) {
        this.healthStat = healthStat;
    }

    /**
     *
     * @return
     */
    public HeroType getHeroType() {
        return heroType;
    }

    /**
     *
     * @return
     */
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
