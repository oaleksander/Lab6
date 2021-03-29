package com.company.commands;

import com.company.storables.DragonHolder;

public class Clear implements CommandAction {
    public String getLabel() {
        return "clear";
    }

    public String getDescription() {
        return "Clear the collection.";
    }

    @Override
    public String execute(String argument) {
        DragonHolder.getCollection().clear();
        return "Collection cleared.";
    }
}
