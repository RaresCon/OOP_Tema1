package commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.ActionsInput;
import tableplayers.GameConfig;

public interface Command {
    /**
     *
     * @param action
     * @param gameConfig
     * @param output
     */
    void executeCommand(ActionsInput action, GameConfig gameConfig, ArrayNode output);
}
