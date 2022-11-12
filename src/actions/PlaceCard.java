package actions;

import cards.Card;
import cards.Minion;
import errors.Error;
import table_players.GameConfig;

import static cards.CardType.*;
import static errors.Error.*;


public class PlaceCard {
    private String command;
    private int cardIdx;
    private String error;

    public PlaceCard(String command, int cardIdx, Error error) {
        this.command = command;
        this.cardIdx = cardIdx;
        this.error = error.toString();
    }

    public static Error executeCommand(GameConfig gameConfig, int cardIdx) {
        Card cardToPlace = gameConfig.getActivePlayer().getCardsInHand().get(cardIdx);
        if (cardToPlace.getCardType() == MINION && gameConfig.getActivePlayer().getMana() >= cardToPlace.getMana()) {
            switch (((Minion)cardToPlace).getMinionType()) {
                case SENTINEL:
                case BERSERKER:
                case CURSED:
                case DISCIPLE:
                    //gameConfig.
                    return null;
                case GOLIATH:
                case WARDEN:
                case RIPPER:
                case MIRAJ:
                    return null;
            }
        } else if (gameConfig.getActivePlayer().getMana() < cardToPlace.getMana()) {

        }
        return null;
    }
}
