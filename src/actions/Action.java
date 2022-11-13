package actions;

import cards.Card;
import cards.Environment;
import cards.Minion;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import table.players.GameConfig;
import table.players.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static cards.CardType.ENVIRONMENT;
import static cards.MinionType.DISCIPLE;
import static errors.Error.ATTACKER_ERR;
import static errors.Error.ENV_MANA_ERR;
import static errors.Error.FROZEN_CARD;
import static errors.Error.HERO_ENEMY_ROW_ERR;
import static errors.Error.HERO_FRIEND_ROW_ERR;
import static errors.Error.HERO_MANA_ERR;
import static errors.Error.INACTIVE_CARD_ERR;
import static errors.Error.INACTIVE_HERO;
import static errors.Error.MANA_ERR;
import static errors.Error.OWN_CARD_ERR;
import static errors.Error.OWN_ROW_ERR;
import static errors.Error.PLACE_ENV_ERR;
import static errors.Error.ROW_SPACE_ERR;
import static errors.Error.STEAL_ERR;
import static errors.Error.TANK_ERR;
import static errors.Error.USE_NONENV_ERR;

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
        } else {
            gameConfig.setActivePlayer(gameConfig.getPlayerOne());
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
        ObjectNode error = JsonNodeFactory.instance.objectNode();
        error.put("command", action.getCommand());
        error.put("handIdx", action.getHandIdx());

        if (action.getHandIdx() >= gameConfig.getActivePlayer().getCardsInHand().size()) {
            return null;
        }

        List<Card> currentPlayerCards = gameConfig.getActivePlayer().getCardsInHand();

        if (currentPlayerCards.get(action.getHandIdx()).getCardType() == ENVIRONMENT) {
            error.put("error", PLACE_ENV_ERR.getDescription());

            return error;
        } else if (currentPlayerCards.get(action.getHandIdx()).getMana()
                   > gameConfig.getActivePlayer().getMana()) {
            error.put("error", MANA_ERR.getDescription());

            return error;
        }

        Minion currentCard = (Minion) currentPlayerCards.get(action.getHandIdx());
        switch (currentCard.getMinionType()) {
            case RIPPER:
            case MIRAJ:
            case GOLIATH:
            case WARDEN:
                if (gameConfig.getActivePlayer().getPlayerRows().get(1).size()
                    == gameConfig.getMaxCardsOnRow()) {
                    error.put("error", ROW_SPACE_ERR.getDescription());

                    return error;
                }

                currentCard.setActive(true);
                gameConfig.getActivePlayer().getPlayerRows().get(1).add(currentCard);
                break;

            case SENTINEL:
            case BERSERKER:
            case CURSED:
            case DISCIPLE:
                if (gameConfig.getActivePlayer().getPlayerRows().get(0).size()
                    == gameConfig.getMaxCardsOnRow()) {
                    error.put("error", ROW_SPACE_ERR.getDescription());

                    return error;
                }

                currentCard.setActive(true);
                gameConfig.getActivePlayer().getPlayerRows().get(0).add(currentCard);
                break;
            default:
                break;
        }

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
        ObjectNode error = JsonNodeFactory.instance.objectNode();
        error.put("command", action.getCommand());
        error.put("handIdx", action.getHandIdx());
        error.put("affectedRow", action.getAffectedRow());

        if (action.getHandIdx() >= gameConfig.getActivePlayer().getCardsInHand().size()) {
            return null;
        }

        List<Card> currentPlayerCards = gameConfig.getActivePlayer().getCardsInHand();

        if (currentPlayerCards.get(action.getHandIdx()).getCardType() != ENVIRONMENT) {
            error.put("error", USE_NONENV_ERR.getDescription());

            return error;
        } else if (currentPlayerCards.get(action.getHandIdx()).getMana()
                   > gameConfig.getActivePlayer().getMana()) {
            error.put("error", ENV_MANA_ERR.getDescription());

            return error;
        }

        Environment currentCard = (Environment) currentPlayerCards.get(action.getHandIdx());
        if (gameConfig.getActivePlayer().equals(gameConfig.getPlayerOne())) {
            if (action.getAffectedRow() > 1) {
                error.put("error", OWN_ROW_ERR.getDescription());

                return error;
            }

            List<Minion> affectedRow = gameConfig.getPlayerTwo()
                                                 .getPlayerRows()
                                                 .get(action.getAffectedRow());
            switch (currentCard.getEnvironmentType()) {
                case FIRESTORM:
                    List<Minion> cardsToDelete = new ArrayList<>();
                    for (Minion card : affectedRow) {
                        card.setHealthStat(card.getHealthStat() - 1);
                        if (card.getHealthStat() == 0) {
                            cardsToDelete.add(card);
                        }
                    }
                    affectedRow.removeAll(cardsToDelete);
                    break;
                case WINTERFELL:
                    for (Minion card : affectedRow) {
                        card.setFrozenStat(2);
                    }
                    break;
                case HEARTHOUND:
                    if (gameConfig.getActivePlayer().getPlayerRows()
                                                    .get(action.getAffectedRow()).size()
                                                         == gameConfig.getMaxCardsOnRow()) {
                        error.put("error", STEAL_ERR.getDescription());

                        return error;
                    }
                    Minion maxHealthMinion;

                    if (affectedRow.stream().max(Comparator.comparing(Minion::getHealthStat))
                                                           .isPresent()) {
                        maxHealthMinion = affectedRow.stream()
                                          .max(Comparator.comparing(Minion::getHealthStat)).get();
                    } else {
                        return null;
                    }

                    gameConfig.getActivePlayer().getPlayerRows()
                                                .get(action.getAffectedRow()).add(maxHealthMinion);
                    affectedRow.remove(maxHealthMinion);
                    break;
                default:
                    break;
            }
        } else {
            if (action.getAffectedRow() < 2) {
                error.put("error", OWN_ROW_ERR.getDescription());

                return error;
            }

            List<Minion> affectedRow = gameConfig.getPlayerOne()
                                                 .getPlayerRows()
                                                 .get(-(action.getAffectedRow() - 3));
            switch (currentCard.getEnvironmentType()) {
                case FIRESTORM:
                    List<Minion> cardsToDelete = new ArrayList<>();
                    for (Minion card : affectedRow) {
                        card.setHealthStat(card.getHealthStat() - 1);
                        if (card.getHealthStat() == 0) {
                            cardsToDelete.add(card);
                        }
                    }
                    affectedRow.removeAll(cardsToDelete);
                    break;
                case WINTERFELL:
                    for (Minion card : affectedRow) {
                        card.setFrozenStat(2);
                    }
                    break;
                case HEARTHOUND:
                    if (gameConfig.getActivePlayer().getPlayerRows()
                                                    .get(-(action.getAffectedRow() - 3)).size()
                                                    == gameConfig.getMaxCardsOnRow()) {
                        error.put("error", STEAL_ERR.getDescription());

                        return error;
                    }
                    Minion maxHealthMinion;

                    if (affectedRow.stream().max(Comparator.comparing(Minion::getHealthStat))
                                            .isPresent()) {
                        maxHealthMinion = affectedRow
                                          .stream()
                                          .max(Comparator.comparing(Minion::getHealthStat)).get();
                    } else {
                        return null;
                    }

                    gameConfig.getActivePlayer().getPlayerRows()
                                                .get(-(action.getAffectedRow() - 3))
                                                .add(maxHealthMinion);
                    affectedRow.remove(maxHealthMinion);
                    break;
                default:
                    break;
            }

        }

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
        ObjectNode error = JsonNodeFactory.instance.objectNode();
        ObjectNode attackerCoordinates = JsonNodeFactory.instance.objectNode();
        ObjectNode attackedCoordinates = JsonNodeFactory.instance.objectNode();
        attackerCoordinates.put("x", action.getCardAttacker().getX());
        attackerCoordinates.put("y", action.getCardAttacker().getY());
        attackedCoordinates.put("x", action.getCardAttacked().getX());
        attackedCoordinates.put("y", action.getCardAttacked().getY());

        error.put("command", "cardUsesAttack");
        error.replace("cardAttacker", attackerCoordinates);
        error.replace("cardAttacked", attackedCoordinates);

        Minion cardAttacker = getCardFromTable(action.getCardAttacker().getX(),
                                               action.getCardAttacker().getY(), gameConfig);
        Minion cardAttacked = getCardFromTable(action.getCardAttacked().getX(),
                                               action.getCardAttacked().getY(), gameConfig);

        int xDefence = action.getCardAttacked().getX();

        if (cardAttacked == null || cardAttacker == null) {
            return null;
        }

        if ((gameConfig.getActivePlayer().equals(gameConfig.getPlayerOne()) && xDefence > 1)
            || (gameConfig.getActivePlayer().equals(gameConfig.getPlayerTwo()) && xDefence < 2)) {
            error.put("error", ATTACKER_ERR.getDescription());

            return error;
        }

        if (!cardAttacker.isActive()) {
            error.put("error", INACTIVE_CARD_ERR.getDescription());

            return error;
        } else if (cardAttacker.isFrozenStat() > 0) {
            error.put("error", FROZEN_CARD.getDescription());

            return error;
        }

        if (gameConfig.getActivePlayer().equals(gameConfig.getPlayerOne())) {
            if (checkTankOnRows(gameConfig.getPlayerTwo()) && !(cardAttacked.isTank())) {
                error.put("error", TANK_ERR.getDescription());

                return error;
            } else {
                cardAttacker.minionAttack(cardAttacked);
                if (cardAttacked.getHealthStat() <= 0) {
                    gameConfig.getPlayerTwo().getPlayerRows().get(xDefence)
                                                             .remove(cardAttacked);
                }
            }
        } else {
            if (checkTankOnRows(gameConfig.getPlayerOne()) && !cardAttacked.isTank()) {
                error.put("error", TANK_ERR.getDescription());

                return error;
            } else {
                cardAttacker.minionAttack(cardAttacked);
                if (cardAttacked.getHealthStat() <= 0) {
                    gameConfig.getPlayerOne().getPlayerRows().get(-(xDefence - 3))
                                                             .remove(cardAttacked);
                }
            }
        }

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
        ObjectNode error = JsonNodeFactory.instance.objectNode();
        ObjectNode attackerCoordinates = JsonNodeFactory.instance.objectNode();
        ObjectNode attackedCoordinates = JsonNodeFactory.instance.objectNode();

        attackerCoordinates.put("x", action.getCardAttacker().getX());
        attackerCoordinates.put("y", action.getCardAttacker().getY());
        attackedCoordinates.put("x", action.getCardAttacked().getX());
        attackedCoordinates.put("y", action.getCardAttacked().getY());

        error.put("command", "cardUsesAbility");
        error.replace("cardAttacker", attackerCoordinates);
        error.replace("cardAttacked", attackedCoordinates);

        Minion cardAttacker = getCardFromTable(action.getCardAttacker().getX(),
                                               action.getCardAttacker().getY(), gameConfig);
        Minion cardAttacked = getCardFromTable(action.getCardAttacked().getX(),
                                               action.getCardAttacked().getY(), gameConfig);

        int xDefence = action.getCardAttacked().getX();

        if (cardAttacked == null || cardAttacker == null) {
            return null;
        }

        if (cardAttacker.isFrozenStat() > 0) {
            error.put("error", FROZEN_CARD.getDescription());

            return error;
        } else if (!cardAttacker.isActive()) {
            error.put("error", INACTIVE_CARD_ERR.getDescription());

            return error;
        } else if (cardAttacker.getMinionType().equals(DISCIPLE)) {
            if (gameConfig.getActivePlayer().equals(gameConfig.getPlayerOne()) && xDefence < 2) {
                error.put("error", OWN_CARD_ERR.getDescription());

                return error;
            }
            if (gameConfig.getActivePlayer().equals(gameConfig.getPlayerTwo()) && xDefence > 1) {
                error.put("error", OWN_CARD_ERR.getDescription());

                return error;
            }

            cardAttacked.setHealthStat(cardAttacked.getHealthStat() + 2);
            cardAttacker.setActive(false);
            return null;
        } else {
            if ((gameConfig.getActivePlayer().equals(gameConfig.getPlayerOne())
                && xDefence > 1)
                || (gameConfig.getActivePlayer().equals(gameConfig.getPlayerTwo())
                && xDefence < 2)) {
                error.put("error", ATTACKER_ERR.getDescription());

                return error;
            }
        }

        if (gameConfig.getActivePlayer().equals(gameConfig.getPlayerOne())) {
            if (checkTankOnRows(gameConfig.getPlayerTwo()) && !(cardAttacked.isTank())) {
                error.put("error", TANK_ERR.getDescription());

                return error;
            } else {
                cardAttacker.minionAbility(cardAttacked);
                if (cardAttacked.getHealthStat() <= 0) {
                    gameConfig.getPlayerTwo().getPlayerRows().get(xDefence).remove(cardAttacked);
                }
            }
        } else {
            if (checkTankOnRows(gameConfig.getPlayerOne()) && !(cardAttacked.isTank())) {
                error.put("error", TANK_ERR.getDescription());

                return error;
            } else {
                cardAttacker.minionAbility(cardAttacked);
                if (cardAttacked.getHealthStat() <= 0) {
                    gameConfig.getPlayerOne().getPlayerRows().get(-(xDefence - 3))
                              .remove(cardAttacked);
                }
            }
        }

        return null;
    }

    /**
     *
     * @param action
     * @param gameConfig
     * @return
     */
    public static ObjectNode useAttackHero(final ActionsInput action, final GameConfig gameConfig) {
        ObjectNode error = JsonNodeFactory.instance.objectNode();
        ObjectNode attackerCoordinates = JsonNodeFactory.instance.objectNode();

        attackerCoordinates.put("x", action.getCardAttacker().getX());
        attackerCoordinates.put("y", action.getCardAttacker().getY());

        error.put("command", "useAttackHero");
        error.replace("cardAttacker", attackerCoordinates);

        Minion cardAttacker = getCardFromTable(action.getCardAttacker().getX(),
                action.getCardAttacker().getY(), gameConfig);

        if (cardAttacker == null) {
            return null;
        }

        if (cardAttacker.isFrozenStat() > 0) {
            error.put("error", FROZEN_CARD.getDescription());

            return error;
        } else if (!cardAttacker.isActive()) {
            error.put("error", INACTIVE_CARD_ERR.getDescription());

            return error;
        } else if (gameConfig.getActivePlayer().equals(gameConfig.getPlayerOne())) {
            if (checkTankOnRows(gameConfig.getPlayerTwo())) {
                error.put("error", TANK_ERR.getDescription());

                return error;
            } else {
                cardAttacker.minionAttack(gameConfig.getPlayerTwo().getPlayerHero());

                if (gameConfig.getPlayerTwo().getPlayerHero().getHealthStat() <= 0) {
                    ObjectNode winStatus = JsonNodeFactory.instance.objectNode();
                    winStatus.put("gameEnded", "Player one killed the enemy hero.");
                    gameConfig.getPlayerOne().setGameWins(gameConfig.getPlayerOne()
                                                                    .getGameWins() + 1);

                    return winStatus;
                }
            }
        } else {
            if (checkTankOnRows(gameConfig.getPlayerOne())) {
                error.put("error", TANK_ERR.getDescription());

                return error;
            } else {
                cardAttacker.minionAttack(gameConfig.getPlayerOne().getPlayerHero());

                if (gameConfig.getPlayerOne().getPlayerHero().getHealthStat() <= 0) {
                    ObjectNode winStatus = JsonNodeFactory.instance.objectNode();
                    winStatus.put("gameEnded", "Player two killed the enemy hero.");
                    gameConfig.getPlayerTwo().setGameWins(gameConfig.getPlayerTwo()
                                                                    .getGameWins() + 1);

                    return winStatus;
                }
            }
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

    /**
     *
     * @param x
     * @param y
     * @param gameConfig
     * @return
     */
    private static Minion getCardFromTable(final int x, final int y, final GameConfig gameConfig) {
        if (x < 2 && y < gameConfig.getPlayerTwo().getPlayerRows().get(x).size()) {
            return gameConfig.getPlayerTwo().getPlayerRows().get(x).get(y);
        } else if (x > 1 && y < gameConfig.getPlayerOne().getPlayerRows().get(-(x - 3)).size()) {
            return gameConfig.getPlayerOne().getPlayerRows().get(-(x - 3)).get(y);
        }

        return null;
    }

    /**
     *
     * @param player
     * @return
     */
    private static boolean checkTankOnRows(final Player player) {
        for (Minion minion : player.getPlayerRows().get(1)) {
            if (minion.isTank()) {
                return true;
            }
        }

        return false;
    }

    /**
     *
     * @param action
     * @param gameConfig
     * @return
     */
    private static ObjectNode errorFactory(final ActionsInput action, final GameConfig gameConfig) {

        return null;
    }
}
