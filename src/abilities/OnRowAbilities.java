package abilities;

import cards.Minion;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public enum OnRowAbilities {
    DAMAGE_CARDS {
        @Override
        public Minion useAbility(final List<Minion> affectedRow) {
            List<Minion> cardsToDelete = new ArrayList<>();
            for (Minion card : affectedRow) {
                card.setHealthStat(card.getHealthStat() - 1);
                if (card.getHealthStat() == 0) {
                    cardsToDelete.add(card);
                }
            }
            affectedRow.removeAll(cardsToDelete);

            return null;
        }
    },

    FREEZE_CARDS {
        @Override
        public Minion useAbility(final List<Minion> affectedRow) {
            for (Minion card : affectedRow) {
                card.setFrozenStat(2);
            }

            return null;
        }
    },

    STEAL_CARD {
        @Override
        public Minion useAbility(final List<Minion> affectedRow) {
            Minion maxHealthMinion = null;

            if (affectedRow.stream().max(Comparator.comparing(Minion::getHealthStat))
                           .isPresent()) {
                maxHealthMinion = affectedRow.stream()
                                  .max(Comparator.comparing(Minion::getHealthStat)).get();
            }
            affectedRow.remove(maxHealthMinion);

            return maxHealthMinion;
        }
    },

    SUBZERO {
        @Override
        public Minion useAbility(final List<Minion> affectedRow) {
            Minion maxAttackMinion;

            if (affectedRow.stream().max(Comparator.comparing(Minion::getAttackStat))
                           .isPresent()) {
                maxAttackMinion = affectedRow.stream()
                                  .max(Comparator.comparing(Minion::getAttackStat)).get();
            } else {
                return null;
            }
            maxAttackMinion.setFrozenStat(2);

            return null;
        }
    },

    LOWBLOW {
        @Override
        public Minion useAbility(final List<Minion> affectedRow) {
            Minion maxHealthMinion = null;

            if (affectedRow.stream().max(Comparator.comparing(Minion::getHealthStat))
                    .isPresent()) {
                maxHealthMinion = affectedRow.stream()
                        .max(Comparator.comparing(Minion::getHealthStat)).get();
            }
            affectedRow.remove(maxHealthMinion);

            return null;
        }
    },

    EARTHBORN {
        @Override
        public Minion useAbility(final List<Minion> affectedRow) {
            for (Minion minion : affectedRow) {
                minion.setHealthStat(minion.getHealthStat() + 1);
            }
            return null;
        }
    },

    BLOODTHIRST {
        @Override
        public Minion useAbility(final List<Minion> affectedRow) {
            for (Minion minion : affectedRow) {
                minion.setAttackStat(minion.getAttackStat() + 1);
            }
            return null;
        }
    };

    /**
     * function to apply the environment/hero ability on a row
     * @param affectedRow the row on which the ability will be applied
     * @return it returns the stolen Minion card (for STEAL_CARD), null otherwise
     *         may return a Minion for other abilities that may need this
     */
    public abstract Minion useAbility(List<Minion> affectedRow);
}
