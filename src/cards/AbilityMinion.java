package cards;

import abilities.OnCardAbilities;
import fileio.CardInput;

public class AbilityMinion extends Minion {
    private final OnCardAbilities minionAbility;

    public AbilityMinion(final CardInput cardInput, final CardType cardType, final boolean isTank,
                         final int homeRow, final int onFriendAbility,
                         final OnCardAbilities ability) {
        name = cardInput.getName();
        description = cardInput.getDescription();
        mana = cardInput.getMana();
        colors = cardInput.getColors();
        attackStat = cardInput.getAttackDamage();
        healthStat = cardInput.getHealth();
        minionAbility = ability;
        this.cardType = cardType;
        this.isTank = isTank;
        this.homeRow = homeRow;
        this.onFriendAbility = onFriendAbility;
    }

    /**
     *
     * @param cardAttacked
     */
    public void useMinionAbility(final Minion cardAttacked) {
        minionAbility.useAbility(this, cardAttacked);
        isActive = false;
    }
}
