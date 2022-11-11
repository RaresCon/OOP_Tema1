package table_players;

import cards.*;
import fileio.CardInput;
import fileio.DecksInput;
import fileio.StartGameInput;

import java.util.ArrayList;
import java.util.List;

import static cards.CardType.*;

public class Player {
    private int mana = 1;
    private List<Card> deck = new ArrayList<>();
    private List<Card> cardsInHand = new ArrayList<>();
    private Hero playerHero;

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
                        deck.add(new Minion(cardInput, SENTINEL));
                        break;

                    case "Berserker":
                        deck.add(new Minion(cardInput, BERSERKER));
                        break;

                    case "Goliath":
                        deck.add(new Minion(cardInput, GOLIATH));
                        break;

                    case "Warden":
                        deck.add(new Minion(cardInput, WARDEN));
                        break;

                    case "The Ripper":
                        deck.add(new Minion(cardInput, RIPPER));
                        break;

                    case "Miraj":
                        deck.add(new Minion(cardInput, MIRAJ));
                        break;

                    case "The Cursed One":
                        deck.add(new Minion(cardInput, CURSED));
                        break;

                    case "Disciple":
                        deck.add(new Minion(cardInput, DISCIPLE));
                        break;

                    case "Firestorm":
                        deck.add(new Environment(cardInput, FIRESTORM));
                        break;

                    case "Winterfell":
                        deck.add(new Environment(cardInput, WINTERFELL));
                        break;

                    case "Heart Hound":
                        deck.add(new Environment(cardInput, HEARTHOUND));
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
                playerHero = new Hero(cardInput, ROYCE);
                break;

            case "Empress Thorina":
                playerHero = new Hero(cardInput, THORINA);
                break;

            case "King Mudface":
                playerHero = new Hero(cardInput, MUDFACE);
                break;

            case "General Kocioraw":
                playerHero = new Hero(cardInput, KOCIORAW);
                break;
        }
    }

    public Hero getPlayerHero() {
        return playerHero;
    }

    public List<Card> getCardsInHand() {
        return cardsInHand;
    }
}
