package commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.ActionsInput;
import tableplayers.GameConfig;

public interface Command {
    void executeCommand(ActionsInput action, GameConfig gameConfig, ArrayNode output);
}
