package com.company.storables;

/**
 * Head of the dragon
 *
 * @see Dragon
 */
public class DragonHead {

    private float eyesCount;

    /**
     * Dragon head from string constructor
     *
     * @param fromString string to parse from
     * @throws IllegalArgumentException if string is invalid
     */
    public DragonHead(String fromString) throws IllegalArgumentException {
        try {
            this.eyesCount = Float.parseFloat(fromString);
        } catch (NumberFormatException | NullPointerException e) {
            throw new IllegalArgumentException("Can't parse Dragon Head from \"" + fromString + "\".");
        }
    }

    /**
     * Dragon head constructor
     *
     * @param eyesCount count of eyes
     */
    public DragonHead(float eyesCount) {
        this.eyesCount = eyesCount;
    }

    /**
     * Get number of eyes
     *
     * @return number of eyes
     */
    public float getEyesCount() {
        return eyesCount;
    }

    /**
     * Set number of eyes
     *
     * @param eyesCount new number of eyes
     */
    public void setEyesCount(float eyesCount) {
        this.eyesCount = eyesCount;
    }

    @Override
    public String toString() {
        return "DragonHead: " +
                " eyesCount=" + eyesCount;
    }
}
