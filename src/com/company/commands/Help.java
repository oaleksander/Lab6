package com.company.commands;

import com.company.ui.UserRunnable;

import java.util.Arrays;

public class Help implements Command {
    String response;

    public String getLabel() {
        return "help";
    }

    public String getDescription() {
        return "Gives the list of available commands.";
    }

    public String execute(String argument) {
        response = "Available commands:\n";
        Arrays.stream(UserRunnable.userCommands).forEach(command -> response += command.getLabel() + " " + command.getArgumentLabel() + ": " + command.getDescription() + "\n");
        response += "Collection class members have to be entered line-by-line. Standard types (including primitive types) have to be entered in the same line as the command.";
        return response;
    }
}
