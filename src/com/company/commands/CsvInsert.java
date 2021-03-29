package com.company.commands;

import com.company.storables.Dragon;
import com.company.storables.DragonHolder;

public class CsvInsert implements CommandAction {
    public String getLabel() {
        return "insert_csv";
    }

    public String getArgumentLabel() {
        return "{key},{Dragon,in,csv,style}";
    }

    public String getDescription() {
        return "Insert new {Dragon} to collection with a {key} (CSV style).";
    }

    public String execute(String argument) {
        if (!argument.isBlank()) {
            String[] splitLine = argument.split(",", 2);
            if (splitLine.length < 2)
                throw new IllegalArgumentException("Invalid key,dragon input string: \"" + argument + "\".");
            try {
                DragonHolder.getCollection().put(Integer.parseInt(splitLine[0]), new Dragon(splitLine[1]));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Can't parse key from " + splitLine[0] + ".");
            }
        } else {
            throw new IllegalArgumentException("Please specify key,dragon.");
        }
        return "Insert successful.";
    }
}
