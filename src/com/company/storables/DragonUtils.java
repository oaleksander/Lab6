package com.company.storables;

import com.company.ui.CommandReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;


/**
 * Class that is needed to create dragons
 */
public class DragonUtils {

    /**
     * Gew unique Dragon ID
     *
     * @return ID
     */
    synchronized public static long getNewId() {
        ArrayList<Long> usedIDs = new ArrayList<>();
        DragonHolder.getCollection().values().forEach(dragon -> usedIDs.add(dragon.getId()));
        long id = 1;
        while (usedIDs.contains(id)) id++;
        usedIDs.add(id);
        return id;
    }

    /**
     * Inputs new dragon from Console (System.in)
     *
     * @return new Dragon
     */
    public static Dragon inputNewDragonFromConsole() {
        return inputDragonFromConsole(getNewId(), new Date());
    }

    /**
     * Update dragon from Console (System.in), with existing id and
     *
     * @param id
     * @param creationDate
     * @return
     */
    public static Dragon inputDragonFromConsole(long id, Date creationDate) {
        Dragon dragon = new Dragon();
        dragon.setId(id);
        System.out.println("Please input {Name} {Age} [Description] {Weight}.");
        String name = "";
        long age = 0;
        String description = null;
        int weight = 0;
        boolean success = false;
        do {
            try {
                String[] input = CommandReader.getStringsFromTerminal();
                if (input.length < 4) {
                    if (input.length == 3) {
                        dragon.setName(input[0]);
                        dragon.setAge(Integer.parseInt(input[1]));
                        dragon.setWeight(Integer.parseInt(input[2]));
                    } else {
                        throw new IllegalArgumentException(Arrays.toString(input) + input.length);
                    }
                } else {
                    dragon.setName(input[0]);
                    dragon.setAge(Integer.parseInt(input[1]));
                    dragon.setDescription(input[2]);
                    dragon.setWeight(Integer.parseInt(input[3]));
                }
                success = true;
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid arguments for {Name} {Age>0} [Description] {Weight>0}: " + e.getMessage() + ".");
            }
        } while (!success);
        System.out.println("Please input coordinates: [x] {y}.");
        success = false;
        Coordinates coordinates = new Coordinates(.0, 0L);
        do {
            try {
                String[] input = CommandReader.getStringsFromTerminal();
                if (input.length < 2) {
                    if (input.length < 1)
                        throw new IllegalArgumentException("Not enough parameters.");
                    else
                        coordinates.setY(Long.parseLong(input[0]));
                } else {
                    coordinates.setX(Double.parseDouble(input[0]));
                    coordinates.setY(Long.parseLong(input[0]));
                }
                success = true;
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid arguments for {x} {y}: " + e.getMessage() + ".");
            }
        } while (!success);
        dragon.setCoordinates(coordinates);
        DragonType dragonType = null;
        System.out.println("Please specify {Dragon Type} from" + Arrays.toString(DragonType.values()));
        success = false;
        do {
            try {
                String[] input = CommandReader.getStringsFromTerminal();
                if (input.length < 1)
                    throw new IllegalArgumentException("Not enough parameters.");
                dragonType = DragonType.valueOf(input[0]);
                success = true;
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid arguments for {Dragon Type} from " + Arrays.toString(DragonType.values()) + ".");
            }
        } while (!success);
        dragon.setType(dragonType);
        DragonHead dragonHead = null;
        System.out.println("Please specify [Dragon Head] eyes count");
        success = false;
        do {
            try {
                String[] input = CommandReader.getStringsFromTerminal();
                if (input.length < 1)
                    success = true;
                else
                    dragonHead = new DragonHead(Float.parseFloat(input[0]));
                success = true;
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid arguments for [Dragon Head] eyes count: " + e.getMessage() + ".");
            }
        } while (!success);
        dragon.setHead(dragonHead);
        dragon.setCreationDate(creationDate);
        return dragon;
    }
}
