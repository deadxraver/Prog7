package elements;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import static java.lang.CharSequence.compare;


/**
 * Class describing operator of the movie. Can be null
 * <p>
 * name - cannot be null or empty
 * <p>
 * birthday, hairColor, nationality - CAN be null
 */
public class Person implements Comparable<Person>, Serializable {
    public Person(String name, LocalDateTime birthday, Color hairColor, Country nationality) {
        this.name = name;
        this.birthday = birthday;
        this.hairColor = hairColor;
        this.nationality = nationality;
    }

    private String name; //Поле не может быть null, Строка не может быть пустой
    private LocalDateTime birthday; //Поле может быть null
    private Color hairColor; //Поле может быть null
    private Country nationality; //Поле может быть null

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDateTime birthday) {
        this.birthday = birthday;
    }

    public Color getHairColor() {
        return hairColor;
    }

    public void setHairColor(Color hairColor) {
        this.hairColor = hairColor;
    }

    public Country getNationality() {
        return nationality;
    }

    public void setNationality(Country nationality) {
        this.nationality = nationality;
    }

    @Override
    public String toString() {
        return "name - " + name +
                ", birthday - " + birthday +
                ", hair color - " + hairColor +
                ", nationality - " + nationality;
    }

    @Override
    public int compareTo(Person person) {
        return person == null ? -1 : compare(this.birthday.toString(), person.birthday.toString());
    }

    @Serial
    private static final long serialVersionUID = 3131329026820032967L;
}
