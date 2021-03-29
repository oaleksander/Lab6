package com.company.storables;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class Dragon implements Comparable<Dragon> {
    private long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.util.Date creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private long age; //Значение поля должно быть больше 0
    private String description; //Поле может быть null
    private int weight; //Значение поля должно быть больше 0
    private DragonType type; //Поле не может быть null
    private DragonHead head;

    /**
     * Converts CSV format string to Dragon
     *
     * @param csvString CSV format string
     * @throws IllegalArgumentException if CSV format string can't be parsed
     */
    public Dragon(String csvString) throws IllegalArgumentException {
        String[] splitString = csvString.split(",");
        try {
            if (splitString[0].isBlank())
                setId(DragonUtils.getNewId());
            else
                try {
                    if (Long.parseLong(splitString[0]) == -1L)
                        setId(DragonUtils.getNewId());
                    setId(Long.parseLong(splitString[0]));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Can't parse Dragon id from " + splitString[0] + ".");
                }
            setName(splitString[1].replaceAll("\"", ""));
            try {
                setCoordinates(new Coordinates(splitString[2].isBlank() ? .0 : Double.parseDouble(splitString[2]), Long.parseLong(splitString[3])));
            } catch (NumberFormatException | NullPointerException e) {
                throw new IllegalArgumentException("Can't parse Dragon coordinates from " + splitString[2] + "," + splitString[3] + ".");
            }
            if (splitString[4].isBlank())
                setCreationDate(new Date());
            else
                try {
                    setCreationDate(DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault()).parse(splitString[4]));
                } catch (ParseException e) {
                    throw new IllegalArgumentException("Can't parse creation date from " + splitString[4] + ".");
                }
            try {
                setAge(Long.parseLong(splitString[5]));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Can't parse Dragon age from " + splitString[5] + ".");
            }
            setDescription(splitString[6].isBlank() ? "\"\"" : splitString[6].replaceAll("\"", ""));
            try {
                setWeight(Integer.parseInt(splitString[7]));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Can't parse Dragon weight from " + splitString[7] + ".");
            }
            try {
                setType(DragonType.valueOf(splitString[8]));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Can't parse Dragon type from " + splitString[8] + ".");
            }
            setHead(new DragonHead(splitString[9]));
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Not enough arguments to parse Dragon (" + splitString.length + ").");
        }

    }

    /**
     * Default constructor
     */
    public Dragon() {
    }

    /**
     * Dragon constructor with fields
     *
     * @param id           id
     * @param name         name
     * @param coordinates  coordinates
     * @param creationDate creationDate
     * @param age          age
     * @param description  description
     * @param weight       weight
     * @param type         type
     * @param head         head
     */
    public Dragon(long id, String name, Coordinates coordinates, Date creationDate, long age, String description, int weight, DragonType type, DragonHead head) {
        setId(id);
        setName(name);
        setCoordinates(coordinates);
        setCreationDate(creationDate);
        setAge(age);
        setDescription(description);
        setWeight(weight);
        setType(type);
        setHead(head);
    }

    /**
     * Dragon == Object equals
     *
     * @param o Object
     * @return true if equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dragon dragon = (Dragon) o;
        return id == dragon.id;
    }

    /**
     * Dragon hashcode by id
     *
     * @return hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Dragon-to-Dragon comparator
     *
     * @param p Dragon
     * @return >0 if this Dragon is older, <0 if this Dragon is younger, 0 if Dragons are of equal age
     */
    @Override
    public int compareTo(Dragon p) {
        return (int) (this.getAge() - p.getAge());
    }

    /**
     * Dragon ID getter
     *
     * @return ID
     */
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     * Dragon ID getter
     *
     * @return ID
     */
    public String getName() {
        return name;
    }

    /**
     * Dragon name setter
     *
     * @param name name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Dragon ID getter
     *
     * @return ID
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * Dragon coordinates setter
     *
     * @param coordinates coordinates
     */
    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * Dragon creation date getter
     *
     * @return creation date
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * Dragon creation date setter
     *
     * @param creationDate creationDate
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Dragon age getter
     *
     * @return age
     */
    public long getAge() {
        return age;
    }

    /**
     * Dragon age setter
     *
     * @param age age
     * @throws IllegalArgumentException if age is invalid
     */
    public void setAge(long age) throws IllegalArgumentException {
        if (age < 0)
            throw new IllegalArgumentException("Age can't be less than 0");
        this.age = age;
    }

    /**
     * Dragon description getter
     *
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Dragon description setter
     *
     * @param description description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Dragon ID getter
     *
     * @return ID
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Dragon weight setter
     *
     * @param weight weight
     * @throws IllegalArgumentException if weight is illegal
     */
    public void setWeight(int weight) throws IllegalArgumentException {
        if (weight < 0)
            throw new IllegalArgumentException("Weight can't be less than 0");
        this.weight = weight;
    }

    /**
     * Dragon ID getter
     *
     * @return ID
     */
    public DragonType getType() {
        return type;
    }

    /**
     * Dragon type setter
     *
     * @param type dragon type
     */
    public void setType(DragonType type) {
        this.type = type;
    }

    /**
     * Dragon ID getter
     *
     * @return ID
     */
    public DragonHead getHead() {
        return head;
    }

    /**
     * Dragon head setter
     *
     * @param head dragon head
     */
    public void setHead(DragonHead head) {
        this.head = head;
    }

    /**
     * Dragon - to - string converter
     *
     * @return dragon string
     */
    @Override
    public String toString() {
        return "Dragon: " +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", age=" + age +
                ", description='" + description + '\'' +
                ", weight=" + weight +
                ", type=" + type +
                ", head=" + head;
    }

    /**
     * Converts Dragon to CSV string
     *
     * @return CSV string
     */
    public String toCsvString() {
        return getId() + ",\"" + getName() + "\"," + getCoordinates().getX() + "," + getCoordinates().getY() + "," + DateFormat.getDateInstance(DateFormat.SHORT).format(getCreationDate()) + "," + getAge() + ",\"" + (getDescription() == null ? "" : getDescription()) + "\"," + getWeight() + "," + getType().getLabel() + "," + getHead().getEyesCount();
    }
}