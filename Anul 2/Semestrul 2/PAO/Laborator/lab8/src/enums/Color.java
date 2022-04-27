package enums;

public enum Color {
    WHITE(true),
    BLACK(true),
    BLUE(true),
    PURPLE(false);

    private boolean primary;

    Color(boolean primary) {
        this.primary = primary;
    }

    public boolean isPrimary() {
        return primary;
    }
}
