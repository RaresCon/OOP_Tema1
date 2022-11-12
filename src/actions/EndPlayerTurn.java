package actions;

import cards.Minion;
import table_players.*;

public class EndPlayerTurn {
    private static int turnsPlayed = 0;
    public static void commandExecute(GameConfig gameConfig) {
        turnsPlayed++;

        for (int i = 0; i < 2; i++) {
            for (Minion minion : gameConfig.getPlayerOne().getPlayerRows().get(i))
                minion.setFrozenStat(false);

            for (Minion minion : gameConfig.getPlayerTwo().getPlayerRows().get(i))
                minion.setFrozenStat(false);
        }

        if (gameConfig.getPlayerOne().getDeck().size() != 0) {
            gameConfig.getPlayerOne().getCardsInHand().add(gameConfig.getPlayerOne().getDeck().get(0));
            gameConfig.getPlayerOne().getDeck().remove(0);
        }

        if (gameConfig.getPlayerTwo().getDeck().size() != 0) {
            gameConfig.getPlayerTwo().getCardsInHand().add(gameConfig.getPlayerTwo().getDeck().get(0));
            gameConfig.getPlayerTwo().getDeck().remove(0);
        }

        if (gameConfig.getActivePlayer().equals(gameConfig.getPlayerOne()))
            gameConfig.setActivePlayer(gameConfig.getPlayerTwo());
        else
            gameConfig.setActivePlayer(gameConfig.getPlayerOne());

        if (turnsPlayed % 2 == 0) {
            if (gameConfig.getManaIncrement() != 10)
                gameConfig.setManaIncrement(gameConfig.getManaIncrement() + 1);
            gameConfig.getPlayerOne().setMana(gameConfig.getManaIncrement());
            gameConfig.getPlayerTwo().setMana(gameConfig.getManaIncrement());
        }
    }
}
