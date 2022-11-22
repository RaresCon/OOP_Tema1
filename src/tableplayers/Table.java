package tableplayers;

import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.ActionsInput;
import fileio.GameInput;
import fileio.Input;
import fileio.StartGameInput;
import commands.Debugs;
import commands.Actions;
import commands.Stats;

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

            for (ActionsInput action : gameInput.getActions()) {
                switch (action.getCommand()) {
                    case "endPlayerTurn" ->
                            Actions.END_TURN.executeCommand(null, gameConfig, null);
                    case "placeCard" ->
                            Actions.PLACE_CARD.executeCommand(action, gameConfig, output);
                    case "cardUsesAttack" ->
                            Actions.USE_CARD_ATTACK.executeCommand(action, gameConfig, output);
                    case "cardUsesAbility" ->
                            Actions.USE_CARD_ABILITY.executeCommand(action, gameConfig, output);
                    case "useAttackHero" ->
                            Actions.ATTACK_HERO.executeCommand(action, gameConfig, output);
                    case "useHeroAbility" ->
                            Actions.USE_HERO_ABILITY.executeCommand(action, gameConfig, output);
                    case "useEnvironmentCard" ->
                            Actions.USE_ENVIRONMENT.executeCommand(action, gameConfig, output);
                    case "getCardsInHand" ->
                            Debugs.GET_CARDS_HAND.executeCommand(action, gameConfig, output);
                    case "getPlayerDeck" ->
                            Debugs.GET_PLAYER_DECK.executeCommand(action, gameConfig, output);
                    case "getCardsOnTable" ->
                            Debugs.GET_CARDS_TABLE.executeCommand(action, gameConfig, output);
                    case "getPlayerTurn" ->
                            Debugs.GET_TURN.executeCommand(action, gameConfig, output);
                    case "getPlayerHero" ->
                            Debugs.GET_HERO.executeCommand(action, gameConfig, output);
                    case "getCardAtPosition" ->
                            Debugs.CARD_AT_POSITION.executeCommand(action, gameConfig, output);
                    case "getPlayerMana" ->
                            Debugs.GET_PLAYER_MANA.executeCommand(action, gameConfig, output);
                    case "getEnvironmentCardsInHand" ->
                            Debugs.ENV_CARDS_HAND.executeCommand(action, gameConfig, output);
                    case "getFrozenCardsOnTable" ->
                            Debugs.FROZEN_CARDS_TABLE.executeCommand(action, gameConfig, output);
                    case "getTotalGamesPlayed" ->
                            Stats.GET_TOTAL_GAMES.executeCommand(action, gameConfig, output);
                    case "getPlayerOneWins", "getPlayerTwoWins" ->
                            Stats.GET_WINS.executeCommand(action, gameConfig, output);
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

        gameConfig.getPlayerOne().getCardsInHand()
                                 .add(gameConfig.getPlayerOne().getDeck().remove(0));

        gameConfig.getPlayerTwo().getCardsInHand()
                                 .add(gameConfig.getPlayerTwo().getDeck().remove(0));

        for (int i = 0; i < 2; i++) {
            gameConfig.getPlayerOne().getPlayerRows().get(i).clear();
            gameConfig.getPlayerTwo().getPlayerRows().get(i).clear();
        }
    }
}
