package com.company.commands;

import com.company.storables.Dragon;
import com.company.storables.DragonHolder;

import java.util.Comparator;

public class Show implements CommandAction {
    String response;

    public String getLabel() {
        return "show";
    }

    public String getDescription() {
        return "Show all collection elements.";
    }

    public String execute(String argument) {
        response = "Collection:\n";
        DragonHolder.getCollection().values().stream().sorted(Comparator.comparing(Dragon::getName)).forEach(element -> response += element.toString() + "\n");
        return response.substring(0, response.length() - 1);
    }
}
