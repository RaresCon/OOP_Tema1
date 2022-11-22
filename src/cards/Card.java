package cards;

import java.util.List;

public abstract class Card {
    protected String name;
    protected String description;
    protected List<String> colors;
    protected int mana;
    protected CardType cardType;
    protected boolean isActive;
    protected int onFriendAbility;

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @return
     */
    public List<String> getColors() {
        return colors;
    }

    /**
     *
     * @return
     */
    public int getMana() {
        return mana;
    }

    /**
     *
     * @return
     */
    public void setMana(final int mana) {
        this.mana = mana;
    }

    /**
     *
     * @return
     */
    public CardType getCardType() {
        return cardType;
    }

    /**
     *
     * @return
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     *
     * @return
     */
    public void setActive(final boolean active) {
        isActive = active;
    }

    /**
     *
     * @return
     */
    public int getOnFriendAbility() {
        return onFriendAbility;
    }
}
