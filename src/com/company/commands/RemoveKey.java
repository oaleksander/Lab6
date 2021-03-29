package com.company.commands;

import com.company.storables.Dragon;
import com.company.storables.DragonHolder;

public class RemoveKey implements CommandAction {
    @Override
    public String getLabel() {
        return "remove_key";
    }

    @Override
    public String getArgumentLabel() {
        return "{key}";
    }

    @Override
    public String getDescription() {
        return "Removes element from collection by its {key}";
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
        Dragon removed = DragonHolder.getCollection().remove(key);
        if (removed == null)
            throw new IllegalArgumentException("No Dragon found with key \"" + key + "\".");
        return "Remove successful.";
    }
}
