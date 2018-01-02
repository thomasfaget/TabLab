package tablab;

/**
 * All the drum part
 */
public enum LineType {
    BASS("Bass"),
    SNARE("Snare"),
    FLOOR_TOM("Floor tom"),
    MIDDLE_TOM("Middle tom"),
    HIGH_TOM("High tom"),
    HIT_HAT("Hit-hat"),
    OPEN_HIT_HAT("Open hit-hat"),
    RIDE("Ride"),
    CRASH("Crash");

    private String type;

    LineType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
