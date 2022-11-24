package cards;

import abilities.OnRowAbilities;
import fileio.CardInput;

import java.util.List;

public class Environment extends Card {
    private final OnRowAbilities envAbility;

    public Environment(final CardInput cardInput, final CardType cardType,
                       final OnRowAbilities ability) {
        super(cardInput);
        envAbility = ability;
        this.cardType = cardType;
    }

    /**
     * function to use the environment ability
     * @param affectedRow the row on which the ability is applied
     * @return it returns the stolen Minion card (for STEAL_CARD), null otherwise
     *         may return a Minion for other abilities that may need this
     */
    public Minion useEnvAbility(final List<Minion> affectedRow) {
        return envAbility.useAbility(affectedRow);
    }

    /**
     * getter
     * @return card's environment ability
     */
    public OnRowAbilities getEnvAbility() {
        return envAbility;
    }
}
