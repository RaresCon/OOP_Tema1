package cards;

import fileio.CardInput;

import java.util.List;

public abstract class Card {
    protected String name;
    protected String description;
    protected List<String> colors;
    protected int mana;
    protected CardType cardType;
    protected boolean isActive;
    protected int onFriendAbility;

    protected Card(final CardInput cardInput) {
        name = cardInput.getName();
        description = cardInput.getDescription();
        colors = cardInput.getColors();
        mana = cardInput.getMana();
    }

    /**
     * getter
     * @return name of the card
     */
    public String getName() {
        return name;
    }

    /**
     * getter
     * @return description of the card
     */
    public String getDescription() {
        return description;
    }

    /**
     * getter
     * @return color list of the card
     */
    public List<String> getColors() {
        return colors;
    }

    /**
     * getter
     * @return card's mana cost
     */
    public int getMana() {
        return mana;
    }

    /**
     * getter
     * @return the card type of the card
     */
    public CardType getCardType() {
        return cardType;
    }

    /**
     * getter
     * @return if the card is active
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * setter
     */
    public void setActive(final boolean active) {
        isActive = active;
    }

    /**
     * getter
     * @return 1 if the ability is friendly, -1 if it's not, 0 if there is no ability
     */
    public int getOnFriendAbility() {
        return onFriendAbility;
    }
}
