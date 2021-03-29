package com.company.storables;

import java.util.Date;
import java.util.Hashtable;

/**
 * Class that holds the collection
 */
public class DragonHolder {
    private static final Date initializationDate = new Date();

    private static final Hashtable<Integer, Dragon> collection = new Hashtable<>();

    /**
     * Access the collection
     *
     * @return Dragon collection
     */
    synchronized public static Hashtable<Integer, Dragon> getCollection() {
        return collection;
    }

    /**
     * Get the time when collection was initialized
     *
     * @return Date time
     */
    public static Date getInitializationDate() {
        return initializationDate;
    }
}
