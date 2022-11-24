package tableplayers;

import cards.*;
import fileio.CardInput;
import fileio.DecksInput;

import java.util.ArrayList;
import java.util.List;

import static cards.CardType.ENVIRONMENT;
import static cards.CardType.HERO;
import static cards.CardType.MINION;
import static abilities.OnCardAbilities.*;
import static abilities.OnRowAbilities.*;

public class Player {
    private int mana = 1;
    private int gameWins = 0;
    private final List<Card> deck = new ArrayList<>();
    private final List<Card> cardsInHand = new ArrayList<>();
    private Hero playerHero;
    private final List<List<Minion>> playerRows = new ArrayList<>();

    /**
     * setter
     * @param mana current player mana
     */
    public void setMana(final int mana) {
        this.mana = mana;
    }

    /**
     * getter
     * @return current player mana
     */
    public int getMana() {
        return mana;
    }

    /**
     * function to set player's deck for a game into this class (deck)
     * @param decksInput all decks as input from the player
     * @param index deck index chosen by the player to be constructed in this class
     */
    public void setDeck(final DecksInput decksInput, final int index) {
            for (CardInput cardInput : decksInput.getDecks().get(index)) {
                switch (cardInput.getName()) {
                    case "Sentinel", "Berserker" ->
                            deck.add(new BasicMinion(cardInput, MINION, false, 0));
                    case "Goliath", "Warden" ->
                            deck.add(new BasicMinion(cardInput, MINION, true, 1));
                    case "The Cursed One" ->
                            deck.add(new AbilityMinion(cardInput, MINION, false, 0,
                                                       -1, SHAPESHIFT));
                    case "Disciple" ->
                            deck.add(new AbilityMinion(cardInput, MINION, false, 0,
                                                       1, GODSPLAN));
                    case "The Ripper" ->
                            deck.add(new AbilityMinion(cardInput, MINION, false, 1,
                                                       -1, WEAKKNEES));
                    case "Miraj" ->
                            deck.add(new AbilityMinion(cardInput, MINION, false, 1,
                                                       -1, SKYJACK));
                    case "Firestorm" ->
                            deck.add(new Environment(cardInput, ENVIRONMENT, DAMAGE_CARDS));
                    case "Winterfell" ->
                            deck.add(new Environment(cardInput, ENVIRONMENT, FREEZE_CARDS));
                    case "Heart Hound" ->
                            deck.add(new Environment(cardInput, ENVIRONMENT, STEAL_CARD));
                    default -> {
                    }
                }
            }
    }

    /**
     * getter
     * @return player's deck
     */
    public List<Card> getDeck() {
        return deck;
    }

    /**
     * setter for player's hero
     * @param cardInput hero input as CardInput to be constructed to this class
     */
    public void setPlayerHero(final CardInput cardInput) {
        switch (cardInput.getName()) {
            case "Lord Royce" -> playerHero = new Hero(cardInput, HERO, -1, SUBZERO);
            case "Empress Thorina" -> playerHero = new Hero(cardInput, HERO, -1, LOWBLOW);
            case "King Mudface" -> playerHero = new Hero(cardInput, HERO, 1, EARTHBORN);
            case "General Kocioraw" -> playerHero = new Hero(cardInput, HERO, 1, BLOODTHIRST);
            default -> {
            }
        }
    }

    /**
     * getter
     * @return player's hero
     */
    public Hero getPlayerHero() {
        return playerHero;
    }

    /**
     * getter
     * @return cards present in player's hand (added from deck)
     */
    public List<Card> getCardsInHand() {
        return cardsInHand;
    }

    /**
     * getter
     * @return number of games won by a player
     */
    public int getGameWins() {
        return gameWins;
    }

    /**
     * setter
     * @param gameWins number of games won by a player
     */
    public void setGameWins(final int gameWins) {
        this.gameWins = gameWins;
    }

    /**
     * getter
     * @return player's rows
     */
    public List<List<Minion>> getPlayerRows() {
        return playerRows;
    }

}
