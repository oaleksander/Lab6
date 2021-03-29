package com.company.commands;

import com.company.storables.Dragon;
import com.company.storables.DragonHolder;
import com.company.ui.CommandExecutor;

import java.io.*;
import java.util.Arrays;

public class Read implements CommandAction {

    String response;

    @Override
    public String getLabel() {
        return "read";
    }

    @Override
    public String getDescription() {
        return "Clear collection and read from file (its name is specified by command line argument).";
    }

    @Override
    public String execute(String argument) {
        response = "";
        File file = CommandExecutor.getFile();
        try {
            BufferedInputStream fileReader = new BufferedInputStream(new FileInputStream(file));
            StringBuilder stringBuilder = new StringBuilder();
            while (fileReader.available() > 0)
                stringBuilder.append((char) fileReader.read());
            DragonHolder.getCollection().clear();
            Arrays.stream(stringBuilder.toString().split("[\\r\\n]+"))
                    .forEach(line -> {
                        if (!line.isBlank())
                            try {
                                String[] splitLine = line.split(",", 2);
                                if (splitLine.length < 2)
                                    throw new IllegalArgumentException("Invalid input string: \"" + line + "\".");
                                try {
                                    DragonHolder.getCollection().put(Integer.parseInt(splitLine[0]), new Dragon(splitLine[1]));
                                } catch (NumberFormatException e) {
                                    throw new IllegalArgumentException("Can't parse key from " + splitLine[0] + ".");
                                }
                            } catch (IllegalArgumentException e) {
                                response += "Error reading from CSV line " + line + ": " + e.getMessage() + ".\n";
                            }
                    });
            fileReader.close();
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Can't find file \"" + file + "\".");
        } catch (SecurityException e) {
            throw new IllegalArgumentException("Can't access file \"" + file + "\".");
        } catch (IOException e) {
            throw new IllegalArgumentException("Error occurred accessing file \"" + file + "\".");
        }
        response += "Read collection from file \"" + file + ".";
        return response;
    }
}
