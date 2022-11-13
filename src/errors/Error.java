package errors;

public enum Error {
    PLACE_ENV_ERR("Cannot place environment card on table."),
    USE_NONENV_ERR("Chosen card is not of type environment."),
    ROW_SPACE_ERR("Cannot place card on table since row is full."),
    OWN_ROW_ERR("Chosen row does not belong to the enemy."),
    STEAL_ERR("Cannot steal enemy card since the player's row is full."),
    MANA_ERR("Not enough mana to place card on table."),
    ENV_MANA_ERR("Not enough mana to use environment card."),
    ATTACKER_ERR("Attacked card does not belong to the enemy."),
    INACTIVE_CARD_ERR("Attacker card has already attacked this turn."),
    FROZEN_CARD("Attacker card is frozen."),
    TANK_ERR("Attacked card is not of type 'Tank'."),
    OWN_CARD_ERR("Attacked card does not belong to the current player."),
    HERO_MANA_ERR("Not enough mana to use hero's ability."),
    INACTIVE_HERO("Hero has already attacked this turn."),
    HERO_ENEMY_ROW_ERR("Selected row does not belong to the enemy."),
    HERO_FRIEND_ROW_ERR("Selected row does not belong to the current player.");
    private final String description;

    Error(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return description;
    }
}
