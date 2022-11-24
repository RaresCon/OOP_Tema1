package cards;

import abilities.OnRowAbilities;
import fileio.CardInput;
import tableplayers.GameConstants;

import java.util.List;

public class Hero extends Card {
    private int healthStat = GameConstants.StdHeroHealth;
    private final OnRowAbilities heroAbility;

    public Hero(final CardInput cardInput, final CardType cardType,
                final int onFriendAbility, final OnRowAbilities ability) {
        super(cardInput);
        heroAbility = ability;
        this.cardType = cardType;
        this.onFriendAbility = onFriendAbility;
    }

    /**
     * function to apply the hero ability on a row
     * @param affectedRow the row on which the ability is applied
     */
    public void useHeroAbility(final List<Minion> affectedRow) {
        heroAbility.useAbility(affectedRow);
        isActive = false;
    }

    /**
     * getter
     * @return hero's health
     */
    public int getHealthStat() {
        return healthStat;
    }

    /**
     * setter
     * @param healthStat new hero health
     */
    public void setHealthStat(final int healthStat) {
        this.healthStat = healthStat;
    }
}
