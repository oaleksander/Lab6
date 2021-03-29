package com.company.storables;

import java.util.regex.PatternSyntaxException;

/**
 * Coordinates where Dragon can be at
 *
 * @see Dragon
 */
public class Coordinates implements Comparable<Coordinates> {

    private double x;
    private Long y; //Can't be null

    /**
     * Coordinates from string constructor
     *
     * @param fromString string to parse from
     * @throws IllegalArgumentException if string is invalid
     */
    public Coordinates(String fromString) throws IllegalArgumentException {
        String[] arguments;
        try {
            arguments = fromString.split(" ", 2);
        } catch (PatternSyntaxException e) {
            throw new IllegalArgumentException("Error in argument: " + e.getMessage() + ".");
        }
        try {
            if (arguments.length > 1) {
                this.x = Double.parseDouble(arguments[0]);
                this.y = Long.parseLong(arguments[1]);
            } else if (arguments.length > 0) {
                this.y = Long.parseLong(arguments[0]);
            }
        } catch (NumberFormatException | NullPointerException e) {
            throw new IllegalArgumentException("Can't parse Coordinates from \"" + fromString + "\": " + e.getMessage() + ".");
        }
    }

    /**
     * Coordinates constructor
     *
     * @param x x coordinate
     * @param y y coordinate
     */
    public Coordinates(double x, Long y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Gets x coordinate
     *
     * @return x coordinate
     */
    public double getX() {
        return x;
    }

    /**
     * Sets x coordinate
     *
     * @param x new x coordinate
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Gets y coordinate
     *
     * @return y coordinate
     */
    public Long getY() {
        return y;
    }

    /**
     * Sets y coordinate
     *
     * @param y new y coordinate
     */
    public void setY(Long y) {
        if (y == null)
            throw new IllegalArgumentException("y can't be null");
        this.y = y;
    }

    @Override
    public String toString() {
        return "{" +
                "x=" + x +
                ";y=" + y +
                '}';
    }

    @Override
    public int compareTo(Coordinates o) {
        return (int) (Math.sqrt(this.getY() * this.getY() + this.getX() * this.getX()) - Math.sqrt(o.getY() * o.getY() + o.getX() * o.getX()));
    }
}