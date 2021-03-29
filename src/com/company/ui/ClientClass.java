package com.company.ui;

import com.company.commands.*;

import java.io.*;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Main command execution runnable
 */
public class ClientClass implements Runnable {

    /**
     * All possible commands
     */
    public static final Command[] allCommands = {
            new Help(),
            new Info(),
            new Show(),
            new Insert(),
            new UpdateID(),
            new RemoveKey(),
            new Clear(),
            new Save(),
            new Execute_script(),
            new Exit(),
            new ReplaceIfGreaterAge(),
            new RemoveGreaterKey(),
            new RemoveLowerKey(),
            new RemoveAllAge(),
            new FilterLessThanType(),
            new PrintDescending(),
            new Read(),
            new CsvInsert(),
            new CsvUpdateID(),
            new CsvReplaceIfGreaterAge()
    };
    /**
     * Commands that user can use
     */
    public static final Command[] userCommands = {
            new Help(),
            new Info(),
            new Show(),
            new Insert(),
            new UpdateID(),
            new RemoveKey(),
            new Clear(),
            new Save(),
            new Execute_script(),
            new Exit(),
            new ReplaceIfGreaterAge(),
            new RemoveGreaterKey(),
            new RemoveLowerKey(),
            new RemoveAllAge(),
            new FilterLessThanType(),
            new PrintDescending(),
            //new Read(),
            //new CsvInsert(),
            //new CsvUpdateID(),
            //new CsvReplaceIfGreaterAge()
    };
    /**
     * Programs that execute_script can use
     */
    public static final Command[] scriptCommands = {
            new Help(),
            new Info(),
            new Show(),
            //new Insert(),
            //new UpdateID(),
            new RemoveKey(),
            new Clear(),
            new Save(),
            //new Execute_script(),
            new Exit(),
            //new ReplaceIfGreaterAge(),
            new RemoveGreaterKey(),
            new RemoveLowerKey(),
            new RemoveAllAge(),
            new FilterLessThanType(),
            new PrintDescending(),
            //new Read(),
            new CsvInsert(),
            new CsvUpdateID(),
            new CsvReplaceIfGreaterAge()
    };
    private static File file = new File("C:\\Users\\muram\\IdeaProjects\\Lab5\\file.csv");
    private final PrintStream printStream;
    private final CommandReader commandReader;
    private final Command[] availableCommands;

    /**
     * User runnable constructor
     *
     * @param availableCommands set of available commands
     * @param printStream       PrintStream to output to
     * @param inputStream       InputStream to input from
     */
    public ClientClass(Command[] availableCommands, PrintStream printStream, InputStream inputStream) {
        this.availableCommands = availableCommands;
        this.printStream = printStream;
        this.commandReader = new CommandReader(new BufferedReader(new InputStreamReader(inputStream)));
    }

    /**
     * Get file specified by command line argument
     *
     * @return File
     */
    public static File getFile() {
        return file;
    }

    /**
     * Set file to save/read collection from
     *
     * @param fileName File name
     */
    public static void setFile(String fileName) {
        file = new File(fileName);
    }

    /**
     * Execute specified user command
     *
     * @param userCommand User command
     */
    public void execute(CommandReader.UserCommand userCommand) {
        AtomicReference<String> response = new AtomicReference<>("Command gave no response.");
        if(Arrays.stream(availableCommands).parallel().noneMatch(command -> command.getLabel().equals(userCommand.Command)))
        {
            response.set("Unknown command \"" + userCommand.Command + "\". try \"help\" for list of commands");
        }
        else
        try {
            Arrays.stream(availableCommands).forEach(command -> {
                if (userCommand.Command.equals(command.getLabel())) response.set(command.execute(userCommand.Argument));
            });
        } catch (IllegalArgumentException e) {
            response.set(e.getMessage());
        } catch (Exception e) {
            response.set("Unexpected error: " + e.getMessage() + ". This is a bug!");
            e.printStackTrace();
        }
        printStream.println(response.get());
    }

    /**
     * Thing that executes commands from bufferedReader until System.exit
     */
    @Override
    public void run() {
        //noinspection InfiniteLoopStatement
        for (; ; ) {
            execute(commandReader.readCommandFromBufferedReader());
        }
    }

    public CommandReader getCommandReader() {
        return commandReader;
    }
}

