package errors;

public enum Error {
    PLACE_ERR("Cannot place environment card on table."),
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
