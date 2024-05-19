package elements;

import exceptions.EmptyCollectionException;
import exceptions.NoSuchMovieException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class MovieCollection {
    public MovieCollection(LocalDate creationDate, Movie... movies) {
        this.creationDate = creationDate;
        collection = new LinkedList<>();
        collection.addAll(Arrays.asList(movies));
    }

    private final LocalDate creationDate;
    private final LinkedList<Movie> collection;

    public Movie getElement(long id) throws NoSuchMovieException {
        for (Movie movie : collection) {
            if (movie.getId() == id) return movie;
        }
        throw new NoSuchMovieException();
    }
    public int getNumberOfMovies() {
        return collection.size();
    }
    public LocalDate getCreationDate() {
        return this.creationDate;
    }
    public String getCollectionType() {
        return Movie.class.getSimpleName();
    }

    public void addMovie(Movie movie) {
        if (movie.getId() == 0) movie.setId(generateId());
        for (int i = 0; i < collection.size(); i++) {
            if (movie.getId() == collection.get(i).getId()) {
                collection.set(i, movie);
                return;
            }
        }
        collection.add(movie);
    }

    public void removeMovie(long id) throws NoSuchMovieException {
        for (Movie movie : collection) {
            if (movie.getId() == id) {
                collection.remove(movie);
                return;
            }
        }
        throw new NoSuchMovieException();
    }

    public void removeMovie(Movie movie) {
        collection.remove(movie);
    }

    public void clear() {
        for (Movie movie : collection) {
            collection.remove(movie);
        }
    }

    public void clear(int userId) {
        // TODO
    }

    private long generateId() {
        while (true) {
            long id = (Long)(long)(Integer.MAX_VALUE * Math.random());
            for (Movie movie : collection) {
                if (movie.getId() == id) {
                    id = 0;
                    break;
                }
            }
            if (id != 0) return id;
        }
    }

    public Movie getMax() throws EmptyCollectionException {
        long id = 0;
        Movie maxMovie = null;
        for (Movie movie : collection) {
            if (movie.getId() > id) {
                maxMovie = movie;
                id = movie.getId();
            }
        }
        if (maxMovie == null) throw new EmptyCollectionException();
        return maxMovie;
    }

    public boolean removeLower(Movie movie) {
        boolean foundLower = false;
        for (Movie movie1 : collection) {
            if (movie.getCoordinates().getLength() >
            movie1.getCoordinates().getLength()) {
                collection.remove(movie1);
                foundLower = true;
            }
        }
        return foundLower;
    }

    public boolean removeByOscar(int number) {
        boolean found = false;
        for (Movie movie : collection) {
            if (movie.getOscarsCount() == number) {
                collection.remove(movie);
                found = true;
            }
        }
        return found;
    }

    public Movie getMpaaMax() throws EmptyCollectionException {
        for (Movie movie : collection) {
            if (movie.getMpaaRating() == MpaaRating.NC_17) {
                return movie;
            }
        }
        for (Movie movie : collection) {
            if (movie.getMpaaRating() == MpaaRating.PG) {
                return movie;
            }
        }
        for (Movie movie : collection) {
            if (movie.getMpaaRating() == MpaaRating.R) {
                return movie;
            }
        }
        throw new EmptyCollectionException();
    }

    public Object[] getOperatorList() {
        ArrayList<Person> list = new ArrayList<>();
        for (Movie movie : collection) {
            if (movie.getOperator() != null) list.add(movie.getOperator());
        }
        return list.toArray();
    }

    public Object[] getCollection() {
        return collection.toArray();
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        for (Movie movie : collection) {
            string.append(movie).append("\n");
        }
        return string.toString();
    }
}
