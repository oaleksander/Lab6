package com.company.commands;

import com.company.storables.DragonHolder;
import com.company.storables.DragonUtils;

public class Insert implements CommandAction {
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
            DragonHolder.getCollection().put(Integer.parseInt(argument), DragonUtils.inputNewDragonFromConsole());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Illegal key: " + e.getMessage() + ".");
        }
        return "Insert successful.";
    }
}
