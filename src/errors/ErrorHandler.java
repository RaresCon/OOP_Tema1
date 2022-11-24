package errors;

import abilities.OnRowAbilities;
import cards.Card;
import cards.Environment;
import cards.Minion;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import commands.Utility;
import fileio.ActionsInput;
import tableplayers.GameConfig;
import tableplayers.GameConstants;

import java.util.List;

import static cards.CardType.ENVIRONMENT;

public enum ErrorHandler {
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

    ErrorHandler(final String description) {
        this.description = description;
    }

    public enum ErrorFactory {
        PLACE_CARD_ERR {
            @Override
            public ObjectNode checkError(final ActionsInput action, final GameConfig gameConfig) {
                ObjectNode error = JsonNodeFactory.instance.objectNode();

                error.put("command", action.getCommand());
                error.put("handIdx", action.getHandIdx());

                List<Card> currentPlayerCards = gameConfig.getActivePlayer().getCardsInHand();

                if (action.getHandIdx() >= currentPlayerCards.size()) {
                    error.put("error", "There is no card in hand at that index");

                    return error;
                } else if (currentPlayerCards.get(action.getHandIdx()).getCardType()
                        == ENVIRONMENT) {
                    error.put("error", PLACE_ENV_ERR.getDescription());

                    return error;
                } else if (currentPlayerCards.get(action.getHandIdx()).getMana()
                        > gameConfig.getActivePlayer().getMana()) {
                    error.put("error", MANA_ERR.getDescription());

                    return error;
                }

                Minion currentCardMin = (Minion) currentPlayerCards.get(action.getHandIdx());

                if (gameConfig.getActivePlayer().getPlayerRows()
                                                .get(currentCardMin.getHomeRow()).size()
                                                == GameConstants.MaxCardsOnRow) {
                    error.put("error", ROW_SPACE_ERR.getDescription());

                    return error;
                }
                return null;
            }
        },

        USE_ENV_CARD_ERR {
            @Override
            public ObjectNode checkError(final ActionsInput action, final GameConfig gameConfig) {
                ObjectNode error = JsonNodeFactory.instance.objectNode();

                error.put("command", action.getCommand());
                error.put("handIdx", action.getHandIdx());
                error.put("affectedRow", action.getAffectedRow());

                List<Card> currentPlayerCards = gameConfig.getActivePlayer().getCardsInHand();

                if (action.getHandIdx() >= gameConfig.getActivePlayer().getCardsInHand().size()) {
                    error.put("error", "There is no card in hand at that index");
                    return error;
                }

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

                int affectedRowIdx;
                if (action.getAffectedRow() > 1) {
                    affectedRowIdx = -(action.getAffectedRow() - 3);
                } else {
                    affectedRowIdx = action.getAffectedRow();
                }

                if ((gameConfig.getActivePlayer().equals(gameConfig.getPlayerOne())
                        && action.getAffectedRow() > 1)
                        || (gameConfig.getActivePlayer().equals(gameConfig.getPlayerTwo())
                        && action.getAffectedRow() < 2)) {
                    error.put("error", OWN_ROW_ERR.getDescription());

                    return error;
                } else if (currentCardEnv.getEnvAbility().equals(OnRowAbilities.STEAL_CARD)
                        && gameConfig.getAttackedRow(affectedRowIdx).size()
                        == GameConstants.MaxCardsOnRow) {
                    error.put("error", STEAL_ERR.getDescription());

                    return error;
                }
                return null;
            }
        },

        USE_CARD_ATTACK_ERR {
            @Override
            public ObjectNode checkError(final ActionsInput action, final GameConfig gameConfig) {
                ObjectNode error = JsonNodeFactory.instance.objectNode();
                ObjectMapper mapper = new ObjectMapper();

                error.put("command", "cardUsesAttack");
                error.replace("cardAttacker", mapper.valueToTree(action.getCardAttacker()));
                error.replace("cardAttacked", mapper.valueToTree(action.getCardAttacked()));
                int xAttacked = action.getCardAttacked().getX();

                if ((gameConfig.getActivePlayer().equals(gameConfig.getPlayerOne())
                        && xAttacked > 1)
                        || (gameConfig.getActivePlayer().equals(gameConfig.getPlayerTwo())
                        && xAttacked < 2)) {
                    error.put("error", ATTACKED_ERR.getDescription());

                    return error;
                }

                Minion cardAttacker = Utility.getCardFromTable(action.getCardAttacker().getX(),
                        action.getCardAttacker().getY(), gameConfig);
                Minion cardAttacked = Utility.getCardFromTable(action.getCardAttacked().getX(),
                        action.getCardAttacked().getY(), gameConfig);
                if (cardAttacked == null || cardAttacker == null) {
                    error.put("error", "The attacker or the attacked doesn't exist on the table");

                    return error;
                } else if (!cardAttacker.isActive()) {
                    error.put("error", INACTIVE_CARD_ERR.getDescription());

                    return error;
                } else if (cardAttacker.isFrozenStat() > 0) {
                    error.put("error", FROZEN_CARD.getDescription());

                    return error;
                } else if (Utility.checkTankOnRows(gameConfig.getInactivePlayer())
                        && !(cardAttacked.isTank())) {
                    error.put("error", TANK_ERR.getDescription());

                    return error;
                }

                return null;
            }
        },

        USE_CARD_ABILITY_ERR {
            @Override
            public ObjectNode checkError(final ActionsInput action, final GameConfig gameConfig) {
                ObjectNode error = JsonNodeFactory.instance.objectNode();
                ObjectMapper mapper = new ObjectMapper();

                error.put("command", "cardUsesAbility");
                error.replace("cardAttacker", mapper.valueToTree(action.getCardAttacker()));
                error.replace("cardAttacked", mapper.valueToTree(action.getCardAttacked()));
                int xAttacked = action.getCardAttacked().getX();

                Minion cardAttacker = Utility.getCardFromTable(action.getCardAttacker().getX(),
                        action.getCardAttacker().getY(), gameConfig);
                Minion cardAttacked = Utility.getCardFromTable(action.getCardAttacked().getX(),
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

                switch (cardAttacker.getOnFriendAbility()) {
                    case 1 -> {
                        if ((gameConfig.getActivePlayer().equals(gameConfig.getPlayerOne())
                                && xAttacked < 2)
                                || (gameConfig.getActivePlayer().equals(gameConfig.getPlayerTwo())
                                && xAttacked > 1)) {
                            error.put("error", OWN_CARD_ERR.getDescription());

                            return error;
                        }
                    }
                    case -1 -> {
                        if ((gameConfig.getActivePlayer().equals(gameConfig.getPlayerOne())
                                && xAttacked > 1)
                                || (gameConfig.getActivePlayer().equals(gameConfig.getPlayerTwo())
                                && xAttacked < 2)) {
                            error.put("error", ATTACKED_ERR.getDescription());

                            return error;
                        } else if (Utility.checkTankOnRows(gameConfig.getInactivePlayer())
                                && !(cardAttacked.isTank())) {
                            error.put("error", TANK_ERR.getDescription());

                            return error;
                        }
                    }
                    default -> {
                    }
                }

                return null;
            }
        },

        ATTACK_HERO_ERR {
            @Override
            public ObjectNode checkError(final ActionsInput action, final GameConfig gameConfig) {
                ObjectNode error = JsonNodeFactory.instance.objectNode();
                ObjectMapper mapper = new ObjectMapper();

                error.put("command", "useAttackHero");
                error.replace("cardAttacker", mapper.valueToTree(action.getCardAttacker()));
                Minion cardAttacker = Utility.getCardFromTable(action.getCardAttacker().getX(),
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
                } else if (Utility.checkTankOnRows(gameConfig.getInactivePlayer())) {
                    error.put("error", TANK_ERR.getDescription());

                    return error;
                }

                return null;
            }
        },

        USE_HERO_ABILITY_ERR {
            @Override
            public ObjectNode checkError(final ActionsInput action, final GameConfig gameConfig) {
                ObjectNode error = JsonNodeFactory.instance.objectNode();

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

                switch (gameConfig.getActivePlayer().getPlayerHero().getOnFriendAbility()) {
                    case -1 -> {
                        if (gameConfig.getActivePlayer().equals(gameConfig.getPlayerOne())
                                && action.getAffectedRow() > 1
                                || gameConfig.getActivePlayer().equals(gameConfig.getPlayerTwo())
                                && action.getAffectedRow() < 2) {
                            error.put("error", HERO_ENEMY_ROW_ERR.getDescription());

                            return error;
                        }
                    }
                    case 1 -> {
                        if (gameConfig.getActivePlayer().equals(gameConfig.getPlayerOne())
                                && action.getAffectedRow() < 2
                                || gameConfig.getActivePlayer().equals(gameConfig.getPlayerTwo())
                                && action.getAffectedRow() > 1) {
                            error.put("error", HERO_FRIEND_ROW_ERR.getDescription());

                            return error;
                        }
                    }
                    default -> {
                    }
                }
                return null;
            }
        };

        /**
         *
         * @param action action to be checked for errors in its input from the player
         * @param gameConfig game configuration at the moment (cards on table and players)
         * @return ObjectNode generated if an error is present in the input, null if otherwise
         */
        public abstract ObjectNode checkError(ActionsInput action, GameConfig gameConfig);
    }

    public String getDescription() {
        return description;
    }
}
