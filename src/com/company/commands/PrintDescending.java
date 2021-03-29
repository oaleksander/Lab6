package com.company.commands;

import com.company.storables.Dragon;
import com.company.storables.DragonHolder;

import java.util.Comparator;

public class PrintDescending implements CommandAction {
    String response;

    public String getLabel() {
        return "print_descending";
    }

    public String getDescription() {
        return "Show all collection elements sorted by age.";
    }

    public String execute(String argument) {
        response = "Sorted collection:\n";
        DragonHolder.getCollection().values().stream().sorted(Comparator.comparingLong(Dragon::getAge).reversed())
                .forEachOrdered(element -> response += element.toString() + "\n");
        return response.substring(0, response.length() - 1);
    }
}
