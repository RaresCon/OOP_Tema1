package stats_commands;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import table_players.GameConfig;


public final class Stats {
    public static ObjectNode getTotalGames(ActionsInput action, GameConfig gameConfig) {
        ObjectNode totalGames = JsonNodeFactory.instance.objectNode();

        totalGames.put("command", action.getCommand());
        totalGames.put("output", gameConfig.getPlayerOne().getGameWins() + gameConfig.getPlayerTwo().getGameWins());

        return totalGames;
    }

    public static ObjectNode getPlayerWins(ActionsInput action, GameConfig gameConfig) {
        ObjectNode playerWins = JsonNodeFactory.instance.objectNode();

        playerWins.put("command", action.getCommand());

        if (action.getCommand().equals("getPlayerOneWins"))
            playerWins.put("output", gameConfig.getPlayerOne().getGameWins());
        else
            playerWins.put("output", gameConfig.getPlayerTwo().getGameWins());

        return playerWins;
    }
}
