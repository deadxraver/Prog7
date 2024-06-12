package commands;

import elements.*;
import exceptions.*;
import parsers.*;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class MovieGenerator {
	private static Movie movie;

	private static Scanner scanner;

	public static Movie generateMovie(Scanner reader) {
		return generateMovie(null, reader);
	}

	/**
	 * Generates a new Movie object based on the provided ID and Scanner.
	 *
	 * @param movieIn movie to be modified, null to create new
	 * @param reader  the Scanner object to read input from
	 * @return the generated Movie object
	 */
	public static Movie generateMovie(Movie movieIn, Scanner reader) {
		scanner = reader;
		movie = movieIn;
		return new Movie(
				movie == null ? 0 : movie.getId(),
				getName(),
				getCoordinates(),
				movie == null ? null : movie.getCreationDate(),
				getOscarsCount(),
				getGenre(),
				getMpaaRating(),
				getOperator(),
				movie == null ? null : movie.getOwner()
		);
	}

	/**
	 * Retrieves the coordinates by calling the CoordinatesHelper methods to get the x and y values.
	 *
	 * @return a new Coordinates object with the retrieved x and y values
	 */
	private static Coordinates getCoordinates() {
		return new Coordinates(
				CoordinatesHelper.getX(),
				CoordinatesHelper.getY()
		);
	}

	/**
	 * Helper class for generating Coordinates objects.
	 */
	private static class CoordinatesHelper {
		/**
		 * Retrieves the x coordinate from the user input.
		 *
		 * @return the x coordinate as a Float value
		 */
		private static Float getX() {
			System.out.println("Enter x coordinate");
			if (movie != null) System.out.println("Current value is: " + movie.getCoordinates().getX());
			while (true) {
				try {
					return FloatParser.parse(scanner.nextLine());
				} catch (NullFieldException e) {
					if (movie == null) System.err.println(e.getMessage());
					else return movie.getCoordinates().getX();
				} catch (NumberFormatException e) {
					System.err.println("Wrong number format");
				}
			}
		}

		/**
		 * Retrieves the y coordinate from the user input.
		 *
		 * @return the y coordinate as a Double value
		 */
		private static Double getY() {
			System.out.println("Enter y coordinate");
			if (movie != null) System.out.println("Current value is: " + movie.getCoordinates().getY());
			while (true) {
				try {
					return DoubleParser.parse(scanner.nextLine());
				} catch (NumberOutOfBoundsException e) {
					System.err.println("Number is too big, max number is " + DoubleParser.UPPER_BOUND);
				} catch (NullFieldException e) {
					if (movie == null) System.err.println(e.getMessage());
					else return movie.getCoordinates().getY();
				}
			}
		}
	}

	/**
	 * Retrieves the genre of a movie from user input.
	 *
	 * @return  the genre of the movie
	 */
	private static MovieGenre getGenre() {
		System.out.println("Enter movie genre");
		if (movie != null) System.out.println("Current value is: " + movie.getGenre());
		while (true) {
			try {
				return GenreParser.parse(scanner.nextLine());
			} catch (NullFieldException e) {
				if (movie == null) System.err.println(e.getMessage());
				else return movie.getGenre();
			} catch (NoSuchGenreException e) {
				System.err.println(e.getMessage());
				printFields(MovieGenre.class);
			}
		}
	}
	/**
	 * Retrieves the MPAA rating from user input.
	 *
	 * @return  the MPAA rating of the movie
	 */
	private static MpaaRating getMpaaRating() {
		System.out.println("Enter MPAA rating");
		if (movie != null) System.out.println("Current value is: " + movie.getMpaaRating());
		while (true) {
			try {
				MpaaRating mpaaRating = MpaaRatingParser.parse(scanner.nextLine());
				return (mpaaRating == null && movie != null) ? movie.getMpaaRating() : mpaaRating;
			} catch (NoSuchMpaaRatingException e) {
				System.err.println(e.getMessage());
				printFields(MpaaRating.class);
			}
		}
	}
	/**
	 * Retrieves the name of a movie from user input.
	 *
	 * @return  the name of the movie as a String
	 */
	private static String getName() {
		System.out.println("Enter movie name");
		if (movie != null) System.out.println("Current value is: " + movie.getName());
		while (true) {
			try {
				return StringParser.parse(scanner.nextLine());
			} catch (EmptyStringException e) {
				if (movie == null) System.err.println(e.getMessage());
				else return movie.getName();
			}
		}
	}
	/**
	 * Retrieves the operator information from the user.
	 *
	 * @return  the operator information as a Person object, or null if not entered
	 */
	private static Person getOperator() {
		System.out.println("Would you like to enter information about operator? [Y/n]");
		while (true) {
			String s = scanner.nextLine();
			if (s.equals("n")) return movie != null ? movie.getOperator() : null;
			if (s.equals("Y")) break;
		}
		return new Person(
				PersonHelper.getName(),
				PersonHelper.getBirthday(),
				PersonHelper.getHairColor(),
				PersonHelper.getNationality()
		);
	}

	/**
	 * Helper class for generating Person objects.
	 */
	private static class PersonHelper {
		/**
		 * Retrieves the name of the operator from the user.
		 *
		 * @return  the name of the operator as a string
		 */
		private static String getName() {
			System.out.println("Enter operator name");
			if (movie != null && movie.getOperator() != null)
				System.out.println("Current value is: " + movie.getOperator().getName());
			while (true) {
				try {
					return StringParser.parse(scanner.nextLine());
				} catch (EmptyStringException e) {
					if (movie == null || movie.getOperator() == null) System.out.println(e.getMessage());
					else return movie.getOperator().getName();
				}
			}
		}

		/**
		 * Retrieves the birthday of the operator from the user in the "yyyy-mm-dd" format.
		 * If the user enters an invalid format or a date that has already passed, an error message is displayed.
		 * If the user does not enter a value, the current value is displayed (if available).
		 *
		 * @return  the birthday of the operator as a LocalDateTime object
		 */
		private static LocalDateTime getBirthday() {
			System.out.println("Enter birthday (yyyy-mm-dd format)");
			if (movie != null && movie.getOperator() != null)
				System.out.println("Current value is: " + movie.getOperator().getBirthday());
			while (true) {
				try {
					LocalDateTime birthday = DateTimeParser.parse(scanner.nextLine());
					return (birthday == null && movie != null && movie.getOperator() != null) ? movie.getOperator().getBirthday() : birthday;
				} catch (DateIsNotReachedException e) {
					System.err.println(e.getMessage());
				} catch (DateTimeParseException e) {
					System.err.println("Wrong date format");
				}
			}
		}

		private static Color getHairColor() {
			System.out.println("Enter hair color");
			if (movie != null && movie.getOperator() != null)
				System.out.println("Current value is: " + movie.getOperator().getHairColor());
			while (true) {
				try {
					Color color = ColorParser.parse(scanner.nextLine());
					return (movie != null && movie.getOperator() != null && color == null) ? movie.getOperator().getHairColor() : color;
				} catch (NoSuchColorException e) {
					System.err.println(e.getMessage());
					printFields(Color.class);
				}
			}
		}


		private static Country getNationality() {
			System.out.println("Enter nationality");
			if (movie != null && movie.getOperator() != null)
				System.out.println("Current value is: " + movie.getOperator().getNationality());
			while (true) {
				try {
					Country country = CountryParser.parse(scanner.nextLine());
					return (country == null && movie != null && movie.getOperator() != null) ? movie.getOperator().getNationality() : country;
				} catch (NoSuchCountryException e) {
					System.err.println(e.getMessage());
					printFields(Country.class);
				}
			}
		}
	}

	private static int getOscarsCount() {
		System.out.println("Enter number of oscars");
		if (movie != null) System.out.println("Current value is: " + movie.getOscarsCount());
		while (true) {
			try {
				return IntParser.parse(scanner.nextLine());
			} catch (NullFieldException e) {
				if (movie == null) System.err.println(e.getMessage());
				else return movie.getOscarsCount();
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