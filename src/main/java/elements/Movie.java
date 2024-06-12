package elements;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;


/**
 * Key element user works with
 * <p>
 * id should be greater than 0, should be unique and be generated automatically
 * <p>
 * name cannot be null or empty
 * <p>
 * coordinates cannot be null
 * <p>
 * creationDate cannot be null and should be generated automatically
 * <p>
 * oscarsCount should be greater than 0
 * <p>
 * genre cannot be null
 * <p>
 * mpaaRating CAN be null
 * <p>
 * operator CAN be null
 */
public class Movie implements Comparable<Movie>, Serializable {
    public Movie(long id, String name, Coordinates coordinates, LocalDate creationDate,
                 int oscarsCount, MovieGenre genre, MpaaRating mpaaRating, Person operator, User owner) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.oscarsCount = oscarsCount;
        this.genre = genre;
        this.mpaaRating = mpaaRating;
        this.operator = operator;
        this.owner = owner;
    }

    private long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private LocalDate creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private int oscarsCount; //Значение поля должно быть больше 0
    private MovieGenre genre; //Поле не может быть null
    private MpaaRating mpaaRating; //Поле может быть null
    private Person operator; //Поле может быть null
    private User owner;

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public int getOscarsCount() {
        return oscarsCount;
    }

    public void setOscarsCount(int oscarsCount) {
        this.oscarsCount = oscarsCount;
    }

    public MovieGenre getGenre() {
        return genre;
    }

    public void setGenre(MovieGenre genre) {
        this.genre = genre;
    }

    public MpaaRating getMpaaRating() {
        return mpaaRating;
    }

    public void setMpaaRating(MpaaRating mpaaRating) {
        this.mpaaRating = mpaaRating;
    }

    public Person getOperator() {
        return operator;
    }

    public void setOperator(Person operator) {
        this.operator = operator;
    }

    public User getOwner() {
        return this.owner;
    }

    public boolean belongsTo(User user) {
        return user == null || this.owner.equals(user);
    }

    @Override
    public String toString() {
        return "id: " + id +
                "\nName: " + name +
                "\nCoordinates: " + coordinates +
                "\nCreation date: " + creationDate +
                "\nNumber of oscars: " + oscarsCount +
                "\nGenre: " + genre +
                "\nMPAA rating: " + (mpaaRating == null ? "no info" : mpaaRating) +
                "\nOperator: " + (operator == null ? "no info" : operator) +
                "\nOwner: " + owner.getUsername();
    }

    @Override
    public int compareTo(Movie movie) {
        return movie == null ? -1 :
                Double.compare(this.coordinates.getLength(), movie.coordinates.getLength());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return id == movie.id && oscarsCount == movie.oscarsCount && Objects.equals(name, movie.name) && Objects.equals(coordinates, movie.coordinates) && Objects.equals(creationDate, movie.creationDate) && genre == movie.genre && mpaaRating == movie.mpaaRating && Objects.equals(operator, movie.operator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creationDate, oscarsCount, genre, mpaaRating, operator);
    }

    @Serial
    private static final long serialVersionUID = 968613003282589089L;
}
