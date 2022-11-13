package cards;

import fileio.CardInput;

public class Environment extends Card {
    private EnvironmentType environmentType;

    public Environment(final CardInput cardInput, final CardType cardType,
                       final EnvironmentType environmentType) {
        name = cardInput.getName();
        description = cardInput.getDescription();
        mana = cardInput.getMana();
        colors = cardInput.getColors();
        this.cardType = cardType;
        this.environmentType = environmentType;
    }

    /**
     *
     * @return
     */
    public EnvironmentType getEnvironmentType() {
        return environmentType;
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
