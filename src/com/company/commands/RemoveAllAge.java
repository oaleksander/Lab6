package com.company.commands;

import com.company.storables.DragonHolder;

import java.util.LinkedList;

public class RemoveAllAge implements CommandAction {
    @Override
    public String getLabel() {
        return "remove_all_by_age";
    }

    @Override
    public String getArgumentLabel() {
        return "{age}";
    }

    @Override
    public String getDescription() {
        return "Remove all Dragons {age} years old.";
    }

    @Override
    public String execute(String argument) {
        long age;
        if (argument == null || argument.isEmpty())
            throw new IllegalArgumentException("Please specify Dragon age.");
        try {
            age = Long.parseLong(argument);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Illegal age: " + e.getMessage() + ".");
        }
        LinkedList<Integer> toRemove = new LinkedList<>();
        DragonHolder.getCollection().forEach((key, dragon) -> {
            if (dragon.getAge() == age) toRemove.add(key);
        });
        toRemove.forEach(key -> DragonHolder.getCollection().remove(key));
        return "Removed " + toRemove.size() + " Dragons.";
    }
}
