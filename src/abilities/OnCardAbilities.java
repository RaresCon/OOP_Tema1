package abilities;

import cards.Minion;

public enum OnCardAbilities {
    WEAKKNEES {
        @Override
        public void useAbility(final Minion abilityMinion, final Minion minion) {
            minion.setAttackStat(minion.getAttackStat() - 2);
            if (minion.getAttackStat() < 0) {
                minion.setAttackStat(0);
            }
            abilityMinion.setActive(false);
        }
    },

    GODSPLAN {
        @Override
        public void useAbility(final Minion abilityMinion, final Minion minion) {
            minion.setHealthStat(minion.getHealthStat() + 2);
            abilityMinion.setActive(false);
        }
    },

    SHAPESHIFT {
        @Override
        public void useAbility(final Minion abilityMinion, final Minion minion) {
            minion.setAttackStat(minion.getAttackStat() - minion.getHealthStat());
            minion.setHealthStat(minion.getHealthStat() + minion.getAttackStat());
            minion.setAttackStat(minion.getHealthStat() - minion.getAttackStat());

            abilityMinion.setActive(false);
        }
    },

    SKYJACK {
        @Override
        public void useAbility(final Minion abilityMinion, final Minion minion) {
            abilityMinion.setHealthStat(abilityMinion.getHealthStat() - minion.getHealthStat());
            minion.setHealthStat(minion.getHealthStat() + abilityMinion.getHealthStat());
            abilityMinion.setHealthStat(minion.getHealthStat() - abilityMinion.getHealthStat());

            abilityMinion.setActive(false);
        }
    };

    /**
     * abstract function to be used by any ability implemented from now, processes the changes
     * over the attacked minion
     * @param abilityMinion the minion that has the ability to be used
     * @param minion the minion on which the ability is used
     */
    public abstract void useAbility(Minion abilityMinion, Minion minion);
}
