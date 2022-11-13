package table_players;

import actions.Action;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import debug_commands.Debug;
import fileio.ActionsInput;
import fileio.GameInput;
import fileio.Input;
import fileio.StartGameInput;
import stats_commands.Stats;

import java.util.*;

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
            gameConfig.setTurnsNum(0);
            System.out.println("NEW MATCH " + gameConfig.getPlayerTwo().getMana());

            if (gameInput.getStartGame().getStartingPlayer() == 1)
                gameConfig.setActivePlayer(gameConfig.getPlayerOne());
            else
                gameConfig.setActivePlayer(gameConfig.getPlayerTwo());

            readyPlayersForGames(gameConfig, input, gameInput.getStartGame());
            System.out.println("NEW MATCH AFTER EQUAL " + gameConfig.getPlayerTwo().getMana());

            for (ActionsInput action : gameInput.getActions())
                switch (action.getCommand()) {
                        // ACTIONS
                    case "endPlayerTurn":
                        Action.endPlayerTurn(gameConfig);
                        break;
                    case "placeCard":
                        ObjectNode possibleError = Action.placeCard(action, gameConfig);
                        if (possibleError != null)
                            output.add(Action.placeCard(action, gameConfig));
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
                        // DEBUG
                    case "getCardsInHand":
                        output.add(Debug.getCardsInHand(action, gameConfig));
                        break;
                    case "getPlayerDeck":
                        output.add(Debug.getPlayerDeck(action, gameConfig));
                        break;
                    case "getCardsOnTable":
                        output.add(Debug.getCardsOnTable(action, gameConfig));
                        break;
                    case "getPlayerTurn":
                        output.add(Debug.getPlayerTurn(action, gameConfig));
                        break;
                    case "getPlayerHero":
                        output.add(Debug.getPlayerHero(action, gameConfig));
                        break;
                    case "getCardAtPosition":
                        output.add(Debug.getCardAtPosition(action, gameConfig));
                        break;
                    case "getPlayerMana":
                        output.add(Debug.getPlayerMana(action, gameConfig));
                        break;
                    case "getEnvironmentCardsInHand":
                        output.add(Debug.getEnvCardsInHand(action, gameConfig));
                        break;
                    case "getFrozenCardsOnTable":
                        output.add(Debug.getFrznCardsOnTable(action, gameConfig));
                        // STATS
                        break;
                    case "getTotalGamesPlayed":
                        output.add(Stats.getTotalGames(action, gameConfig));
                        break;
                    case "getPlayerOneWins":
                    case "getPlayerTwoWins":
                        output.add(Stats.getPlayerWins(action, gameConfig));
                        break;
                    default:
                        break;
                }
        }
    }

    private void readyPlayersForGames(GameConfig gameConfig, Input input, StartGameInput gameInput) {
        gameConfig.getPlayerOne().setDeck(input.getPlayerOneDecks(), gameInput.getPlayerOneDeckIdx());
        gameConfig.getPlayerTwo().setDeck(input.getPlayerTwoDecks(), gameInput.getPlayerTwoDeckIdx());
        gameConfig.getPlayerOne().setPlayerHero(gameInput.getPlayerOneHero());
        gameConfig.getPlayerTwo().setPlayerHero(gameInput.getPlayerTwoHero());

        Collections.shuffle(gameConfig.getPlayerOne().getDeck(), new Random(gameInput.getShuffleSeed()));
        Collections.shuffle(gameConfig.getPlayerTwo().getDeck(), new Random(gameInput.getShuffleSeed()));

        gameConfig.getPlayerOne().getCardsInHand().clear();
        gameConfig.getPlayerTwo().getCardsInHand().clear();
        gameConfig.getPlayerOne().setMana(1);
        gameConfig.getPlayerTwo().setMana(1);
        gameConfig.getPlayerOne().setGameWins(0);
        gameConfig.getPlayerTwo().setGameWins(0);

        gameConfig.getPlayerOne().getCardsInHand().add(gameConfig.getPlayerOne().getDeck().get(0));
        gameConfig.getPlayerOne().getDeck().remove(0);

        gameConfig.getPlayerTwo().getCardsInHand().add(gameConfig.getPlayerTwo().getDeck().get(0));
        gameConfig.getPlayerTwo().getDeck().remove(0);

        for (int i = 0; i < 2; i ++) {
            gameConfig.getPlayerOne().getPlayerRows().get(i).clear();
            gameConfig.getPlayerTwo().getPlayerRows().get(i).clear();
        }
    }
}
