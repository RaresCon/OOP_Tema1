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

        gameConfig.initRows(gameConfig.getPlayerOne(), gameConfig.getPlayerTwo(), 2);

        for (GameInput gameInput : input.getGames()) {
            gameConfig.setTurnsNum(0);

            if (gameInput.getStartGame().getStartingPlayer() == 1)
                gameConfig.setActivePlayer(gameConfig.getPlayerOne());
            else
                gameConfig.setActivePlayer(gameConfig.getPlayerTwo());
            readyPlayersForGames(gameConfig, input, gameInput.getStartGame());

            for (ActionsInput action : gameInput.getActions())
                switch (action.getCommand()) {
                    // ACTIONS
                    case "endPlayerTurn":
                        Action.endPlayerTurn(gameConfig);
                        break;
                    case "placeCard":
                        ObjectNode possibleError = Action.placeCard(action, gameConfig);
                        if (possibleError != null)
                            output.add(possibleError);
                        break;
                    case "cardUsesAttack":
                        possibleError = Action.cardUsesAttack(action, gameConfig);
                        if (possibleError != null)
                            output.add(possibleError);
                        break;
                    case "cardUsesAbility":
                        possibleError = Action.cardUsesAbility(action, gameConfig);
                        if (possibleError != null)
                            output.add(possibleError);
                        break;
                    case "useAttackHero":
                        possibleError = Action.useAttackHero(action, gameConfig);
                        if (possibleError != null)
                            output.add(possibleError);
                        break;
                    case "useHeroAbility":
                        possibleError = Action.useHeroAbility(action, gameConfig);
                        if (possibleError != null)
                            output.add(possibleError);
                        break;
                    case "useEnvironmentCard":
                        possibleError = Action.useEnvironment(action, gameConfig);
                        if (possibleError != null)
                            output.add(possibleError);
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
        gameConfig.getPlayerOne().getDeck().clear();
        gameConfig.getPlayerTwo().getDeck().clear();
        gameConfig.getPlayerOne().setDeck(input.getPlayerOneDecks(), gameInput.getPlayerOneDeckIdx());
        gameConfig.getPlayerTwo().setDeck(input.getPlayerTwoDecks(), gameInput.getPlayerTwoDeckIdx());
        gameConfig.getPlayerOne().setPlayerHero(gameInput.getPlayerOneHero());
        gameConfig.getPlayerTwo().setPlayerHero(gameInput.getPlayerTwoHero());

        Collections.shuffle(gameConfig.getPlayerOne().getDeck(), new Random(gameInput.getShuffleSeed()));
        Collections.shuffle(gameConfig.getPlayerTwo().getDeck(), new Random(gameInput.getShuffleSeed()));

        gameConfig.setManaIncrement(1);
        gameConfig.getPlayerOne().setMana(1);
        gameConfig.getPlayerTwo().setMana(1);
        gameConfig.getPlayerOne().getCardsInHand().clear();
        gameConfig.getPlayerTwo().getCardsInHand().clear();
        gameConfig.getPlayerOne().getPlayerHero().setActive(true);
        gameConfig.getPlayerTwo().getPlayerHero().setActive(true);

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
