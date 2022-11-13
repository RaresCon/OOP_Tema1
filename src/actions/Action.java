package actions;

import cards.Card;
import cards.Minion;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import table_players.GameConfig;

import java.util.List;

import static cards.CardType.*;
import static errors.Error.*;

public final class Action {
    public static void endPlayerTurn(GameConfig gameConfig) {
        gameConfig.setTurnsNum(gameConfig.getTurnsNum() + 1);

        for (int i = 0; i < 2; i++) {
            for (Minion minion : gameConfig.getPlayerOne().getPlayerRows().get(i))
                minion.setFrozenStat(false);

            for (Minion minion : gameConfig.getPlayerTwo().getPlayerRows().get(i))
                minion.setFrozenStat(false);
        }

        if (gameConfig.getActivePlayer().equals(gameConfig.getPlayerOne()))
            gameConfig.setActivePlayer(gameConfig.getPlayerTwo());
        else
            gameConfig.setActivePlayer(gameConfig.getPlayerOne());

        if (gameConfig.getTurnsNum() % 2 == 0) {
            if (gameConfig.getManaIncrement() != 10)
                gameConfig.setManaIncrement(gameConfig.getManaIncrement() + 1);;
            gameConfig.getPlayerOne().setMana(gameConfig.getPlayerOne().getMana() + gameConfig.getManaIncrement());
            gameConfig.getPlayerTwo().setMana(gameConfig.getPlayerTwo().getMana() + gameConfig.getManaIncrement());

            if (gameConfig.getPlayerOne().getDeck().size() != 0) {
                gameConfig.getPlayerOne().getCardsInHand().add(gameConfig.getPlayerOne().getDeck().get(0));
                gameConfig.getPlayerOne().getDeck().remove(0);
            }

            if (gameConfig.getPlayerTwo().getDeck().size() != 0) {
                gameConfig.getPlayerTwo().getCardsInHand().add(gameConfig.getPlayerTwo().getDeck().get(0));
                gameConfig.getPlayerTwo().getDeck().remove(0);
            }
        }
    }

    public static ObjectNode placeCard(ActionsInput action, GameConfig gameConfig) {
        ObjectNode error = JsonNodeFactory.instance.objectNode();

        if (action.getHandIdx() >= gameConfig.getActivePlayer().getCardsInHand().size()) {
            return null;
        }

        List<Card> currentPlayerCards = gameConfig.getActivePlayer().getCardsInHand();

        if (currentPlayerCards.get(action.getHandIdx()).getCardType() == ENVIRONMENT) {
            error.put("command", action.getCommand());
            error.put("handIdx", action.getHandIdx());
            error.put("error", PLACE_ENV_ERR.getDescription());

            return error;
        } else if (currentPlayerCards.get(action.getHandIdx()).getMana() > gameConfig.getActivePlayer().getMana()) {
            error.put("command", action.getCommand());
            error.put("handIdx", action.getHandIdx());
            error.put("error", MANA_ERR.getDescription());

            return error;
        }

        Minion currentCard = (Minion)currentPlayerCards.get(action.getHandIdx());
        switch (currentCard.getMinionType()) {
            case RIPPER:
            case MIRAJ:
            case GOLIATH:
            case WARDEN:
                if (gameConfig.getActivePlayer().getPlayerRows().get(1).size() == 5) {
                    error.put("command", action.getCommand());
                    error.put("handIdx", action.getHandIdx());
                    error.put("error", ROW_ERR.getDescription());

                    return error;
                }

                gameConfig.getActivePlayer().getPlayerRows().get(1).add(currentCard);
                gameConfig.getActivePlayer().getCardsInHand().remove(action.getHandIdx());
                gameConfig.getActivePlayer().setMana(gameConfig.getActivePlayer().getMana() - currentCard.getMana());
                break;

            case SENTINEL:
            case BERSERKER:
            case CURSED:
            case DISCIPLE:
                if (gameConfig.getActivePlayer().getPlayerRows().get(0).size() == 5) {
                    error.put("command", action.getCommand());
                    error.put("handIdx", action.getHandIdx());
                    error.put("error", ROW_ERR.getDescription());

                    return error;
                }
                gameConfig.getActivePlayer().getPlayerRows().get(0).add(currentCard);
                gameConfig.getActivePlayer().getCardsInHand().remove(action.getHandIdx());
                gameConfig.getActivePlayer().setMana(gameConfig.getActivePlayer().getMana() - currentCard.getMana());
                break;
        }

        return null;
    }
}
