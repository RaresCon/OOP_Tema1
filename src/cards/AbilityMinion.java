package cards;

import abilities.OnCardAbilities;
import fileio.CardInput;

public class AbilityMinion extends Minion {
    private final OnCardAbilities minionAbility;

    public AbilityMinion(final CardInput cardInput, final CardType cardType, final boolean isTank,
                         final int homeRow, final int onFriendAbility,
                         final OnCardAbilities ability) {
        super(cardInput);
        attackStat = cardInput.getAttackDamage();
        healthStat = cardInput.getHealth();
        minionAbility = ability;
        this.cardType = cardType;
        this.isTank = isTank;
        this.homeRow = homeRow;
        this.onFriendAbility = onFriendAbility;
    }

    /**
     * function to use the minion ability
     * @param cardAttacked the card (minion) that is affected by the ability
     */
    public void useMinionAbility(final Minion cardAttacked) {
        minionAbility.useAbility(this, cardAttacked);
        isActive = false;
    }
}
