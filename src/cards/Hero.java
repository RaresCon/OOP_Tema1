package cards;

import abilities.OnRowAbilities;
import fileio.CardInput;

import java.util.List;

public class Hero extends Card {
    private int healthStat = 30;
    private final OnRowAbilities heroAbility;

    public Hero(final CardInput cardInput, final CardType cardType,
                final int onFriendAbility, final OnRowAbilities ability) {
        name = cardInput.getName();
        description = cardInput.getDescription();
        colors = cardInput.getColors();
        mana = cardInput.getMana();
        heroAbility = ability;
        this.cardType = cardType;
        this.onFriendAbility = onFriendAbility;
    }

    /**
     *
     * @param affectedRow
     */
    public void useHeroAbility(final List<Minion> affectedRow) {
        heroAbility.useAbility(affectedRow);
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
