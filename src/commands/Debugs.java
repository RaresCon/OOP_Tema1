package commands;

import cards.Card;
import cards.Minion;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import tableplayers.GameConfig;

import java.util.ArrayList;
import java.util.List;

import static cards.CardType.ENVIRONMENT;

public enum Debugs implements Command {
    GET_PLAYER_DECK {
        @Override
        public void executeCommand(ActionsInput action, GameConfig gameConfig, ArrayNode output) {
            ObjectNode cardsInDeck = JsonNodeFactory.instance.objectNode();

            cardsInDeck.put("command", action.getCommand());
            cardsInDeck.put("playerIdx", action.getPlayerIdx());

            if (action.getPlayerIdx() == 1) {
                cardsInDeck.replace("output", Utility.cardListObjectNode(gameConfig
                                                                 .getPlayerOne().getDeck()));
            } else {
                cardsInDeck.replace("output", Utility.cardListObjectNode(gameConfig
                                                                 .getPlayerTwo().getDeck()));
            }

            output.add(cardsInDeck);
        }
    },

    GET_CARDS_HAND {
        @Override
        public void executeCommand(ActionsInput action, GameConfig gameConfig, ArrayNode output) {
            ObjectNode cardsInHand = JsonNodeFactory.instance.objectNode();

            cardsInHand.put("command", action.getCommand());
            cardsInHand.put("playerIdx", action.getPlayerIdx());

            if (action.getPlayerIdx() == 1) {
                cardsInHand.replace("output", Utility.cardListObjectNode(gameConfig
                                                                 .getPlayerOne().getCardsInHand()));
            } else {
                cardsInHand.replace("output", Utility.cardListObjectNode(gameConfig
                                                                 .getPlayerTwo().getCardsInHand()));
            }

            output.add(cardsInHand);
        }
    },

    GET_CARDS_TABLE {
        @Override
        public void executeCommand(ActionsInput action, GameConfig gameConfig, ArrayNode output) {
            ObjectNode cardsOnTable = JsonNodeFactory.instance.objectNode();

            ArrayNode cardsOnRow1 = Utility.rowObjectNode(gameConfig.getPlayerTwo()
                                                                    .getPlayerRows().get(0));
            ArrayNode cardsOnRow2 = Utility.rowObjectNode(gameConfig.getPlayerTwo()
                                                                    .getPlayerRows().get(1));
            ArrayNode cardsOnRow3 = Utility.rowObjectNode(gameConfig.getPlayerOne()
                                                                    .getPlayerRows().get(1));
            ArrayNode cardsOnRow4 = Utility.rowObjectNode(gameConfig.getPlayerOne()
                                                                    .getPlayerRows().get(0));

            ArrayNode cardsOnRows = JsonNodeFactory.instance.arrayNode();
            cardsOnRows.add(cardsOnRow1);
            cardsOnRows.add(cardsOnRow2);
            cardsOnRows.add(cardsOnRow3);
            cardsOnRows.add(cardsOnRow4);

            cardsOnTable.put("command", action.getCommand());

            cardsOnTable.replace("output", cardsOnRows);

            output.add(cardsOnTable);
        }
    },

    GET_TURN {
        @Override
        public void executeCommand(ActionsInput action, GameConfig gameConfig, ArrayNode output) {
            ObjectNode playerTurn = JsonNodeFactory.instance.objectNode();

            playerTurn.put("command", action.getCommand());

            if (gameConfig.getActivePlayer().equals(gameConfig.getPlayerOne())) {
                playerTurn.put("output", 1);
            } else {
                playerTurn.put("output", 2);
            }

            output.add(playerTurn);
        }
    },

    GET_HERO {
        @Override
        public void executeCommand(ActionsInput action, GameConfig gameConfig, ArrayNode output) {
            ObjectNode playerHero = JsonNodeFactory.instance.objectNode();

            playerHero.put("command", action.getCommand());
            playerHero.put("playerIdx", action.getPlayerIdx());

            if (action.getPlayerIdx() == 1) {
                playerHero.replace("output",
                           Utility.cardObjectNode(gameConfig.getPlayerOne().getPlayerHero()));
            } else {
                playerHero.replace("output",
                           Utility.cardObjectNode(gameConfig.getPlayerTwo().getPlayerHero()));
            }

            output.add(playerHero);
        }
    },

    CARD_AT_POSITION {
        @Override
        public void executeCommand(ActionsInput action, GameConfig gameConfig, ArrayNode output) {
            ObjectNode cardAtPosition = JsonNodeFactory.instance.objectNode();

            cardAtPosition.put("command", action.getCommand());
            cardAtPosition.put("x", action.getX());
            cardAtPosition.put("y", action.getY());

            Minion cardOnTable = Utility.getCardFromTable(action.getX(), action.getY(), gameConfig);

            if (cardOnTable == null) {
                cardAtPosition.put("output", "No card available at that position.");
                output.add(cardAtPosition);
                return;
            }

            cardAtPosition.replace("output", Utility.cardObjectNode(cardOnTable));

            output.add(cardAtPosition);
        }
    },

    GET_PLAYER_MANA {
        @Override
        public void executeCommand(ActionsInput action, GameConfig gameConfig, ArrayNode output) {
            ObjectNode playerMana = JsonNodeFactory.instance.objectNode();

            playerMana.put("command", action.getCommand());
            playerMana.put("playerIdx", action.getPlayerIdx());

            if (action.getPlayerIdx() == 1) {
                playerMana.put("output", gameConfig.getPlayerOne().getMana());
            } else {
                playerMana.put("output", gameConfig.getPlayerTwo().getMana());
            }

            output.add(playerMana);
        }
    },

    ENV_CARDS_HAND {
        @Override
        public void executeCommand(ActionsInput action, GameConfig gameConfig, ArrayNode output) {
            ObjectNode envCardsInHand = JsonNodeFactory.instance.objectNode();

            envCardsInHand.put("command", action.getCommand());
            envCardsInHand.put("playerIdx", action.getPlayerIdx());

            List<Card> envCards = new ArrayList<>();

            if (action.getPlayerIdx() == 1) {
                for (Card cardOne : gameConfig.getPlayerOne().getCardsInHand()) {
                    if (cardOne.getCardType() == ENVIRONMENT) {
                        envCards.add(cardOne);
                    }
                }
            } else {
                for (Card cardTwo : gameConfig.getPlayerTwo().getCardsInHand()) {
                    if (cardTwo.getCardType() == ENVIRONMENT) {
                        envCards.add(cardTwo);
                    }
                }
            }

            envCardsInHand.replace("output", Utility.cardListObjectNode(envCards));

            output.add(envCardsInHand);
        }
    },

    FROZEN_CARDS_TABLE {
        @Override
        public void executeCommand(ActionsInput action, GameConfig gameConfig, ArrayNode output) {
            ObjectNode frznCardsOnTable = JsonNodeFactory.instance.objectNode();

            frznCardsOnTable.put("command", action.getCommand());
            List<Card> frznCards = new ArrayList<>();

            for (Minion minion : gameConfig.getPlayerTwo().getPlayerRows().get(0)) {
                if (minion.isFrozenStat() > 0) {
                    frznCards.add(minion);
                }
            }

            for (Minion minion : gameConfig.getPlayerTwo().getPlayerRows().get(1)) {
                if (minion.isFrozenStat() > 0) {
                    frznCards.add(minion);
                }
            }

            for (Minion minion : gameConfig.getPlayerOne().getPlayerRows().get(1)) {
                if (minion.isFrozenStat() > 0) {
                    frznCards.add(minion);
                }
            }

            for (Minion minion : gameConfig.getPlayerOne().getPlayerRows().get(0)) {
                if (minion.isFrozenStat() > 0) {
                    frznCards.add(minion);
                }
            }

            frznCardsOnTable.replace("output", Utility.cardListObjectNode(frznCards));

            output.add(frznCardsOnTable);
        }
    }


}
