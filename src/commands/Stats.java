package commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import tableplayers.GameConfig;


public enum Stats implements Command {
    GET_TOTAL_GAMES {
        @Override
        public void executeCommand(ActionsInput action, GameConfig gameConfig, ArrayNode output) {
            ObjectNode totalGames = JsonNodeFactory.instance.objectNode();

            totalGames.put("command", action.getCommand());
            totalGames.put("output", gameConfig.getPlayerOne().getGameWins()
                    + gameConfig.getPlayerTwo().getGameWins());

            output.add(totalGames);
        }
    },

    GET_WINS {
        @Override
        public void executeCommand(ActionsInput action, GameConfig gameConfig, ArrayNode output) {
            ObjectNode playerWins = JsonNodeFactory.instance.objectNode();

            playerWins.put("command", action.getCommand());

            if (action.getCommand().equals("getPlayerOneWins")) {
                playerWins.put("output", gameConfig.getPlayerOne().getGameWins());
            } else {
                playerWins.put("output", gameConfig.getPlayerTwo().getGameWins());
            }

            output.add(playerWins);
        }
    }
}
