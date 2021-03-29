package com.company.commands;

import com.company.storables.Dragon;
import com.company.storables.DragonHolder;
import com.company.storables.DragonUtils;

public class ReplaceIfGreaterAge implements CommandAction {
    @Override
    public String getLabel() {
        return "replace_if_greater";
    }

    @Override
    public String getArgumentLabel() {
        return "{key} {Dragon}";
    }

    @Override
    public String getDescription() {
        return "replaces a {Dragon} by {key} if he has a greater age";
    }

    @Override
    public String execute(String argument) {
        int key;
        if (argument == null || argument.isEmpty())
            throw new IllegalArgumentException("Please specify Dragon key.");
        try {
            key = Integer.parseInt(argument);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Illegal key: " + e.getMessage() + ".");
        }
        Dragon currentDragon = DragonHolder.getCollection().get(key);
        if (currentDragon == null)
            throw new IllegalArgumentException("No Dragon found with key \"" + key + "\".");
        else {
            Dragon newDragon = DragonUtils.inputNewDragonFromConsole();
            if (newDragon.getAge() > currentDragon.getAge()) {
                DragonHolder.getCollection().replace(key, newDragon);
                return "Remove successful.";
            } else {
                return "Dragon not replaced: new dragon is not older.";
            }
        }
    }
}
