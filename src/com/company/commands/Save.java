package com.company.commands;

import com.company.collectionmanagement.DragonHolder;
import com.company.ui.UserRunnable;

import java.io.*;

public class Save implements Command {

    @Override
    public String getLabel() {
        return "save";
    }

    @Override
    public String getDescription() {
        return "Saves collection to file (its name is specified by command line argument).";
    }

    @Override
    public String execute(String argument) {
        File file = UserRunnable.getFile();
        try {
            file.createNewFile();
            OutputStreamWriter fileWriter = new OutputStreamWriter(new FileOutputStream(file));
            DragonHolder.getCollection().forEach((key, value) -> {
                try {
                    fileWriter.write(key + "," + value.toCsvString() + "\n");
                } catch (IOException e) {
                    throw new IllegalArgumentException("Error occurred writing collection to file \"" + file + "\".");
                }
            });
            fileWriter.close();
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Can't find file \"" + file + "\".");
        } catch (SecurityException e) {
            throw new IllegalArgumentException("Can't access file \"" + file + "\".");
        } catch (IOException e) {
            throw new IllegalArgumentException("Error occurred accessing file \"" + file + "\".");
        }
        return "Saved collection to file \"" + file + ".";
    }
}
