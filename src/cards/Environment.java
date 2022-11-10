package cards;

import fileio.CardInput;

public class Environment extends Card{
    private CardType environmentType;

    public Environment(CardInput cardInput, CardType cardType) {
        name = cardInput.getName();
        description = cardInput.getDescription();
        mana = cardInput.getMana();
        colors = cardInput.getColors();
        environmentType = cardType;
    }

    public CardType getEnvironmentType() {
        return environmentType;
    }

    public void setEnvironmentType(CardType environmentType) {
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
