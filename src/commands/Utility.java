package commands;

import cards.Card;
import cards.Hero;
import cards.Minion;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import tableplayers.GameConfig;

import java.util.ArrayList;
import java.util.List;

import static cards.CardType.ENVIRONMENT;
import static cards.CardType.HERO;
import static cards.CardType.MINION;

public final class Utility {
    private Utility() {
    }

    /**
     *
     * @param cards
     * @return
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
     *
     * @param row
     * @return
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
     *
     * @param card
     * @return
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
     * @param colors
     * @return
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
     * @param x
     * @param y
     * @param gameConfig
     * @return
     */
    public static Minion getCardFromTable(final int x, final int y, final GameConfig gameConfig) {
        if (x < 2 && y < gameConfig.getPlayerTwo().getPlayerRows().get(x).size()) {
            return gameConfig.getPlayerTwo().getPlayerRows().get(x).get(y);
        } else if (x > 1 && y < gameConfig.getPlayerOne().getPlayerRows().get(-(x - 3)).size()) {
            return gameConfig.getPlayerOne().getPlayerRows().get(-(x - 3)).get(y);
        }

        return null;
    }
}
