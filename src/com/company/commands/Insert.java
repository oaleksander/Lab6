package com.company.commands;

import com.company.collectionmanagement.DragonFactory;
import com.company.collectionmanagement.DragonHolder;

public class Insert implements Command {
    public String getLabel() {
        return "insert";
    }

    public String getArgumentLabel() {
        return "{key} {element}";
    }

    public String getDescription() {
        return "Insert new {element} to collection with a {key}.";
    }

    public String execute(String argument) {
        if (argument == null || argument.isEmpty())
            throw new IllegalArgumentException("Please specify Dragon key.");
        try {
            DragonHolder.getCollection().put(Integer.parseInt(argument), DragonFactory.inputNewDragonFromConsole());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Illegal key: " + e.getMessage() + ".");
        }
        return "Insert successful.";
    }
}
