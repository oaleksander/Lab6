package com.company.commands;

import com.company.storables.Dragon;
import com.company.storables.DragonHolder;
import com.company.storables.DragonUtils;

import java.util.Date;

public class CsvReplaceIfGreaterAge implements CommandAction {
    @Override
    public String getLabel() {
        return "replace_if_greater_csv";
    }

    @Override
    public String getArgumentLabel() {
        return "{key},{Dragon,in,csv,style}";
    }

    @Override
    public String getDescription() {
        return "replaces a {Dragon} by {key} if he has a greater age (CSV version).";
    }

    @Override
    public String execute(String argument) {
        int key;
        if (!argument.isBlank()) {
            String[] splitLine = argument.split(",", 2);
            if (splitLine.length < 2)
                throw new IllegalArgumentException("Invalid key,dragon input string: \"" + argument + "\".");
            try {
                key = Integer.parseInt(splitLine[0]);
                Dragon currentDragon = DragonHolder.getCollection().get(key);
                if (currentDragon == null)
                    throw new IllegalArgumentException("No Dragon found with key \"" + key + "\".");
                Dragon newDragon = new Dragon(splitLine[1]);
                newDragon.setId(DragonUtils.getNewId());
                newDragon.setCreationDate(new Date());
                if (newDragon.getAge() > currentDragon.getAge()) {
                    DragonHolder.getCollection().replace(Integer.parseInt(splitLine[0]), newDragon);
                    return "Remove successful.";
                } else {
                    return "Dragon not replaced: new dragon is not older.";
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Can't parse key from " + splitLine[0] + ".");
            }
        } else {
            throw new IllegalArgumentException("Please specify key,dragon.");
        }
    }
}