package com.company.commands;

import com.company.ui.CommandExecutor;
import com.company.ui.CommandReader;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Arrays;

public class Execute_script implements CommandAction {
    @Override
    public String getLabel() {
        return "execute_script";
    }

    @Override
    public String getArgumentLabel() {
        return "file_name";
    }

    @Override
    public String getDescription() {
        return "executes script from \"file_name\"";
    }

    @Override
    public String execute(String argument) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        File file;
        try {
            file = new File(argument);
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Please specify filename.");
        }
        if (argument.isBlank())
            throw new IllegalArgumentException("Please specify filename.");
        try {
            if (!file.canRead())
                throw new IllegalArgumentException("Can't read file \"" + argument + "\".");
        } catch (SecurityException e) {
            throw new IllegalArgumentException("Can't access file \"" + argument + "\".");
        }
        try {
            BufferedInputStream fileReader = new BufferedInputStream(new FileInputStream(file));
            CommandExecutor commandExecutor = new CommandExecutor(CommandExecutor.scriptCommands, printStream);
            StringBuilder stringBuilder = new StringBuilder();
            while (fileReader.available() > 0)
                stringBuilder.append((char) fileReader.read());
            Arrays.stream(stringBuilder.toString().split("[\\r\\n]+"))
                    .forEach(line -> {
                        if (!line.isBlank())
                            printStream.append(line).append("\n");
                        String formattedLine = line
                                .replaceAll("\\breplace_if_greater\\b", "replace_if_greater_csv")
                                .replaceAll("\\bupdate\\b", "update_csv")
                                .replaceAll("\\binsert\\b", "insert_csv");
                        commandExecutor.execute(CommandReader.readCommandFromString(formattedLine));
                    });
            fileReader.close();
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Can't find file \"" + file + "\".");
        } catch (SecurityException e) {
            throw new IllegalArgumentException("Can't access file \"" + file + "\".");
        } catch (IOException e) {
            throw new IllegalArgumentException("Error occurred accessing file \"" + file + "\".");
        }
        printStream.append("Executed script from file \"").append(argument).append(".");
        return outputStream.toString(Charset.defaultCharset());
    }
}
