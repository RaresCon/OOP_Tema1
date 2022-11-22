package cards;

import fileio.CardInput;

public class BasicMinion extends Minion {
    public BasicMinion(final CardInput cardInput, final CardType cardType,
                       final boolean isTank, final int homeRow) {
        name = cardInput.getName();
        description = cardInput.getDescription();
        mana = cardInput.getMana();
        colors = cardInput.getColors();
        attackStat = cardInput.getAttackDamage();
        healthStat = cardInput.getHealth();
        this.cardType = cardType;
        this.isTank = isTank;
        this.homeRow = homeRow;
    }
}
