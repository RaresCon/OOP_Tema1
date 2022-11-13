package stats.commands;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import table.players.GameConfig;


public final class Stats {
    private Stats() {

    }

    /**
     *
     * @param action
     * @param gameConfig
     * @return
     */
    public static ObjectNode getTotalGames(final ActionsInput action, final GameConfig gameConfig) {
        ObjectNode totalGames = JsonNodeFactory.instance.objectNode();

        totalGames.put("command", action.getCommand());
        totalGames.put("output", gameConfig.getPlayerOne().getGameWins()
                                 + gameConfig.getPlayerTwo().getGameWins());

        return totalGames;
    }

    /**
     *
     * @param action
     * @param gameConfig
     * @return
     */
    public static ObjectNode getPlayerWins(final ActionsInput action, final GameConfig gameConfig) {
        ObjectNode playerWins = JsonNodeFactory.instance.objectNode();

        playerWins.put("command", action.getCommand());

        if (action.getCommand().equals("getPlayerOneWins")) {
            playerWins.put("output", gameConfig.getPlayerOne().getGameWins());
        } else {
            playerWins.put("output", gameConfig.getPlayerTwo().getGameWins());
        }

        return playerWins;
    }
}
