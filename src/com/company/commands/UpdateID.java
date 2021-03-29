package com.company.commands;

import com.company.storables.DragonHolder;
import com.company.storables.DragonUtils;

import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class UpdateID implements CommandAction {
    @Override
    public String getLabel() {
        return "update";
    }

    @Override
    public String getArgumentLabel() {
        return "{id} {element}";
    }

    @Override
    public String getDescription() {
        return "Update {element} of collection by its {id}.";
    }

    @Override
    public String execute(String argument) {
        long id;
        try {
            id = Long.parseLong(argument);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ID argument \"" + argument + "\" is not an integer.");
        }
        AtomicBoolean found = new AtomicBoolean(false);
        AtomicReference<Integer> dragonKey = new AtomicReference<>();
        AtomicReference<Long> dragonId = new AtomicReference<>();
        AtomicReference<Date> dragonCreationDate = new AtomicReference<>();
        DragonHolder.getCollection().forEach((key, value) -> {
            if (value.getId() == id) {
                dragonKey.set(key);
                dragonId.set(value.getId());
                dragonCreationDate.set(value.getCreationDate());
                found.set(true);
            }
        });
        if (!found.get()) throw new IllegalArgumentException("Dragon with id '" + argument + "' not found");
        else
            DragonHolder.getCollection().put(dragonKey.get(), DragonUtils.inputDragonFromConsole(dragonId.get(), dragonCreationDate.get()));
        return "Update successful.";
    }
}
