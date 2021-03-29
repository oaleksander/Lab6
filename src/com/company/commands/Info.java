package com.company.commands;

import com.company.storables.DragonHolder;

public class Info implements CommandAction {
    public String getLabel() {
        return "info";
    }

    public String getDescription() {
        return "Gives the info about collection.";
    }

    public String execute(String argument) {
        return "Dragon collection, initialization date: " + DragonHolder.getInitializationDate() + ", number of elements: " + DragonHolder.getCollection().size() + ".";
    }
}
