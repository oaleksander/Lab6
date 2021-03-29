package com.company.commands;

import com.company.storables.Coordinates;
import com.company.storables.Dragon;
import com.company.storables.DragonHead;
import com.company.storables.DragonHolder;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.regex.PatternSyntaxException;

public class FilterLessThanType implements CommandAction {
    String response;

    public String getLabel() {
        return "filter_less_than_type";
    }

    public String getArgumentLabel() {
        return "{type} {value}";
    }

    public String getDescription() {
        return "Filter all elements that have {type} less than {value}.";
    }

    public String execute(String argument) {
        String[] arguments;
        try {
            arguments = argument.split(" ", 2);
        } catch (PatternSyntaxException | NullPointerException e) {
            throw new IllegalArgumentException("Please specify field type and value.");
        }
        if (arguments.length < 1)
            throw new IllegalArgumentException("Please specify field type and value.");
        if (arguments.length < 2)
            throw new IllegalArgumentException("Please specify field value.");
        String field = arguments[0];
        String value = arguments[1];
        ArrayList<Dragon> result = new ArrayList<>();
        try {
            Dragon.class.getDeclaredField(field);
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("No field named \"" + field + "\".");
        }
        switch (field) {
            case "id":
            case "age":
                try {
                    long compareToLong = Long.parseLong(value);
                    DragonHolder.getCollection().forEach((key, element) -> {
                        if (((field.equals("id")) ? element.getId() : element.getAge()) < compareToLong)
                            result.add(element);
                    });
                } catch (NumberFormatException | NullPointerException e) {
                    throw new IllegalArgumentException("Can't parse " + field + " from \"" + value + "\": " + e.getMessage() + ".");
                }
                break;
            case "name":
            case "description":
                DragonHolder.getCollection().forEach((key, element) -> {
                    if (((field.equals("name")) ? element.getName() : element.getDescription()).compareTo(value) < 0)
                        result.add(element);
                });
                break;
            case "coordinates":
                Coordinates compareToCoordinates = new Coordinates(value);
                DragonHolder.getCollection().forEach((key, element) -> {
                    if (element.getCoordinates().compareTo(compareToCoordinates) < 0)
                        result.add(element);
                });
                break;
            case "creationDate":
                try {
                    Date compareToDate = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault()).parse(value);
                    DragonHolder.getCollection().forEach((key, element) -> {
                        if (element.getCreationDate().compareTo(compareToDate) < 0)
                            result.add(element);
                    });
                } catch (ParseException e) {
                    throw new IllegalArgumentException("Can't parse date from \"" + value + "\": " + e.getMessage() + ".");
                }
                break;
            case "weight":
                try {
                    int compareToWeight = Integer.parseInt(value);
                    DragonHolder.getCollection().forEach((key, element) -> {
                        if (element.getWeight() < compareToWeight)
                            result.add(element);
                    });
                } catch (NumberFormatException | NullPointerException e) {
                    throw new IllegalArgumentException("Can't parse weight from \"" + value + "\": " + e.getMessage() + ".");
                }
                break;
            case "type":
                throw new IllegalArgumentException("Can't compare Dragon types. All Dragons are equal!");
            case "head":
                float compareToEyesCount = new DragonHead(value).getEyesCount();
                DragonHolder.getCollection().forEach((key, element) -> {
                    if (element.getHead().getEyesCount() < compareToEyesCount)
                        result.add(element);
                });
                break;
            default:
                throw new IllegalArgumentException("No field named \"" + field + "\".");
        }
        response = "Result:\n";
        result.forEach(element -> response += element.toString() + "\n");
        return response;
    }
}