package errors;

public enum Error {
    PLACE_ENV_ERR("Cannot place environment card on table."),
    ROW_ERR("Cannot place card on table since row is full."),
    MANA_ERR("Not enough mana to place card on table.");
    private final String description;

    Error(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return description;
    }
}
