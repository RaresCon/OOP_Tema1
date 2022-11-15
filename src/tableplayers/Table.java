package tableplayers;

import actions.Action;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import debugcommands.Debug;
import fileio.ActionsInput;
import fileio.GameInput;
import fileio.Input;
import fileio.StartGameInput;
import statscommands.Stats;

import java.util.Collections;
import java.util.Random;

public final class Table {
    private static Table instance = null;

    private Table() {
    }

    /**
     *
     * @return
     */
    public static Table getTableInstance() {
        if (instance == null) {
            instance = new Table();
        }

        return instance;
    }

    /**
     *
     * @param input
     * @param output
     */
    public void playGame(final Input input, final ArrayNode output) {
        GameConfig gameConfig = new GameConfig();

        gameConfig.initRows(2);

        for (GameInput gameInput : input.getGames()) {
            gameConfig.setTurnsNum(0);

            if (gameInput.getStartGame().getStartingPlayer() == 1) {
                gameConfig.setActivePlayer(gameConfig.getPlayerOne());
                gameConfig.setInactivePlayer(gameConfig.getPlayerTwo());
            } else {
                gameConfig.setActivePlayer(gameConfig.getPlayerTwo());
                gameConfig.setInactivePlayer(gameConfig.getPlayerOne());
            }
            readyPlayersForGames(gameConfig, input, gameInput.getStartGame());

            ObjectNode possibleError;
            for (ActionsInput action : gameInput.getActions()) {
                switch (action.getCommand()) {
                    // ACTIONS
                    case "endPlayerTurn" -> Action.endPlayerTurn(gameConfig);
                    case "placeCard" -> {
                        possibleError = Action.placeCard(action, gameConfig);
                        if (possibleError != null) {
                            output.add(possibleError);
                        }
                    }
                    case "cardUsesAttack" -> {
                        possibleError = Action.cardUsesAttack(action, gameConfig);
                        if (possibleError != null) {
                            output.add(possibleError);
                        }
                    }
                    case "cardUsesAbility" -> {
                        possibleError = Action.cardUsesAbility(action, gameConfig);
                        if (possibleError != null) {
                            output.add(possibleError);
                        }
                    }
                    case "useAttackHero" -> {
                        possibleError = Action.useAttackHero(action, gameConfig);
                        if (possibleError != null) {
                            output.add(possibleError);
                        }
                    }
                    case "useHeroAbility" -> {
                        possibleError = Action.useHeroAbility(action, gameConfig);
                        if (possibleError != null) {
                            output.add(possibleError);
                        }
                    }
                    case "useEnvironmentCard" -> {
                        possibleError = Action.useEnvironment(action, gameConfig);
                        if (possibleError != null) {
                            output.add(possibleError);
                        }
                    }
                    // DEBUG
                    case "getCardsInHand" -> output.add(Debug.getCardsInHand(action, gameConfig));
                    case "getPlayerDeck" -> output.add(Debug.getPlayerDeck(action, gameConfig));
                    case "getCardsOnTable" -> output.add(Debug.getCardsOnTable(action, gameConfig));
                    case "getPlayerTurn" -> output.add(Debug.getPlayerTurn(action, gameConfig));
                    case "getPlayerHero" -> output.add(Debug.getPlayerHero(action, gameConfig));
                    case "getCardAtPosition" ->
                            output.add(Debug.getCardAtPosition(action, gameConfig));
                    case "getPlayerMana" -> output.add(Debug.getPlayerMana(action, gameConfig));
                    case "getEnvironmentCardsInHand" ->
                            output.add(Debug.getEnvCardsInHand(action, gameConfig));
                    case "getFrozenCardsOnTable" ->
                            output.add(Debug.getFrznCardsOnTable(action, gameConfig));

                    // STATS
                    case "getTotalGamesPlayed" ->
                            output.add(Stats.getTotalGames(action, gameConfig));
                    case "getPlayerOneWins", "getPlayerTwoWins" ->
                            output.add(Stats.getPlayerWins(action, gameConfig));
                    default -> {
                    }
                }
            }
        }
}

    /**
     *
     * @param gameConfig
     * @param input
     * @param gameInput
     */
    private void readyPlayersForGames(final GameConfig gameConfig, final Input input,
                                      final StartGameInput gameInput) {
        gameConfig.getPlayerOne().getDeck().clear();
        gameConfig.getPlayerTwo().getDeck().clear();
        gameConfig.getPlayerOne().setDeck(input.getPlayerOneDecks(),
                                          gameInput.getPlayerOneDeckIdx());
        gameConfig.getPlayerTwo().setDeck(input.getPlayerTwoDecks(),
                                          gameInput.getPlayerTwoDeckIdx());
        gameConfig.getPlayerOne().setPlayerHero(gameInput.getPlayerOneHero());
        gameConfig.getPlayerTwo().setPlayerHero(gameInput.getPlayerTwoHero());

        Collections.shuffle(gameConfig.getPlayerOne().getDeck(),
                            new Random(gameInput.getShuffleSeed()));
        Collections.shuffle(gameConfig.getPlayerTwo().getDeck(),
                            new Random(gameInput.getShuffleSeed()));

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

        for (int i = 0; i < 2; i++) {
            gameConfig.getPlayerOne().getPlayerRows().get(i).clear();
            gameConfig.getPlayerTwo().getPlayerRows().get(i).clear();
        }
    }
}
