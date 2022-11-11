package actions;

import fileio.ActionsInput;

import java.util.ArrayList;
import java.util.List;

public abstract class Action {
    protected String commandName;

    abstract public void actionToOuput();
    abstract public void actionExecute();

    public static List<Action> loadActions(List<ActionsInput> actionsInputList) {
        List<Action> actionList = new ArrayList<>();

        return actionList;
    }

    public String getCommandName() {
        return commandName;
    }

    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }
}
