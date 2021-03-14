package com.company.commands;

import com.company.collectionmanagement.DragonHolder;
import com.company.storables.Dragon;

import java.util.Comparator;

public class PrintDescending implements Command {
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
        return response;
    }
}
