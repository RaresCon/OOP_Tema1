package errors;

import cards.Card;
import cards.Environment;
import cards.Minion;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fileio.ActionsInput;
import tableplayers.GameConfig;

import java.util.List;

import static cards.CardType.ENVIRONMENT;
import static cards.EnvironmentType.HEARTHOUND;
import static cards.MinionType.DISCIPLE;

public enum ErrorMessages {
    PLACE_ENV_ERR("Cannot place environment card on table."),
    USE_NONENV_ERR("Chosen card is not of type environment."),
    ROW_SPACE_ERR("Cannot place card on table since row is full."),
    OWN_ROW_ERR("Chosen row does not belong to the enemy."),
    STEAL_ERR("Cannot steal enemy card since the player's row is full."),
    MANA_ERR("Not enough mana to place card on table."),
    ENV_MANA_ERR("Not enough mana to use environment card."),
    ATTACKED_ERR("Attacked card does not belong to the enemy."),
    INACTIVE_CARD_ERR("Attacker card has already attacked this turn."),
    FROZEN_CARD("Attacker card is frozen."),
    TANK_ERR("Attacked card is not of type 'Tank'."),
    OWN_CARD_ERR("Attacked card does not belong to the current player."),
    HERO_MANA_ERR("Not enough mana to use hero's ability."),
    INACTIVE_HERO("Hero has already attacked this turn."),
    HERO_ENEMY_ROW_ERR("Selected row does not belong to the enemy."),
    HERO_FRIEND_ROW_ERR("Selected row does not belong to the current player.");
    private final String description;

    ErrorMessages(final String description) {
        this.description = description;
    }

    public static ObjectNode errorFactory(ActionsInput action, GameConfig gameConfig) {
        ObjectNode error = JsonNodeFactory.instance.objectNode();
        ObjectMapper mapper = new ObjectMapper();
        List<Card> currentPlayerCards;
        Minion cardAttacker;
        Minion cardAttacked;
        switch (action.getCommand()) {
            case "placeCard" -> {
                error.put("command", action.getCommand());
                error.put("handIdx", action.getHandIdx());
                currentPlayerCards = gameConfig.getActivePlayer().getCardsInHand();
                if (currentPlayerCards.get(action.getHandIdx()).getCardType() == ENVIRONMENT) {
                    error.put("error", PLACE_ENV_ERR.getDescription());

                    return error;
                } else if (currentPlayerCards.get(action.getHandIdx()).getMana()
                        > gameConfig.getActivePlayer().getMana()) {
                    error.put("error", MANA_ERR.getDescription());

                    return error;
                }
                Minion currentCardMin = (Minion) currentPlayerCards.get(action.getHandIdx());
                if (gameConfig.getActivePlayer().getPlayerRows()
                        .get(currentCardMin.getHomeRow()).size() == gameConfig.getMaxCardsOnRow()) {
                    error.put("error", ROW_SPACE_ERR.getDescription());

                    return error;
                }
            }
            case "useEnvironmentCard" -> {
                error.put("command", action.getCommand());
                error.put("handIdx", action.getHandIdx());
                error.put("affectedRow", action.getAffectedRow());
                currentPlayerCards = gameConfig.getActivePlayer().getCardsInHand();
                if (currentPlayerCards.get(action.getHandIdx()).getCardType() != ENVIRONMENT) {
                    error.put("error", USE_NONENV_ERR.getDescription());

                    return error;
                } else if (currentPlayerCards.get(action.getHandIdx()).getMana()
                        > gameConfig.getActivePlayer().getMana()) {
                    error.put("error", ENV_MANA_ERR.getDescription());

                    return error;
                }
                Environment currentCardEnv =
                        (Environment) currentPlayerCards.get(action.getHandIdx());
                if (gameConfig.getActivePlayer().equals(gameConfig.getPlayerOne())) {
                    if (action.getAffectedRow() > 1) {
                        error.put("error", OWN_ROW_ERR.getDescription());

                        return error;
                    }
                } else {
                    if (action.getAffectedRow() < 2) {
                        error.put("error", OWN_ROW_ERR.getDescription());

                        return error;
                    }
                }
                int affectedRowIdx;
                if (action.getAffectedRow() > 1) {
                    affectedRowIdx = -(action.getAffectedRow() - 3);
                } else {
                    affectedRowIdx = action.getAffectedRow();
                }
                if (currentCardEnv.getEnvironmentType().equals(HEARTHOUND)
                        && gameConfig.getActivePlayer().getPlayerRows().get(affectedRowIdx)
                        .size() == gameConfig.getMaxCardsOnRow()) {
                    error.put("error", STEAL_ERR.getDescription());

                    return error;
                }
            }
            case "cardUsesAttack" -> {
                error.put("command", "cardUsesAttack");
                error.replace("cardAttacker", mapper.valueToTree(action.getCardAttacker()));
                error.replace("cardAttacked", mapper.valueToTree(action.getCardAttacked()));
                int xDefence = action.getCardAttacked().getX();
                if ((gameConfig.getActivePlayer().equals(gameConfig.getPlayerOne())
                    && xDefence > 1)
                    || (gameConfig.getActivePlayer().equals(gameConfig.getPlayerTwo())
                    && xDefence < 2)) {
                    error.put("error", ATTACKED_ERR.getDescription());

                    return error;
                }
                cardAttacker = gameConfig.getCardFromTable(action.getCardAttacker().getX(),
                        action.getCardAttacker().getY(), gameConfig);
                cardAttacked = gameConfig.getCardFromTable(action.getCardAttacked().getX(),
                        action.getCardAttacked().getY(), gameConfig);
                if (cardAttacked == null || cardAttacker == null) {
                    error.put("error", "The attacker or the attacked doesn't exist on the table");

                    return error;
                }
                if (!cardAttacker.isActive()) {
                    error.put("error", INACTIVE_CARD_ERR.getDescription());

                    return error;
                } else if (cardAttacker.isFrozenStat() > 0) {
                    error.put("error", FROZEN_CARD.getDescription());

                    return error;
                }
                if (gameConfig.checkTankOnRows(gameConfig.getInactivePlayer())
                        && !(cardAttacked.isTank())) {
                    error.put("error", TANK_ERR.getDescription());

                    return error;
                }
            }
            // maybe better implementation?
            case "cardUsesAbility" -> {
                error.put("command", "cardUsesAbility");
                error.replace("cardAttacker", mapper.valueToTree(action.getCardAttacker()));
                error.replace("cardAttacked", mapper.valueToTree(action.getCardAttacked()));
                int xDefence = action.getCardAttacked().getX();
                cardAttacker = gameConfig.getCardFromTable(action.getCardAttacker().getX(),
                        action.getCardAttacker().getY(), gameConfig);
                cardAttacked = gameConfig.getCardFromTable(action.getCardAttacked().getX(),
                        action.getCardAttacked().getY(), gameConfig);
                if (cardAttacked == null || cardAttacker == null) {
                    error.put("error", "The attacker or the attacked doesn't exist on the table");

                    return error;
                }
                if (cardAttacker.isFrozenStat() > 0) {
                    error.put("error", FROZEN_CARD.getDescription());

                    return error;
                } else if (!cardAttacker.isActive()) {
                    error.put("error", INACTIVE_CARD_ERR.getDescription());

                    return error;
                }
                if (cardAttacker.getMinionType().equals(DISCIPLE)) {
                    if ((gameConfig.getActivePlayer().equals(gameConfig.getPlayerOne())
                        && xDefence < 2)
                        || (gameConfig.getActivePlayer().equals(gameConfig.getPlayerTwo())
                        && xDefence > 1)) {
                        error.put("error", OWN_CARD_ERR.getDescription());

                        return error;
                    }
                } else if ((gameConfig.getActivePlayer().equals(gameConfig.getPlayerOne())
                        && xDefence > 1)
                        || (gameConfig.getActivePlayer().equals(gameConfig.getPlayerTwo())
                        && xDefence < 2)) {
                    error.put("error", ATTACKED_ERR.getDescription());

                    return error;
                } else if (gameConfig.checkTankOnRows(gameConfig.getInactivePlayer())
                        && !(cardAttacked.isTank())) {
                    error.put("error", TANK_ERR.getDescription());

                    return error;
                }
            }
            case "useAttackHero" -> {
                error.put("command", "useAttackHero");
                error.replace("cardAttacker", mapper.valueToTree(action.getCardAttacker()));
                cardAttacker = gameConfig.getCardFromTable(action.getCardAttacker().getX(),
                        action.getCardAttacker().getY(), gameConfig);
                if (cardAttacker == null) {
                    return null;
                }
                if (cardAttacker.isFrozenStat() > 0) {
                    error.put("error", FROZEN_CARD.getDescription());

                    return error;
                } else if (!cardAttacker.isActive()) {
                    error.put("error", INACTIVE_CARD_ERR.getDescription());

                    return error;
                } else if (gameConfig.checkTankOnRows(gameConfig.getInactivePlayer())) {
                    error.put("error", TANK_ERR.getDescription());

                    return error;
                }
            }
            case "useHeroAbility" -> {
                error.put("command", "useHeroAbility");
                error.put("affectedRow", action.getAffectedRow());

                if (gameConfig.getActivePlayer().getPlayerHero().getMana()
                    > gameConfig.getActivePlayer().getMana()) {
                    error.put("error", HERO_MANA_ERR.getDescription());

                    return error;
                } else if (!gameConfig.getActivePlayer().getPlayerHero().isActive()) {
                    error.put("error", INACTIVE_HERO.getDescription());

                    return error;
                }

                switch (gameConfig.getActivePlayer().getPlayerHero().getHeroType()) {
                    case ROYCE, THORINA -> {
                        if (gameConfig.getActivePlayer().equals(gameConfig.getPlayerOne())
                                && action.getAffectedRow() > 1
                                || gameConfig.getActivePlayer().equals(gameConfig.getPlayerTwo())
                                && action.getAffectedRow() < 2) {
                            error.put("error", HERO_ENEMY_ROW_ERR.getDescription());

                            return error;
                        }
                    }
                    case MUDFACE, KOCIORAW -> {
                        if (gameConfig.getActivePlayer().equals(gameConfig.getPlayerOne())
                                && action.getAffectedRow() < 2
                                || gameConfig.getActivePlayer().equals(gameConfig.getPlayerTwo())
                                && action.getAffectedRow() > 1) {
                            error.put("error", HERO_FRIEND_ROW_ERR.getDescription());

                            return error;
                        }
                    }
                }
            }
        }
        return null;
    }


    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return description;
    }
}
