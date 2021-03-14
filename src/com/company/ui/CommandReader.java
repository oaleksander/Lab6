package com.company.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Class designed to get commands from buffered readers and strings
 *
 * @see UserRunnable
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
    public static UserCommand readCommandFromString(String singleString) {
        return (readCommandFromString(singleString.split(" ", 2)));
    }

    /**
     * Get a command from strings
     *
     * @param input strings to parse from
     * @return Command
     */
    public static UserCommand readCommandFromString(String[] input) {
        if (input.length != 0) {
            input[0] = input[0].toLowerCase();
            if (input.length > 1)
                return new UserCommand(input[0], input[1]);
            else
                return new UserCommand(input[0]);
        } else return new UserCommand();
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
    public UserCommand readCommandFromBufferedReader() {
        return readCommandFromString(getStringFromBufferedReader());
    }

    /**
     * User command class
     */
    public static class UserCommand {
        public String Command = null;
        public String Argument = null;

        /**
         * User command constructor with argument
         *
         * @param Command  Command
         * @param Argument Argument
         */
        public UserCommand(String Command, String Argument) {
            this.Command = Command;
            this.Argument = Argument;
        }

        /**
         * User command constructor without argument
         *
         * @param Command Command
         */
        public UserCommand(String Command) {
            this.Command = Command;
        }

        /**
         * Empty user command constructor
         */
        public UserCommand() {
        }

        @Override
        public String toString() {
            return "Command{" +
                    "Command='" + Command + '\'' +
                    ", Arguments='" + Argument + '\'' +
                    '}';
        }
    }
}
