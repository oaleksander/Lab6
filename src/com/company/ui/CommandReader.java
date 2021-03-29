package com.company.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;

/**
 * Class designed to get commands from buffered readers and strings
 *
 * @see CommandExecutor
 */
public class CommandReader {

    BufferedReader bufferedReader;

    /**
     * Command reader constructor
     *
     * @param bufferedReader buffered reader to get commands from
     */
    public CommandReader(BufferedReader bufferedReader) {
        this.bufferedReader = bufferedReader;
    }

    /**
     * Get an array of strings from System.in (separated by spaces)
     *
     * @return Array of strings
     */
    public static String[] getStringsFromTerminal() {
        return new CommandReader(new BufferedReader(new InputStreamReader(System.in))).getStringFromBufferedReader().split(" ");
    }

    /**
     * Get a command from string
     *
     * @param singleString string to parse from
     * @return Command
     */
    public static Command readCommandFromString(String singleString) {
        return (readCommandFromString(singleString.split(" ", 2)));
    }

    /**
     * Get a command from strings
     *
     * @param input strings to parse from
     * @return Command
     */
    public static Command readCommandFromString(String[] input) {
        if (input.length != 0) {
            input[0] = input[0].toLowerCase();
            if (input.length > 1)
                return new Command(input[0], input[1]);
            else
                return new Command(input[0]);
        } else return new Command();
    }

    /**
     * Gets a string from buffered reader line
     *
     * @return Received string
     */
    public String getStringFromBufferedReader() {
        try {
            return bufferedReader.readLine();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage() + ".");
            return "";
        }
    }

    /**
     * Get a command from Buffered Reader
     *
     * @return Command
     */
    public Command readCommandFromBufferedReader() {
        return readCommandFromString(getStringFromBufferedReader());
    }

    /**
     * User command class
     */
    public static class Command implements Serializable {
        public String CommandString = "";
        public String ArgumentString = "";

        /**
         * User command constructor with argument
         *
         * @param CommandString  Command
         * @param ArgumentString Argument
         */
        public Command(String CommandString, String ArgumentString) {
            this.CommandString = CommandString;
            this.ArgumentString = ArgumentString;
        }

        /**
         * User command constructor without argument
         *
         * @param CommandString Command
         */
        public Command(String CommandString) {
            this.CommandString = CommandString;
        }

        /**
         * Empty user command constructor
         */
        public Command() {
        }

        @Override
        public String toString() {
            return "Command{" + CommandString + (ArgumentString.isBlank() ? "" : " ") + ArgumentString + "}";
        }
    }
}
