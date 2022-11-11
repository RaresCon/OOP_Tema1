package table_players;

import actions.Action;
import cards.Card;
import fileio.ActionsInput;
import fileio.GameInput;
import fileio.Input;
import fileio.StartGameInput;

import java.util.*;

public class Table {
    private static Table instance = null;
    private static int playerOneWins = 0;
    private static int playerTwoWins = 0;
    private static int gamesPlayed = 0;


    private Table() {}

    public static Table getTableInstance () {
        if (instance == null)
            instance = new Table();

        return instance;
    }

    public void playGame(Input input) {
        Player playerOne = new Player();
        Player playerTwo = new Player();
        List<List<Card>> playerOneRows = new ArrayList<>();
        List<List<Card>> playerTwoRows = new ArrayList<>();

        int numRows = 2;
        for (int i = 0; i < numRows; i++) {
            playerOneRows.add(new ArrayList<>());
            playerTwoRows.add(new ArrayList<>());
        }

        for (GameInput gameInput : input.getGames()) {
            int manaIncrement = 1;
            List<Action> actionList = Action.loadActions(gameInput.getActions());
            int activePlayer = gameInput.getStartGame().getStartingPlayer();
            int actionIterator = 0;

            readyPlayersForGames(playerOne, playerTwo, input, gameInput.getStartGame());
            //System.out.println(playerOne.getDeck());
            //System.out.println(playerTwo.getDeck());

            while (playerOne.getPlayerHero().getHealthStat() != 0 && playerTwo.getPlayerHero().getHealthStat() != 0) {
                playerOne.setMana(manaIncrement);
                playerTwo.setMana(manaIncrement);


            }
        }
    }

    public void readyPlayersForGames(Player playerOne, Player playerTwo, Input input, StartGameInput gameInput) {
        playerOne.setDeck(input.getPlayerOneDecks(), gameInput.getPlayerOneDeckIdx());
        playerTwo.setDeck(input.getPlayerTwoDecks(), gameInput.getPlayerTwoDeckIdx());
        playerOne.setPlayerHero(gameInput.getPlayerOneHero());
        playerTwo.setPlayerHero(gameInput.getPlayerTwoHero());

        Random rndSeed = new Random(gameInput.getShuffleSeed());
        Collections.shuffle(playerOne.getDeck(), rndSeed);
        Collections.shuffle(playerTwo.getDeck(), rndSeed);
    }
}
