package commands;

import cards.Card;
import cards.Environment;
import cards.Minion;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import errors.ErrorMessages;
import fileio.ActionsInput;
import tableplayers.GameConfig;

import java.util.List;

import static cards.MinionType.DISCIPLE;

public enum Actions implements Command {
    END_TURN {
        @Override
        public void executeCommand(ActionsInput action, GameConfig gameConfig, ArrayNode output) {
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
    },

    PLACE_CARD {
        @Override
        public void executeCommand(ActionsInput action, GameConfig gameConfig, ArrayNode output) {
            if (action.getHandIdx() >= gameConfig.getActivePlayer().getCardsInHand().size()) {
                return;
            }

            ObjectNode error = ErrorMessages.errorFactory(action, gameConfig);

            if (error != null) {
                output.add(error);
                return;
            }

            List<Card> currentPlayerCards = gameConfig.getActivePlayer().getCardsInHand();
            Minion currentCard = (Minion) currentPlayerCards.get(action.getHandIdx());

            currentCard.setActive(true);
            gameConfig.getActivePlayer().getPlayerRows().get(currentCard.getHomeRow()).add(currentCard);
            gameConfig.getActivePlayer().setMana(gameConfig.getActivePlayer().getMana()
                    - currentCard.getMana());
            gameConfig.getActivePlayer().getCardsInHand().remove(action.getHandIdx());
        }
    },

    USE_ENVIRONMENT {
        @Override
        public void executeCommand(ActionsInput action, GameConfig gameConfig, ArrayNode output) {
            if (action.getHandIdx() >= gameConfig.getActivePlayer().getCardsInHand().size()) {
                return;
            }

            ObjectNode error = ErrorMessages.errorFactory(action, gameConfig);

            if (error != null) {
                output.add(error);
                return;
            }

            List<Card> currentPlayerCards = gameConfig.getActivePlayer().getCardsInHand();

            Environment currentCard = (Environment) currentPlayerCards.get(action.getHandIdx());
            List<Minion> affectedRow = gameConfig.getAttackedRow(gameConfig, action.getAffectedRow());

            currentCard.environmentAbility(affectedRow);

            gameConfig.getActivePlayer().getCardsInHand().remove(action.getHandIdx());
            gameConfig.getActivePlayer().setMana(gameConfig.getActivePlayer().getMana()
                    - currentCard.getMana());
        }
    },

    USE_CARD_ATTACK {
        @Override
        public void executeCommand(ActionsInput action, GameConfig gameConfig, ArrayNode output) {
            ObjectNode error = ErrorMessages.errorFactory(action, gameConfig);

            if (error != null) {
                output.add(error);
                return;
            }

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
        }
    },

    USE_CARD_ABILITY {
        @Override
        public void executeCommand(ActionsInput action, GameConfig gameConfig, ArrayNode output) {
            ObjectNode error = ErrorMessages.errorFactory(action, gameConfig);

            if (error != null) {
                output.add(error);
                return;
            }

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
        }
    },

    ATTACK_HERO {
        @Override
        public void executeCommand(ActionsInput action, GameConfig gameConfig, ArrayNode output) {
            ObjectNode error = ErrorMessages.errorFactory(action, gameConfig);

            if (error != null) {
                output.add(error);
                return;
            }

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
                output.add(winStatus);
            }
        }
    },

    USE_HERO_ABILITY {
        @Override
        public void executeCommand(ActionsInput action, GameConfig gameConfig, ArrayNode output) {
            ObjectNode error = ErrorMessages.errorFactory(action, gameConfig);

            if (error != null) {
                output.add(error);
                return;
            }

            gameConfig.getActivePlayer().getPlayerHero()
                    .heroAbility(gameConfig.
                            getAttackedRow(gameConfig, action.getAffectedRow()));

            gameConfig.getActivePlayer().setMana(gameConfig.getActivePlayer().getMana()
                    - gameConfig.getActivePlayer().getPlayerHero()
                    .getMana());
        }
    }
}
