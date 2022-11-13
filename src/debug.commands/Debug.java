package debug.commands;

import cards.Card;
import cards.Hero;
import cards.Minion;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import table.players.GameConfig;

import java.util.ArrayList;
import java.util.List;

import static cards.CardType.ENVIRONMENT;
import static cards.CardType.HERO;
import static cards.CardType.MINION;

public final class Debug {
    private Debug() {

    }

    /**
     *
     * @param action
     * @param gameConfig
     * @return
     */
    public static ObjectNode getPlayerDeck(final ActionsInput action, final GameConfig gameConfig) {
        ObjectNode cardsInDeck = JsonNodeFactory.instance.objectNode();

        cardsInDeck.put("command", action.getCommand());
        cardsInDeck.put("playerIdx", action.getPlayerIdx());

        if (action.getPlayerIdx() == 1) {
            cardsInDeck.replace("output", cardListObjectNode(gameConfig.getPlayerOne()
                                                                                   .getDeck()));
        } else {
            cardsInDeck.replace("output", cardListObjectNode(gameConfig.getPlayerTwo()
                                                                                   .getDeck()));
        }

        return cardsInDeck;
    }

    /**
     *
     * @param action
     * @param gameConfig
     * @return
     */
    public static ObjectNode getCardsInHand(final ActionsInput action,
                                            final GameConfig gameConfig) {
        ObjectNode cardsInHand = JsonNodeFactory.instance.objectNode();

        cardsInHand.put("command", action.getCommand());
        cardsInHand.put("playerIdx", action.getPlayerIdx());

        if (action.getPlayerIdx() == 1) {
            cardsInHand.replace("output", cardListObjectNode(gameConfig.getPlayerOne()
                                                                         .getCardsInHand()));
        } else {
            cardsInHand.replace("output", cardListObjectNode(gameConfig.getPlayerTwo()
                                                                         .getCardsInHand()));
        }

        return cardsInHand;
    }

    /**
     *
     * @param action
     * @param gameConfig
     * @return
     */
    public static ObjectNode getCardsOnTable(final ActionsInput action,
                                             final GameConfig gameConfig) {
        ObjectNode cardsOnTable = JsonNodeFactory.instance.objectNode();

        ArrayNode cardsOnRow1 = rowObjectNode(gameConfig.getPlayerTwo().getPlayerRows().get(0));
        ArrayNode cardsOnRow2 = rowObjectNode(gameConfig.getPlayerTwo().getPlayerRows().get(1));
        ArrayNode cardsOnRow3 = rowObjectNode(gameConfig.getPlayerOne().getPlayerRows().get(1));
        ArrayNode cardsOnRow4 = rowObjectNode(gameConfig.getPlayerOne().getPlayerRows().get(0));

        ArrayNode cardsOnRows = JsonNodeFactory.instance.arrayNode();
        cardsOnRows.add(cardsOnRow1);
        cardsOnRows.add(cardsOnRow2);
        cardsOnRows.add(cardsOnRow3);
        cardsOnRows.add(cardsOnRow4);

        cardsOnTable.put("command", action.getCommand());

        cardsOnTable.replace("output", cardsOnRows);

        return cardsOnTable;
    }

    /**
     *
     * @param action
     * @param gameConfig
     * @return
     */
    public static ObjectNode getPlayerTurn(final ActionsInput action, final GameConfig gameConfig) {
        ObjectNode playerTurn = JsonNodeFactory.instance.objectNode();

        playerTurn.put("command", action.getCommand());

        if (gameConfig.getActivePlayer().equals(gameConfig.getPlayerOne())) {
            playerTurn.put("output", 1);
        } else {
            playerTurn.put("output", 2);
        }

        return playerTurn;
    }

    /**
     *
     * @param action
     * @param gameConfig
     * @return
     */
    public static ObjectNode getPlayerHero(final ActionsInput action, final GameConfig gameConfig) {
        ObjectNode playerHero = JsonNodeFactory.instance.objectNode();

        playerHero.put("command", action.getCommand());
        playerHero.put("playerIdx", action.getPlayerIdx());

        if (action.getPlayerIdx() == 1) {
            playerHero.replace("output", cardObjectNode(gameConfig.getPlayerOne()
                                                                              .getPlayerHero()));
        } else {
            playerHero.replace("output", cardObjectNode(gameConfig.getPlayerTwo()
                                                                              .getPlayerHero()));
        }

        return playerHero;
    }

    /**
     *
     * @param action
     * @param gameConfig
     * @return
     */
    public static ObjectNode getCardAtPosition(final ActionsInput action,
                                               final GameConfig gameConfig) {
        ObjectNode cardAtPosition = JsonNodeFactory.instance.objectNode();

        cardAtPosition.put("command", action.getCommand());
        cardAtPosition.put("x", action.getX());
        cardAtPosition.put("y", action.getY());

        Minion cardOnTable = getCardFromTable(action.getX(), action.getY(), gameConfig);

        if (cardOnTable == null) {
            cardAtPosition.put("output", "No card available at that position.");
            return cardAtPosition;
        }

        cardAtPosition.replace("output", cardObjectNode(cardOnTable));

        return cardAtPosition;
    }

    /**
     *
     * @param action
     * @param gameConfig
     * @return
     */
    public static ObjectNode getPlayerMana(final ActionsInput action, final GameConfig gameConfig) {
        ObjectNode playerMana = JsonNodeFactory.instance.objectNode();

        playerMana.put("command", action.getCommand());
        playerMana.put("playerIdx", action.getPlayerIdx());

        if (action.getPlayerIdx() == 1) {
            playerMana.put("output", gameConfig.getPlayerOne().getMana());
        } else {
            playerMana.put("output", gameConfig.getPlayerTwo().getMana());
        }

        return playerMana;
    }

    /**
     *
     * @param action
     * @param gameConfig
     * @return
     */
    public static ObjectNode getEnvCardsInHand(final ActionsInput action,
                                               final GameConfig gameConfig) {
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

        envCardsInHand.replace("output", cardListObjectNode(envCards));

        return envCardsInHand;
    }

    /**
     *
     * @param action
     * @param gameConfig
     * @return
     */
    public static ObjectNode getFrznCardsOnTable(final ActionsInput action,
                                                 final GameConfig gameConfig) {
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

        frznCardsOnTable.replace("output", cardListObjectNode(frznCards));

        return frznCardsOnTable;
    }

    /**
     *
     * @param cards
     * @return
     */
    private static ArrayNode cardListObjectNode(final List<Card> cards) {
        ArrayNode cardsConversion = JsonNodeFactory.instance.arrayNode();

        for (Card card : cards) {
            ObjectNode cardConversion = cardObjectNode(card);
            cardsConversion.add(cardConversion);
        }

        return cardsConversion;
    }

    /**
     *
     * @param row
     * @return
     */
    private static ArrayNode rowObjectNode(final List<Minion> row) {
        ArrayNode cardsConversion = JsonNodeFactory.instance.arrayNode();

        for (Minion card : row) {
            ObjectNode cardConversion = cardObjectNode(card);
            cardsConversion.add(cardConversion);
        }

        return cardsConversion;
    }

    /**
     *
     * @param card
     * @return
     */
    private static ObjectNode cardObjectNode(final Card card) {
        ObjectNode cardConversion = JsonNodeFactory.instance.objectNode();

        cardConversion.put("mana", card.getMana());
        if (card.getCardType() == MINION) {
            cardConversion.put("attackDamage", ((Minion) card).getAttackStat());
            cardConversion.put("health", ((Minion) card).getHealthStat());
        }
        cardConversion.put("description", card.getDescription());
        cardConversion.replace("colors", colorsObject(card.getColors()));
        cardConversion.put("name", card.getName());
        if (card.getCardType() == HERO) {
            cardConversion.put("health", ((Hero) card).getHealthStat());
        }

        return cardConversion;
    }

    /**
     *
     * @param colors
     * @return
     */
    private static ArrayNode colorsObject(final List<String> colors) {
        ArrayNode colorsConversion = JsonNodeFactory.instance.arrayNode();

        for (String color : colors) {
            colorsConversion.add(color);
        }

        return colorsConversion;
    }

    /**
     *
     * @param x
     * @param y
     * @param gameConfig
     * @return
     */
    private static Minion getCardFromTable(final int x, final int y, final GameConfig gameConfig) {
        if (x < 2 && y < gameConfig.getPlayerTwo().getPlayerRows().get(x).size()) {
            return gameConfig.getPlayerTwo().getPlayerRows().get(x).get(y);
        } else if (x > 1 && y < gameConfig.getPlayerOne().getPlayerRows().get(-(x - 3)).size()) {
            return gameConfig.getPlayerOne().getPlayerRows().get(-(x - 3)).get(y);
        }

        return null;
    }
}
