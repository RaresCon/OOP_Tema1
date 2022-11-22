package cards;

import abilities.OnRowAbilities;
import fileio.CardInput;

import java.util.List;

public class Environment extends Card {
    private final OnRowAbilities envAbility;

    public Environment(final CardInput cardInput, final CardType cardType,
                       final OnRowAbilities ability) {
        name = cardInput.getName();
        description = cardInput.getDescription();
        mana = cardInput.getMana();
        colors = cardInput.getColors();
        envAbility = ability;
        this.cardType = cardType;
    }

    /**
     *
     * @param affectedRow
     * @return
     */
    public Minion useEnvAbility(final List<Minion> affectedRow) {
        return envAbility.useAbility(affectedRow);
    }

    /**
     *
     * @return
     */
    public OnRowAbilities getEnvAbility() {
        return envAbility;
    }

    /**
     *
     * @return
     */
    public String toString() {
        return "Environment{"
                +  "mana="
                + mana
                +  ", description='"
                + description
                + '\''
                + ", colors="
                + colors
                + ", name='"
                +  ""
                + name
                + '\''
                + '}'
                + '\n';
    }
}
