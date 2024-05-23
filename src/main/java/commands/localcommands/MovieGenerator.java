package commands.localcommands;

import elements.*;
import exceptions.*;
import parsers.*;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class MovieGenerator {
    /**
     * Generates a new Movie object based on the provided ID and Scanner.
     *
     * @param  id   the ID of the movie
     * @param  reader   the Scanner object to read input from
     * @return      the generated Movie object
     */
    public static Movie generateMovie(long id, Scanner reader) {
        scanner = reader;
        return new Movie(
                id,
                getName(),
                getCoordinates(),
                LocalDate.now(),
                getOscarsCount(),
                getGenre(),
                getMpaaRating(),
                getOperator()
        );
    }
    private static Scanner scanner;

    private static Coordinates getCoordinates() {
        return new Coordinates(
                CoordinatesHelper.getX(),
                CoordinatesHelper.getY()
        );
    }
    private static class CoordinatesHelper {
        private static Float getX() {
            System.out.println("Enter x coordinate");
            while (true) {
                try {
                    return FloatParser.parse(scanner.nextLine());
                } catch (NullFieldException e) {
                    System.err.println(e.getMessage());
                } catch (NumberFormatException e) {
                    System.err.println("Wrong number format");
                }
            }
        }
        private static Double getY() {
            System.out.println("Enter y coordinate");
            while (true) {
                try {
                    return DoubleParser.parse(scanner.nextLine());
                } catch (NumberOutOfBoundsException e) {
                    System.err.println("Number is too big, max number is " + DoubleParser.UPPER_BOUND);
                } catch (NullFieldException e) {
                    System.err.println(e.getMessage());
                }
            }
        }
    }
    private static MovieGenre getGenre() {
        System.out.println("Enter movie genre");
        while (true) {
            try {
                return GenreParser.parse(scanner.nextLine());
            } catch (NullFieldException e) {
                System.err.println(e.getMessage());
            } catch (NoSuchGenreException e) {
                System.err.println(e.getMessage());
                printFields(MovieGenre.class);
            }
        }
    }
    private static MpaaRating getMpaaRating() {
        System.out.println("Enter MPAA rating");
        while (true) {
            try {
                return MpaaRatingParser.parse(scanner.nextLine());
            } catch (NoSuchMpaaRatingException e) {
                System.err.println(e.getMessage());
                printFields(MpaaRating.class);
            }
        }
    }
    private static String getName() {
        System.out.println("Enter movie name");
        while (true) {
            try {
                return StringParser.parse(scanner.nextLine());
            } catch (EmptyStringException e) {
                System.err.println(e.getMessage());
            }
        }
    }
    private static Person getOperator() {
        System.out.println("Would you like to enter information about operator? [Y/n]");
        while (true) {
            String s = scanner.nextLine();
            if (s.equals("n")) return null;
            if (s.equals("Y")) break;
        }
        return new Person(
                PersonHelper.getName(),
                PersonHelper.getBirthday(),
                PersonHelper.getHairColor(),
                PersonHelper.getNationality()
        );
    }
    private static class PersonHelper {
        private static String getName() {
            System.out.println("Enter operator name");
            while (true) {
                try {
                    return StringParser.parse(scanner.nextLine());
                } catch (EmptyStringException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        private static LocalDateTime getBirthday() {
            System.out.println("Enter birthday (yyyy-mm-dd format)");
            while (true) {
                try {
                    return DateTimeParser.parse(scanner.nextLine());
                } catch (DateIsNotReachedException e) {
                    System.err.println(e.getMessage());
                } catch (DateTimeParseException e) {
                    System.err.println("Wrong date format");
                }
            }
        }
        private static Color getHairColor() {
            System.out.println("Enter hair color");
            while (true) {
                try {
                    return ColorParser.parse(scanner.nextLine());
                } catch (NoSuchColorException e) {
                    System.err.println(e.getMessage());
                    printFields(Color.class);
                }
            }
        }

        private static Country getNationality() {
            System.out.println("Enter nationality");
            while (true) {
                try {
                    return CountryParser.parse(scanner.nextLine());
                } catch (NoSuchCountryException e) {
                    System.err.println(e.getMessage());
                    printFields(Country.class);
                }
            }
        }
    }

    private static int getOscarsCount() {
        System.out.println("Enter number of oscars");
        while (true) {
            try {
                return IntParser.parse(scanner.nextLine());
            } catch (NullFieldException e) {
                System.err.println(e.getMessage());
            } catch (NumberFormatException e) {
                System.err.println("Wrong number format");
            } catch (NumberOutOfBoundsException e) {
                System.out.println("Number must be greater than " + IntParser.LOWER_OSCAR_BOUND);
            }
        }
    }

    private static void printFields(Class<?> clazz) {
        System.out.println("LIST OF AVAILABLE FIELDS:");
        for (Field field : clazz.getDeclaredFields()) {
            String fieldName = field.getName().toLowerCase();
            if (fieldName.equals("$values")) break;
            System.out.println(fieldName);
        }
    }
}
