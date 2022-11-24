package commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.ActionsInput;
import tableplayers.GameConfig;

public interface Command {
    /**
     * function to get every command effect over the game configuration
     * @param action action input coming from the player
     * @param gameConfig the game configuration at the time about cards and players
     * @param output the output ArrayNode used to store the Json
     */
    void executeCommand(ActionsInput action, GameConfig gameConfig, ArrayNode output);
}
