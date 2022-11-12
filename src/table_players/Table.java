package table_players;

import com.fasterxml.jackson.databind.node.ArrayNode;
import errors.Error;
import fileio.ActionsInput;
import fileio.GameInput;
import fileio.Input;
import fileio.StartGameInput;

import javax.annotation.processing.SupportedSourceVersion;
import java.util.*;

import static errors.Error.*;

public final class Table {
    private static Table instance = null;

    private Table() {}

    public static Table getTableInstance () {
        if (instance == null)
            instance = new Table();

        return instance;
    }

    public void playGame(Input input, ArrayNode output) {
        GameConfig gameConfig = new GameConfig();

        int numRows = 2;
        gameConfig.initRows(gameConfig.getPlayerOne(), gameConfig.getPlayerTwo(), numRows);

        for (GameInput gameInput : input.getGames()) {
            if (gameInput.getStartGame().getStartingPlayer() == 1)
                gameConfig.setActivePlayer(gameConfig.getPlayerOne());
            else
                gameConfig.setActivePlayer(gameConfig.getPlayerTwo());

            readyPlayersForGames(gameConfig, input, gameInput.getStartGame());
            System.out.println(gameConfig.getActivePlayer());

            for (ActionsInput action : gameInput.getActions())
                switch (action.getCommand()) {
                    case "endPlayerTurn":
                        break;
                    case "placeCard":
                        break;
                    case "cardUsesAttack":
                        break;
                    case "cardUsesAbility":
                        break;
                    case "useAttackHero":
                        break;
                    case "useHeroAbility":
                        break;
                    case "useEnvironmentCard":
                        break;
                    case "getCardsInHand":
                        break;
                    case "getPlayerDeck":
                        break;
                    case "getCardsOnTable":
                        break;
                    case "getPlayerTurn":
                        break;
                    case "getPlayerHero":
                        break;
                    case "getCardAtPosition":
                        break;
                    case "getPlayerMana":
                        break;
                    case "getEnvironmentCardsInHand":
                        break;
                    case "getFrozenCardsOnTable":
                        break;
                    case "getTotalGamesPlayed":
                        break;
                    case "getPlayerOneWins":
                        break;
                    case "getPlayerTwoWins":
                    default:
                        break;
                }
        }
    }

    public void readyPlayersForGames(GameConfig gameConfig, Input input, StartGameInput gameInput) {
        gameConfig.getPlayerOne().setDeck(input.getPlayerOneDecks(), gameInput.getPlayerOneDeckIdx());
        gameConfig.getPlayerTwo().setDeck(input.getPlayerTwoDecks(), gameInput.getPlayerTwoDeckIdx());
        gameConfig.getPlayerOne().setPlayerHero(gameInput.getPlayerOneHero());
        gameConfig.getPlayerTwo().setPlayerHero(gameInput.getPlayerTwoHero());

        Random rndSeed = new Random(gameInput.getShuffleSeed());
        Collections.shuffle(gameConfig.getPlayerOne().getDeck(), rndSeed);
        Collections.shuffle(gameConfig.getPlayerTwo().getDeck(), rndSeed);
    }
}
