package cards;

import fileio.CardInput;
import tableplayers.GameConfig;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Environment extends Card {
    private final EnvironmentType environmentType;

    public Environment(final CardInput cardInput, final CardType cardType,
                       final EnvironmentType environmentType) {
        name = cardInput.getName();
        description = cardInput.getDescription();
        mana = cardInput.getMana();
        colors = cardInput.getColors();
        this.cardType = cardType;
        this.environmentType = environmentType;
    }

    public void environmentAbility(List<Minion> affectedRow) {
        switch (environmentType) {
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
                Minion maxHealthMinion = null;

                if (affectedRow.stream().max(Comparator.comparing(Minion::getHealthStat))
                        .isPresent()) {
                    maxHealthMinion = affectedRow.stream()
                            .max(Comparator.comparing(Minion::getHealthStat)).get();
                }

                affectedRow.add(maxHealthMinion);
                affectedRow.remove(maxHealthMinion);
                break;
            default:
                break;
        }
    }

    /**
     *
     * @return
     */
    public EnvironmentType getEnvironmentType() {
        return environmentType;
    }

    /**
     *
     * @return
     */
    public String toString() {
        return "Environment{"
                +  "mana="
                + mana
                +  ", description='"
                + description
                + '\''
                + ", colors="
                + colors
                + ", name='"
                +  ""
                + name
                + '\''
                + '}'
                + '\n';
    }
}
