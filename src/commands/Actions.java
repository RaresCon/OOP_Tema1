package commands;

import cards.Card;
import cards.Environment;
import cards.Minion;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import errors.ErrorHandler;
import fileio.ActionsInput;
import tableplayers.GameConfig;

import java.util.List;
import cards.AbilityMinion;

public enum Actions implements Command {
    END_TURN {
        @Override
        public void executeCommand(final ActionsInput action, final GameConfig gameConfig,
                                   final ArrayNode output) {
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

                if (!gameConfig.getPlayerOne().getDeck().isEmpty()) {
                    gameConfig.getPlayerOne().getCardsInHand()
                                             .add(gameConfig.getPlayerOne().getDeck().remove(0));
                }

                if (!gameConfig.getPlayerTwo().getDeck().isEmpty()) {
                    gameConfig.getPlayerTwo().getCardsInHand()
                                             .add(gameConfig.getPlayerTwo().getDeck().remove(0));
                }
            }
        }
    },

    PLACE_CARD {
        @Override
        public void executeCommand(final ActionsInput action, final GameConfig gameConfig,
                                   final ArrayNode output) {
            ObjectNode error = ErrorHandler.ErrorFactory
                                           .PLACE_CARD_ERR.checkError(action, gameConfig);

            if (error != null) {
                output.add(error);
                return;
            }

            List<Card> currentPlayerCards = gameConfig.getActivePlayer().getCardsInHand();
            Minion currentCard = (Minion) currentPlayerCards.get(action.getHandIdx());

            currentCard.setActive(true);
            gameConfig.getActivePlayer().getPlayerRows().get(currentCard.getHomeRow())
                                        .add((Minion) gameConfig.getActivePlayer()
                                        .getCardsInHand().remove(action.getHandIdx()));
            gameConfig.getActivePlayer().setMana(gameConfig.getActivePlayer().getMana()
                                                 - currentCard.getMana());
        }
    },

    USE_ENVIRONMENT {
        @Override
        public void executeCommand(final ActionsInput action, final GameConfig gameConfig,
                                   final ArrayNode output) {
            ObjectNode error = ErrorHandler.ErrorFactory
                                           .USE_ENV_CARD_ERR.checkError(action, gameConfig);

            if (error != null) {
                output.add(error);
                return;
            }

            List<Card> currentPlayerCards = gameConfig.getActivePlayer().getCardsInHand();

            Environment currentCard = (Environment) currentPlayerCards.get(action.getHandIdx());
            List<Minion> affectedRow = gameConfig.getAttackedRow(action.getAffectedRow());

            Minion resultMinion = currentCard.useEnvAbility(affectedRow);
            if (resultMinion != null) {
                gameConfig.getActivePlayer().getPlayerRows().get(resultMinion.getHomeRow())
                          .add(resultMinion);
            }

            gameConfig.getActivePlayer().getCardsInHand().remove(action.getHandIdx());
            gameConfig.getActivePlayer().setMana(gameConfig.getActivePlayer().getMana()
                                                 - currentCard.getMana());
        }
    },

    USE_CARD_ATTACK {
        @Override
        public void executeCommand(final ActionsInput action, final GameConfig gameConfig,
                                   final ArrayNode output) {
            ObjectNode error = ErrorHandler.ErrorFactory
                                           .USE_CARD_ATTACK_ERR.checkError(action, gameConfig);

            if (error != null) {
                output.add(error);
                return;
            }

            Minion cardAttacker = Utility.getCardFromTable(action.getCardAttacker().getX(),
                    action.getCardAttacker().getY(), gameConfig);
            Minion cardAttacked = Utility.getCardFromTable(action.getCardAttacked().getX(),
                    action.getCardAttacked().getY(), gameConfig);

            int xDefence = action.getCardAttacked().getX();

            if (cardAttacker == null || cardAttacked == null) {
                return;
            }

            cardAttacker.minionAttack(cardAttacked);
            if (cardAttacked.getHealthStat() <= 0) {
                gameConfig.getAttackedRow(xDefence).remove(cardAttacked);
            }

            cardAttacker.setActive(false);
        }
    },

    USE_CARD_ABILITY {
        @Override
        public void executeCommand(final ActionsInput action, final GameConfig gameConfig,
                                   final ArrayNode output) {
            ObjectNode error = ErrorHandler.ErrorFactory
                                           .USE_CARD_ABILITY_ERR.checkError(action, gameConfig);

            if (error != null) {
                output.add(error);
                return;
            }

            AbilityMinion cardAttacker = (AbilityMinion) Utility
                                         .getCardFromTable(action.getCardAttacker().getX(),
                                                           action.getCardAttacker().getY(),
                                                           gameConfig);
            Minion cardAttacked = Utility.getCardFromTable(action.getCardAttacked().getX(),
                                                           action.getCardAttacked().getY(),
                                                           gameConfig);

            int xDefence = action.getCardAttacked().getX();

            if (cardAttacker == null || cardAttacked == null) {
                return;
            }

            cardAttacker.useMinionAbility(cardAttacked);
            if (cardAttacked.getHealthStat() <= 0) {
                gameConfig.getAttackedRow(xDefence).remove(cardAttacked);
            }

            cardAttacker.setActive(false);
        }
    },

    ATTACK_HERO {
        @Override
        public void executeCommand(final ActionsInput action, final GameConfig gameConfig,
                                   final ArrayNode output) {
            ObjectNode error = ErrorHandler.ErrorFactory
                                           .ATTACK_HERO_ERR.checkError(action, gameConfig);

            if (error != null) {
                output.add(error);
                return;
            }

            Minion cardAttacker = Utility.getCardFromTable(action.getCardAttacker().getX(),
                                  action.getCardAttacker().getY(), gameConfig);

            if (cardAttacker == null) {
                return;
            }

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
        public void executeCommand(final ActionsInput action, final GameConfig gameConfig,
                                   final ArrayNode output) {
            ObjectNode error = ErrorHandler.ErrorFactory
                                           .USE_HERO_ABILITY_ERR.checkError(action, gameConfig);

            if (error != null) {
                output.add(error);
                return;
            }

            gameConfig.getActivePlayer().getPlayerHero()
                                        .useHeroAbility(gameConfig
                                        .getAttackedRow(action.getAffectedRow()));

            gameConfig.getActivePlayer().setMana(gameConfig.getActivePlayer().getMana()
                                                 - gameConfig.getActivePlayer().getPlayerHero()
                                                 .getMana());
        }
    }
}
