package table_players;

import cards.*;
import fileio.CardInput;
import fileio.DecksInput;
import fileio.StartGameInput;

import java.util.ArrayList;
import java.util.List;

import static cards.CardType.*;
import static cards.MinionType.*;
import static cards.HeroType.*;
import static cards.EnvironmentType.*;

public class Player {
    private int mana = 1;
    private int gameWins = 0;
    private List<Card> deck = new ArrayList<>();
    private List<Card> cardsInHand = new ArrayList<>();
    private Hero playerHero;
    private List<List<Minion>> playerRows = new ArrayList<>();

    public void setMana(int mana) {
        this.mana = mana;
    }

    public int getMana() {
        return mana;
    }

    public void setDeck(DecksInput decksInput, int index) {
            for (CardInput cardInput : decksInput.getDecks().get(index)) {
                switch (cardInput.getName()) {
                    case "Sentinel":
                        deck.add(new Minion(cardInput, MINION, SENTINEL));
                        break;

                    case "Berserker":
                        deck.add(new Minion(cardInput, MINION, BERSERKER));
                        break;

                    case "Goliath":
                        deck.add(new Minion(cardInput, MINION, GOLIATH));
                        break;

                    case "Warden":
                        deck.add(new Minion(cardInput, MINION, WARDEN));
                        break;

                    case "The Ripper":
                        deck.add(new Minion(cardInput, MINION, RIPPER));
                        break;

                    case "Miraj":
                        deck.add(new Minion(cardInput, MINION, MIRAJ));
                        break;

                    case "The Cursed One":
                        deck.add(new Minion(cardInput, MINION, CURSED));
                        break;

                    case "Disciple":
                        deck.add(new Minion(cardInput, MINION, DISCIPLE));
                        break;

                    case "Firestorm":
                        deck.add(new Environment(cardInput, ENVIRONMENT, FIRESTORM));
                        break;

                    case "Winterfell":
                        deck.add(new Environment(cardInput, ENVIRONMENT, WINTERFELL));
                        break;

                    case "Heart Hound":
                        deck.add(new Environment(cardInput, ENVIRONMENT, HEARTHOUND));
                        break;
                }
            }
    }

    public List<Card> getDeck() {
        return deck;
    }

    public void setPlayerHero(CardInput cardInput) {
        switch (cardInput.getName()) {
            case "Lord Royce":
                playerHero = new Hero(cardInput, HERO, ROYCE);
                break;

            case "Empress Thorina":
                playerHero = new Hero(cardInput, HERO, THORINA);
                break;

            case "King Mudface":
                playerHero = new Hero(cardInput, HERO, MUDFACE);
                break;

            case "General Kocioraw":
                playerHero = new Hero(cardInput, HERO, KOCIORAW);
                break;
        }
    }

    public Hero getPlayerHero() {
        return playerHero;
    }

    public List<Card> getCardsInHand() {
        return cardsInHand;
    }

    public int getGameWins() {
        return gameWins;
    }

    public void setGameWins(int gameWins) {
        this.gameWins = gameWins;
    }

    public List<List<Minion>> getPlayerRows() {
        return playerRows;
    }

    public void setPlayerRows(List<List<Minion>> playerRows) {
        this.playerRows = playerRows;
    }
}
