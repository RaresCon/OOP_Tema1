package table.players;

import cards.Card;
import cards.Environment;
import cards.Hero;
import cards.Minion;
import fileio.CardInput;
import fileio.DecksInput;

import java.util.ArrayList;
import java.util.List;

import static cards.CardType.ENVIRONMENT;
import static cards.CardType.HERO;
import static cards.CardType.MINION;
import static cards.EnvironmentType.FIRESTORM;
import static cards.EnvironmentType.HEARTHOUND;
import static cards.EnvironmentType.WINTERFELL;
import static cards.HeroType.KOCIORAW;
import static cards.HeroType.MUDFACE;
import static cards.HeroType.ROYCE;
import static cards.HeroType.THORINA;
import static cards.MinionType.BERSERKER;
import static cards.MinionType.CURSED;
import static cards.MinionType.DISCIPLE;
import static cards.MinionType.GOLIATH;
import static cards.MinionType.MIRAJ;
import static cards.MinionType.RIPPER;
import static cards.MinionType.SENTINEL;
import static cards.MinionType.WARDEN;

public class Player {
    private int mana = 1;
    private int gameWins = 0;
    private List<Card> deck = new ArrayList<>();
    private List<Card> cardsInHand = new ArrayList<>();
    private Hero playerHero;
    private List<List<Minion>> playerRows = new ArrayList<>();

    /**
     *
     * @param mana
     */
    public void setMana(final int mana) {
        this.mana = mana;
    }

    /**
     *
     * @return
     */
    public int getMana() {
        return mana;
    }

    /**
     *
     * @param decksInput
     * @param index
     */
    public void setDeck(final DecksInput decksInput, final int index) {
            for (CardInput cardInput : decksInput.getDecks().get(index)) {
                switch (cardInput.getName()) {
                    case "Sentinel" -> deck.add(new Minion(cardInput, MINION, SENTINEL));
                    case "Berserker" -> deck.add(new Minion(cardInput, MINION, BERSERKER));
                    case "Goliath" -> deck.add(new Minion(cardInput, MINION, GOLIATH));
                    case "Warden" -> deck.add(new Minion(cardInput, MINION, WARDEN));
                    case "The Ripper" -> deck.add(new Minion(cardInput, MINION, RIPPER));
                    case "Miraj" -> deck.add(new Minion(cardInput, MINION, MIRAJ));
                    case "The Cursed One" -> deck.add(new Minion(cardInput, MINION, CURSED));
                    case "Disciple" -> deck.add(new Minion(cardInput, MINION, DISCIPLE));
                    case "Firestorm" ->
                            deck.add(new Environment(cardInput, ENVIRONMENT, FIRESTORM));
                    case "Winterfell" ->
                            deck.add(new Environment(cardInput, ENVIRONMENT, WINTERFELL));
                    case "Heart Hound" ->
                            deck.add(new Environment(cardInput, ENVIRONMENT, HEARTHOUND));
                    default -> {
                    }
                }
            }
    }

    /**
     *
     * @return
     */
    public List<Card> getDeck() {
        return deck;
    }

    /**
     *
     * @param cardInput
     */
    public void setPlayerHero(final CardInput cardInput) {
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
            default:
                break;
        }
    }

    /**
     *
     * @return
     */
    public Hero getPlayerHero() {
        return playerHero;
    }

    /**
     *
     * @return
     */
    public List<Card> getCardsInHand() {
        return cardsInHand;
    }

    /**
     *
     * @return
     */
    public int getGameWins() {
        return gameWins;
    }

    /**
     *
     * @param gameWins
     */
    public void setGameWins(final int gameWins) {
        this.gameWins = gameWins;
    }

    /**
     *
     * @return
     */
    public List<List<Minion>> getPlayerRows() {
        return playerRows;
    }

}
