package commands;

import cards.Card;
import cards.Hero;
import cards.Minion;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import tableplayers.GameConfig;
import tableplayers.Player;

import java.util.List;

import static cards.CardType.HERO;
import static cards.CardType.MINION;

public final class Utility {
    private Utility() {
    }

    /**
     * function to convert a list of cards to an ArrayNode that can be added to a Json file
     * @param cards list of cards to be converted to ObjectNodes
     * @return ArrayNode of the cards to be added to Json files
     */
    public static ArrayNode cardListObjectNode(final List<Card> cards) {
        ArrayNode cardsConversion = JsonNodeFactory.instance.arrayNode();

        for (Card card : cards) {
            ObjectNode cardConversion = cardObjectNode(card);
            cardsConversion.add(cardConversion);
        }

        return cardsConversion;
    }

    /**
     * function to convert a row of cards to an ArrayNode that can be added to a Json file
     * @param row list of cards to be converted to ObjectNodes
     * @return ArrayNode of the row to be added to Json files
     */
    public static ArrayNode rowObjectNode(final List<Minion> row) {
        ArrayNode cardsConversion = JsonNodeFactory.instance.arrayNode();

        for (Minion card : row) {
            ObjectNode cardConversion = cardObjectNode(card);
            cardsConversion.add(cardConversion);
        }

        return cardsConversion;
    }

    /**
     * function to convert a single card to an ObjectNode
     * @param card card to be converted to ObjectNode
     * @return ObjectNode of the card to be added to Json files
     */
    public static ObjectNode cardObjectNode(final Card card) {
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
     * @param colors list of colors of a card to be converted to an ArrayNode
     * @return ArrayNode of the colors to be added to Json files
     */
    public static ArrayNode colorsObject(final List<String> colors) {
        ArrayNode colorsConversion = JsonNodeFactory.instance.arrayNode();

        for (String color : colors) {
            colorsConversion.add(color);
        }

        return colorsConversion;
    }

    /**
     *
     * @param x x coordinate of the card
     * @param y y coordinate of the card
     * @param gameConfig game configuration at the moment, including the cards on rows
     * @return Minion card on table (at x/y) that is requested by the caller
     */
    public static Minion getCardFromTable(final int x, final int y, final GameConfig gameConfig) {
        if (x < 2 && y < gameConfig.getPlayerTwo().getPlayerRows().get(x).size()) {
            return gameConfig.getPlayerTwo().getPlayerRows().get(x).get(y);
        } else if (x > 1 && y < gameConfig.getPlayerOne().getPlayerRows().get(-(x - 3)).size()) {
            return gameConfig.getPlayerOne().getPlayerRows().get(-(x - 3)).get(y);
        }

        return null;
    }

    /**
     * function to check if a player has any tank card on their first row
     * @param player player that is checked for tank cards on its first row
     * @return true if a tank card is present, false otherwise
     */
    public static boolean checkTankOnRows(final Player player) {
        for (Minion minion : player.getPlayerRows().get(1)) {
            if (minion.isTank()) {
                return true;
            }
        }
        return false;
    }
}
