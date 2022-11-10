package table_players;

import cards.Card;
import fileio.GameInput;
import fileio.Input;
import fileio.StartGameInput;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Table {
    private static Table instance = null;
    private int numRows = 2;
    private int rndSeed;

    private Table() {}

    public static Table getTableInstance () {
        if (instance == null)
            instance = new Table();

        return instance;
    }

    public void loagInputToGame(Input input) {
        Player playerOne = new Player();
        Player playerTwo = new Player();
        List<List<Card>> playerOneRows = new ArrayList<>();
        List<List<Card>> playerTwoRows = new ArrayList<>();

        for (int i = 0; i < numRows; i++) {
            playerOneRows.add(new ArrayList<>());
            playerTwoRows.add(new ArrayList<>());
        }

        for (GameInput gameInput : input.getGames()) {
            readyPlayersForGames(playerOne, playerTwo, input, gameInput.getStartGame());

            Random rndSeed = new Random(gameInput.getStartGame().getShuffleSeed());
            Collections.shuffle(playerOne.getDeck(), rndSeed);
            Collections.shuffle(playerTwo.getDeck(), rndSeed);

            System.out.println(playerOne.getDeck());
            System.out.println(playerTwo.getDeck());
        }
    }

    public void readyPlayersForGames(Player playerOne, Player playerTwo, Input input, StartGameInput gameInput) {
        playerOne.setDeck(input.getPlayerOneDecks(), gameInput.getPlayerOneDeckIdx());
        playerTwo.setDeck(input.getPlayerTwoDecks(), gameInput.getPlayerTwoDeckIdx());
        playerOne.setPlayerHero(gameInput.getPlayerOneHero());
        playerTwo.setPlayerHero(gameInput.getPlayerTwoHero());
    }
}
