package actions;

import cards.Card;
import cards.Environment;
import cards.Minion;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import errors.ErrorMessages;
import fileio.ActionsInput;
import tableplayers.GameConfig;
import tableplayers.Player;

import java.util.List;

import static cards.MinionType.DISCIPLE;
import static errors.ErrorMessages.HERO_ENEMY_ROW_ERR;
import static errors.ErrorMessages.HERO_FRIEND_ROW_ERR;
import static errors.ErrorMessages.HERO_MANA_ERR;
import static errors.ErrorMessages.INACTIVE_HERO;


public final class Action {
    private Action() {
    }

    /**
     *
     * @param gameConfig
     */
    public static void endPlayerTurn(final GameConfig gameConfig) {
        gameConfig.setTurnsNum(gameConfig.getTurnsNum() + 1);
        gameConfig.getPlayerOne().getPlayerHero().setActive(true);
        gameConfig.getPlayerTwo().getPlayerHero().setActive(true);

        for (int i = 0; i < 2; i++) {
            for (Minion minion : gameConfig.getPlayerOne().getPlayerRows().get(i)) {
                if (minion.isFrozenStat() > 0) {
                    minion.setFrozenStat(minion.isFrozenStat() - 1);
                }
                minion.setActive(true);
            }

            for (Minion minion : gameConfig.getPlayerTwo().getPlayerRows().get(i)) {
                if (minion.isFrozenStat() > 0) {
                    minion.setFrozenStat(minion.isFrozenStat() - 1);
                }
                minion.setActive(true);
            }
        }

        if (gameConfig.getActivePlayer().equals(gameConfig.getPlayerOne())) {
            gameConfig.setActivePlayer(gameConfig.getPlayerTwo());
            gameConfig.setInactivePlayer(gameConfig.getPlayerOne());
        } else {
            gameConfig.setActivePlayer(gameConfig.getPlayerOne());
            gameConfig.setInactivePlayer(gameConfig.getPlayerTwo());
        }

        if (gameConfig.getTurnsNum() % 2 == 0) {
            if (gameConfig.getManaIncrement() != gameConfig.getManaMaxIncrement()) {
                gameConfig.setManaIncrement(gameConfig.getManaIncrement() + 1);
            }
            gameConfig.getPlayerOne().setMana(gameConfig.getPlayerOne().getMana()
                                              + gameConfig.getManaIncrement());
            gameConfig.getPlayerTwo().setMana(gameConfig.getPlayerTwo().getMana()
                                              + gameConfig.getManaIncrement());

            if (gameConfig.getPlayerOne().getDeck().size() != 0) {
                gameConfig.getPlayerOne().getCardsInHand().add(gameConfig.getPlayerOne()
                                                               .getDeck().get(0));
                gameConfig.getPlayerOne().getDeck().remove(0);
            }

            if (gameConfig.getPlayerTwo().getDeck().size() != 0) {
                gameConfig.getPlayerTwo().getCardsInHand().add(gameConfig.getPlayerTwo()
                                                               .getDeck().get(0));
                gameConfig.getPlayerTwo().getDeck().remove(0);
            }
        }
    }

    /**
     *
     * @param action
     * @param gameConfig
     * @return
     */
    public static ObjectNode placeCard(final ActionsInput action, final GameConfig gameConfig) {
        if (action.getHandIdx() >= gameConfig.getActivePlayer().getCardsInHand().size()) {
            return null;
        }

        ObjectNode error = ErrorMessages.errorFactory(action, gameConfig);

        if (error != null)
            return error;

        List<Card> currentPlayerCards = gameConfig.getActivePlayer().getCardsInHand();
        Minion currentCard = (Minion) currentPlayerCards.get(action.getHandIdx());

        currentCard.setActive(true);
        gameConfig.getActivePlayer().getPlayerRows().get(currentCard.getHomeRow()).add(currentCard);
        gameConfig.getActivePlayer().setMana(gameConfig.getActivePlayer().getMana()
                                             - currentCard.getMana());
        gameConfig.getActivePlayer().getCardsInHand().remove(action.getHandIdx());
        return null;
    }

    /**
     *
     * @param action
     * @param gameConfig
     * @return
     */
    public static ObjectNode useEnvironment(final ActionsInput action,
                                            final GameConfig gameConfig) {
        if (action.getHandIdx() >= gameConfig.getActivePlayer().getCardsInHand().size()) {
            return null;
        }

        ObjectNode error = ErrorMessages.errorFactory(action, gameConfig);

        if (error != null) {
            return error;
        }

        List<Card> currentPlayerCards = gameConfig.getActivePlayer().getCardsInHand();

        Environment currentCard = (Environment) currentPlayerCards.get(action.getHandIdx());
        List<Minion> affectedRow = gameConfig.getAttackedRow(gameConfig, action.getAffectedRow());

        currentCard.environmentAbility(affectedRow);

        gameConfig.getActivePlayer().getCardsInHand().remove(action.getHandIdx());
        gameConfig.getActivePlayer().setMana(gameConfig.getActivePlayer().getMana()
                                             - currentCard.getMana());
        return null;
    }

    /**
     *
     * @param action
     * @param gameConfig
     * @return
     */
    public static ObjectNode cardUsesAttack(final ActionsInput action,
                                            final GameConfig gameConfig) {

        ObjectNode error = ErrorMessages.errorFactory(action, gameConfig);

        if (error != null)
            return error;

        Minion cardAttacker = gameConfig.getCardFromTable(action.getCardAttacker().getX(),
                              action.getCardAttacker().getY(), gameConfig);
        Minion cardAttacked = gameConfig.getCardFromTable(action.getCardAttacked().getX(),
                              action.getCardAttacked().getY(), gameConfig);

        int xDefence = action.getCardAttacked().getX();

        if (gameConfig.getActivePlayer().equals(gameConfig.getPlayerOne())) {
            cardAttacker.minionAttack(cardAttacked);
            if (cardAttacked.getHealthStat() <= 0) {
                gameConfig.getPlayerTwo().getPlayerRows().get(xDefence).remove(cardAttacked);
            }
        } else {
            cardAttacker.minionAttack(cardAttacked);
            if (cardAttacked.getHealthStat() <= 0) {
                gameConfig.getPlayerOne().getPlayerRows().get(-(xDefence - 3)).remove(cardAttacked);
            }
        }

        cardAttacker.setActive(false);
        return null;
    }

    /**
     *
     * @param action
     * @param gameConfig
     * @return
     */
    public static ObjectNode cardUsesAbility(final ActionsInput action,
                                             final GameConfig gameConfig) {
        ObjectNode error = ErrorMessages.errorFactory(action, gameConfig);

        if (error != null)
            return error;

        Minion cardAttacker = gameConfig.getCardFromTable(action.getCardAttacker().getX(),
                              action.getCardAttacker().getY(), gameConfig);
        Minion cardAttacked = gameConfig.getCardFromTable(action.getCardAttacked().getX(),
                              action.getCardAttacked().getY(), gameConfig);

        int xDefence = action.getCardAttacked().getX();

        if (cardAttacker.getMinionType().equals(DISCIPLE)) {
            cardAttacked.setHealthStat(cardAttacked.getHealthStat() + 2);
        } else if (gameConfig.getActivePlayer().equals(gameConfig.getPlayerOne())) {
            cardAttacker.minionAbility(cardAttacked);
            if (cardAttacked.getHealthStat() <= 0) {
                gameConfig.getPlayerTwo().getPlayerRows().get(xDefence).remove(cardAttacked);
            }
        } else {
            cardAttacker.minionAbility(cardAttacked);
            if (cardAttacked.getHealthStat() <= 0) {
                gameConfig.getPlayerOne().getPlayerRows().get(-(xDefence - 3)).remove(cardAttacked);
            }
        }

        cardAttacker.setActive(false);
        return null;
    }

    /**
     *
     * @param action
     * @param gameConfig
     * @return
     */
    public static ObjectNode useAttackHero(final ActionsInput action, final GameConfig gameConfig) {
        ObjectNode error = ErrorMessages.errorFactory(action, gameConfig);

        if (error != null)
            return error;

        Minion cardAttacker = gameConfig.getCardFromTable(action.getCardAttacker().getX(),
                action.getCardAttacker().getY(), gameConfig);

        cardAttacker.minionAttack(gameConfig.getInactivePlayer().getPlayerHero());

        if (gameConfig.getInactivePlayer().getPlayerHero().getHealthStat() <= 0) {
            ObjectNode winStatus = JsonNodeFactory.instance.objectNode();
            if (gameConfig.getActivePlayer().equals(gameConfig.getPlayerOne())) {
                winStatus.put("gameEnded", "Player one killed the enemy hero.");
            } else {
                winStatus.put("gameEnded", "Player two killed the enemy hero.");
            }
            gameConfig.getActivePlayer().setGameWins(gameConfig.getActivePlayer()
                                                               .getGameWins() + 1);
            return winStatus;
        }

        return null;
    }

    /**
     *
     * @param action
     * @param gameConfig
     * @return
     */
    public static ObjectNode useHeroAbility(final ActionsInput action,
                                            final GameConfig gameConfig) {
        ObjectNode error = JsonNodeFactory.instance.objectNode();

        error.put("command", "useHeroAbility");
        error.put("affectedRow", action.getAffectedRow());

        if (gameConfig.getActivePlayer().getPlayerHero().getMana()
            > gameConfig.getActivePlayer().getMana()) {
            error.put("error", HERO_MANA_ERR.getDescription());

            return error;
        } else if (!gameConfig.getActivePlayer().getPlayerHero().isActive()) {
            error.put("error", INACTIVE_HERO.getDescription());

            return error;
        } else {
            switch (gameConfig.getActivePlayer().getPlayerHero().getHeroType()) {
                case ROYCE, THORINA -> {
                    if (gameConfig.getActivePlayer().equals(gameConfig.getPlayerOne())
                            && action.getAffectedRow() > 1
                            || gameConfig.getActivePlayer().equals(gameConfig.getPlayerTwo())
                            && action.getAffectedRow() < 2) {
                        error.put("error", HERO_ENEMY_ROW_ERR.getDescription());

                        return error;
                    }
                    if (gameConfig.getActivePlayer().equals(gameConfig.getPlayerOne())) {
                        gameConfig.getActivePlayer().getPlayerHero()
                                .heroAbility(gameConfig.getPlayerTwo()
                                        .getPlayerRows().get(action.getAffectedRow()));
                    } else {
                        gameConfig.getActivePlayer().getPlayerHero()
                                .heroAbility(gameConfig.getPlayerOne()
                                        .getPlayerRows().get(-(action.getAffectedRow() - 3)));
                    }
                }
                case MUDFACE, KOCIORAW -> {
                    if (gameConfig.getActivePlayer().equals(gameConfig.getPlayerOne())
                            && action.getAffectedRow() < 2
                            || gameConfig.getActivePlayer().equals(gameConfig.getPlayerTwo())
                            && action.getAffectedRow() > 1) {
                        error.put("error", HERO_FRIEND_ROW_ERR.getDescription());

                        return error;
                    }
                    if (gameConfig.getActivePlayer().equals(gameConfig.getPlayerOne())) {
                        gameConfig.getActivePlayer().getPlayerHero()
                                .heroAbility(gameConfig.getPlayerOne()
                                        .getPlayerRows().get(-(action.getAffectedRow() - 3)));

                    } else {
                        gameConfig.getActivePlayer().getPlayerHero()
                                .heroAbility(gameConfig.getPlayerTwo()
                                        .getPlayerRows().get(action.getAffectedRow()));
                    }
                }
                default -> {
                }
            }
        }

        gameConfig.getActivePlayer().setMana(gameConfig.getActivePlayer().getMana()
                                             - gameConfig.getActivePlayer().getPlayerHero()
                                                                           .getMana());
        return null;
    }
}
