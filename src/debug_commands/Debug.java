package debug_commands;

import cards.Card;
import cards.Hero;
import cards.Minion;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import table_players.GameConfig;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static cards.CardType.*;

public final class Debug {
    public static ObjectNode getPlayerDeck(ActionsInput action, GameConfig gameConfig) {
        ObjectNode cardsInDeck = JsonNodeFactory.instance.objectNode();

        cardsInDeck.put("command", action.getCommand());
        cardsInDeck.put("playerIdx", action.getPlayerIdx());

        if (action.getPlayerIdx() == 1)
            cardsInDeck.replace("output", cardListObjectNode(gameConfig.getPlayerOne().getDeck()));
        else
            cardsInDeck.replace("output", cardListObjectNode(gameConfig.getPlayerTwo().getDeck()));

        return cardsInDeck;
    }

    public static ObjectNode getCardsInHand(ActionsInput action, GameConfig gameConfig) {
        ObjectNode cardsInHand = JsonNodeFactory.instance.objectNode();

        cardsInHand.put("command", action.getCommand());
        cardsInHand.put("playerIdx", action.getPlayerIdx());

        if (action.getPlayerIdx() == 1)
            cardsInHand.replace("output", cardListObjectNode(gameConfig.getPlayerOne().getCardsInHand()));
        else
            cardsInHand.replace("output", cardListObjectNode(gameConfig.getPlayerTwo().getCardsInHand()));

        return cardsInHand;
    }

    //DE REZOLVAT OUTPUT CARDS ON TABLE
    public static ObjectNode getCardsOnTable(ActionsInput action, GameConfig gameConfig) {
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

    public static ObjectNode getPlayerTurn(ActionsInput action, GameConfig gameConfig) {
        ObjectNode playerTurn = JsonNodeFactory.instance.objectNode();

        playerTurn.put("command", action.getCommand());

        if(gameConfig.getActivePlayer().equals(gameConfig.getPlayerOne()))
            playerTurn.put("output", 1);
        else
            playerTurn.put("output", 2);

        return playerTurn;
    }

    public static ObjectNode getPlayerHero(ActionsInput action, GameConfig gameConfig) {
        ObjectNode playerHero = JsonNodeFactory.instance.objectNode();

        playerHero.put("command", action.getCommand());
        playerHero.put("playerIdx", action.getPlayerIdx());

        if (action.getPlayerIdx() == 1)
            playerHero.replace("output", cardObjectNode(gameConfig.getPlayerOne().getPlayerHero()));
        else
            playerHero.replace("output", cardObjectNode(gameConfig.getPlayerTwo().getPlayerHero()));

        return playerHero;
    }

    public static ObjectNode getCardAtPosition(ActionsInput action, GameConfig gameConfig) {
        ObjectNode cardAtPosition = JsonNodeFactory.instance.objectNode();

        cardAtPosition.put("command", action.getCommand());

        if (action.getY() < 0 || action.getY() > 3 || action.getX() < 0 || action.getX() > 4) {
            cardAtPosition.put("output", "No card at that position.");
            return cardAtPosition;
        }

        List<List<Minion>> playerOneRows = gameConfig.getPlayerOne().getPlayerRows();
        List<List<Minion>> playerTwoRows = gameConfig.getPlayerTwo().getPlayerRows();

        if (action.getY() == 0 && action.getX() < playerTwoRows.get(0).size())
            cardAtPosition.replace("output", cardObjectNode(playerTwoRows.get(0).get(action.getX())));
        if (action.getY() == 1 && action.getX() < playerTwoRows.get(1).size())
            cardAtPosition.replace("output", cardObjectNode(playerTwoRows.get(1).get(action.getX())));
        if (action.getY() == 2 && action.getX() < playerOneRows.get(2).size())
            cardAtPosition.replace("output", cardObjectNode(playerOneRows.get(2).get(action.getX())));
        if (action.getY() == 3 && action.getX() < playerOneRows.get(3).size())
            cardAtPosition.replace("output", cardObjectNode(playerOneRows.get(3).get(action.getX())));

        return cardAtPosition;
    }

    public static ObjectNode getPlayerMana(ActionsInput action, GameConfig gameConfig) {
        ObjectNode playerMana = JsonNodeFactory.instance.objectNode();

        playerMana.put("command", action.getCommand());
        playerMana.put("playerIdx", action.getPlayerIdx());

        if (action.getPlayerIdx() == 1)
            playerMana.put("output", gameConfig.getPlayerOne().getMana());
        else
            playerMana.put("output", gameConfig.getPlayerTwo().getMana());

        return playerMana;
    }

    public static ObjectNode getEnvCardsInHand(ActionsInput action, GameConfig gameConfig){
        ObjectNode envCardsInHand = JsonNodeFactory.instance.objectNode();

        envCardsInHand.put("command", action.getCommand());
        envCardsInHand.put("playerIdx", action.getPlayerIdx());

        List<Card> envCards = new ArrayList<>();

        if (action.getPlayerIdx() == 1)
            for (Card card : gameConfig.getPlayerOne().getCardsInHand())
                if (card.getCardType() == ENVIRONMENT)
                    envCards.add(card);

        envCardsInHand.replace("output", cardListObjectNode(envCards));

        return envCardsInHand;
    }

    public static ObjectNode getFrznCardsOnTable(ActionsInput action, GameConfig gameConfig) {
        ObjectNode frznCardsOnTable = JsonNodeFactory.instance.objectNode();

        frznCardsOnTable.put("command", action.getCommand());
        List<Card> frznCards = new ArrayList<>();

        for (Minion minion : gameConfig.getPlayerTwo().getPlayerRows().get(0))
            if (minion.isFrozenStat())
                frznCards.add(minion);

        for (Minion minion : gameConfig.getPlayerTwo().getPlayerRows().get(1))
            if (minion.isFrozenStat())
                frznCards.add(minion);

        for (Minion minion : gameConfig.getPlayerOne().getPlayerRows().get(1))
            if (minion.isFrozenStat())
                frznCards.add(minion);

        for (Minion minion : gameConfig.getPlayerOne().getPlayerRows().get(0))
            if (minion.isFrozenStat())
                frznCards.add(minion);

        frznCardsOnTable.replace("output", cardListObjectNode(frznCards));

        return frznCardsOnTable;
    }

    private static ArrayNode cardListObjectNode(List<Card> cards) {
        ArrayNode cardsConversion = JsonNodeFactory.instance.arrayNode();

        for (Card card : cards) {
            ObjectNode cardConversion = cardObjectNode(card);
            cardsConversion.add(cardConversion);
        }

        return cardsConversion;
    }

    private static ArrayNode rowObjectNode(List<Minion> row) {
        ArrayNode cardsConversion = JsonNodeFactory.instance.arrayNode();

        for (Minion card : row) {
            ObjectNode cardConversion = cardObjectNode(card);
            cardsConversion.add(cardConversion);
        }

        return cardsConversion;
    }

    private static ObjectNode cardObjectNode(Card card) {
        ObjectNode cardConversion = JsonNodeFactory.instance.objectNode();

        cardConversion.put("mana", card.getMana());
        if (card.getCardType() == MINION) {
            cardConversion.put("attackDamage", ((Minion) card).getAttackStat());
            cardConversion.put("health", ((Minion) card).getHealthStat());
        }
        cardConversion.put("description", card.getDescription());
        cardConversion.replace("colors", colorsObject(card.getColors()));
        cardConversion.put("name", card.getName());
        if (card.getCardType() == HERO)
            cardConversion.put("health", ((Hero)card).getHealthStat());

        return cardConversion;
    }

    private static ArrayNode colorsObject(List<String> colors) {
        ArrayNode colorsConversion = JsonNodeFactory.instance.arrayNode();

        for (String color : colors)
            colorsConversion.add(color);

        return colorsConversion;
    }
}
