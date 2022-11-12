package cards;

import fileio.CardInput;

public class Environment extends Card{
    private EnvironmentType environmentType;

    public Environment(CardInput cardInput, CardType cardType, EnvironmentType environmentType) {
        name = cardInput.getName();
        description = cardInput.getDescription();
        mana = cardInput.getMana();
        colors = cardInput.getColors();
        this.cardType = cardType;
        this.environmentType = environmentType;
    }

    public EnvironmentType getEnvironmentType() {
        return environmentType;
    }

    public void setEnvironmentType(EnvironmentType environmentType) {
        this.environmentType = environmentType;
    }

    @Override
    public String toString() {
        return "CardInput{"
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
                + '}';
    }
}
