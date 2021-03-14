package com.company.storables;

/**
 * Type of the dragon
 *
 * @see Dragon
 */
public enum DragonType {
    AIR("AIR"),
    FIRE("FIRE"),
    UNDERGROUND("UNDERGROUND"),
    WATER("WATER");

    private final String label;

    /**
     * Dragon type constructor
     *
     * @param label name
     */
    DragonType(String label) {
        this.label = label;
    }

    /**
     * Gets dragon type name
     *
     * @return dragon type name
     */
    public String getLabel() {
        return label;
    }
}
