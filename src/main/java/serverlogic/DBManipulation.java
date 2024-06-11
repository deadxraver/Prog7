package serverlogic;

import elements.*;
import exceptions.PasswordException;
import exceptions.UserAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class DBManipulation {
	static {
		try {
			Class.forName("org.postgresql.Driver");
			ResourceBundle resourceBundle = ResourceBundle.getBundle("database_en");
			connection = DriverManager.getConnection(
					resourceBundle.getString("url"),
					resourceBundle.getString("login"),
					resourceBundle.getString("password")
			);
		} catch (SQLException | ClassNotFoundException e) {
			LoggerFactory.getLogger(DBManipulation.class).error(e.getMessage());
			System.exit(1);
		}
	}

	private static final Logger logger = LoggerFactory.getLogger(DBManipulation.class);
	private static Connection connection;

	private DBManipulation() {
	}

	public static boolean grant(String username) throws SQLException {
		String get = "SELECT superuser FROM USERS WHERE username = ?;";
		String set = "UPDATE USERS SET superuser = ? WHERE username = ? RETURNING superuser;";
		try (
				PreparedStatement preparedStatementGet = connection.prepareStatement(get);
				PreparedStatement preparedStatementSet = connection.prepareStatement(set)
				) {
			preparedStatementGet.setString(1, username);
			preparedStatementSet.setString(2, username);
			ResultSet resultSet = preparedStatementGet.executeQuery();
			boolean superuser;
			if (resultSet.next()) superuser = resultSet.getBoolean("superuser");
			else throw new SQLException();
			preparedStatementSet.setBoolean(1, !superuser);
			preparedStatementSet.executeQuery();
			return !superuser;
		}
	}

	public static User getUser(String login, String password) {
		String query = "SELECT id, salt, password FROM USERS WHERE username = ? LIMIT 1;";
		try (
				PreparedStatement ps = connection.prepareStatement(query)
		) {
			ps.setString(1, login);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				String hashedPassword = PasswordHandler.encrypt(password + rs.getString("salt"));
				if (hashedPassword == null) throw new SQLException();
				if (!hashedPassword.equals(rs.getString("password"))) throw new PasswordException();
				return new User(rs.getLong("id"), login, password);
			} else throw new SQLException();
		} catch (SQLException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	public static boolean addUser(String login, String password) {
		String query = "SELECT * FROM USERS WHERE username = ? LIMIT 1;";
		try (
				PreparedStatement ps = connection.prepareStatement(query)
		) {
			ps.setString(1, login);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				logger.error("user already exists");
				throw new UserAlreadyExistsException();
			}
		} catch (SQLException e) {
			logger.error(e.getSQLState());
			return false;
		}
		logger.info("place is not taken");
		String addQuery = "INSERT INTO USERS (username, password, salt) VALUES (?, ?, ?) RETURNING id;";
		try (
				PreparedStatement ps = connection.prepareStatement(addQuery)
		) {
			String salt = PasswordHandler.generateSalt();
			ps.setString(1, login);
			ps.setString(2, PasswordHandler.encrypt(password + salt));
			ps.setString(3, salt);
			ps.executeQuery();
			return true;
		} catch (SQLException e) {
			logger.error(e.toString());
			return false;
		}
	}

	public static boolean addMovie(User user, Movie movie) {
		long id;
		String idQuery = "SELECT id FROM USERS WHERE username = ? AND password = ? LIMIT 1;";
		try (
				PreparedStatement ps = connection.prepareStatement(idQuery)
		) {
			ps.setString(1, user.getUsername());
			ps.setString(2, PasswordHandler.encrypt(user.getPassword()));
			ResultSet resultSet = ps.executeQuery();
			if (resultSet.next()) id = resultSet.getLong(1);
			else throw new SQLException();
		} catch (SQLException e) {
			logger.error(e.getMessage());
			return false;
		}
		String addQuery = "INSERT INTO MOVIE (user_id, name, oscars_count, genre, mpaa_rating, x, y) VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING movie_id, creation_date;";
		try (
				PreparedStatement ps = connection.prepareStatement(addQuery)
		) {
			ps.setLong(1, id);
			ps.setString(2, movie.getName());
			ps.setInt(3, movie.getOscarsCount());
			ps.setString(4, movie.getGenre().toString());
			ps.setString(5, movie.getMpaaRating().toString());
			ps.setDouble(6, movie.getCoordinates().getX());
			ps.setDouble(7, movie.getCoordinates().getY());
			ResultSet resultSet = ps.executeQuery();
			if (resultSet.next()) {
				movie.setId(resultSet.getLong("id"));
				movie.setCreationDate(resultSet.getDate("creation_date").toLocalDate());
				logger.info("Movie successfully added");
				if (movie.getOperator() == null) return true;
			}
			if (movie.getOperator() == null) return false;
		} catch (SQLException e) {
			logger.error(e.getMessage());
			return false;
		}
		String operatorQuery = "INSERT INTO PERSON (movie_id, name, birthday, hair_color, nationality) " +
				"VALUES (?, ?, ?, ?, ?);";
		try (
				PreparedStatement ps = connection.prepareStatement(operatorQuery)
		) {
			ps.setLong(1, movie.getId());
			ps.setString(2, movie.getOperator().getName());
			ps.setTimestamp(3, Timestamp.valueOf(movie.getOperator().getBirthday()));
			ps.setObject(4, movie.getOperator().getHairColor());
			ps.setObject(5, movie.getOperator().getNationality());
			ps.executeQuery();
			logger.info("Movie and Operator successfully added");
			return true;
		} catch (SQLException e) {
			logger.error(e.getMessage());
			return false;
		}
	}

	public static boolean replaceMovie(User user, Movie oldMovie, Movie newMovie) {
		if (check(oldMovie)) return false;
		String deleteQuery = "DELETE FROM MOVIE WHERE id = ? RETURNING creation_date;";
		try (
				PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)
		) {
			preparedStatement.setLong(1, oldMovie.getId());
			ResultSet resultSet = preparedStatement.executeQuery();
			if (!resultSet.next()) throw new SQLException();
		} catch (SQLException e) {
			logger.error(e.getMessage());
			return false;
		}
		String addQuery = "INSERT INTO MOVIE (id, user_id, name, creation_date, oscars_count, genre, mpaa_rating, x, y) " +
				"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try (
				PreparedStatement preparedStatement = connection.prepareStatement(addQuery)
		) {
			preparedStatement.setLong(1, newMovie.getId());
			preparedStatement.setLong(2, oldMovie.getOwner().getId());
			preparedStatement.setString(3, newMovie.getName());
			preparedStatement.setDate(4, Date.valueOf(oldMovie.getCreationDate()));
			preparedStatement.setInt(5, newMovie.getOscarsCount());
			preparedStatement.setObject(6, newMovie.getGenre());
			preparedStatement.setObject(7, newMovie.getMpaaRating());
			preparedStatement.setDouble(8, newMovie.getCoordinates().getX());
			preparedStatement.setDouble(9, newMovie.getCoordinates().getY());
			preparedStatement.executeQuery();
		} catch (SQLException e) {
			logger.error(e.getMessage());
			return false;
		}
		logger.info("movie in database updated");
		return true;
	}

	private static boolean check(Movie oldMovie) {
		String checkQuery = "SELECT * FROM MOVIE WHERE id = ? LIMIT 1;";
		try (
				PreparedStatement preparedStatement = connection.prepareStatement(checkQuery)
		) {
			preparedStatement.setLong(1, oldMovie.getId());
			ResultSet resultSet = preparedStatement.executeQuery();
			if (!resultSet.next()) throw new SQLException();
		} catch (SQLException e) {
			logger.error(e.getMessage());
			return true;
		}
		return false;
	}

	public static boolean deleteMovie(User user, Movie movie) {
		if (check(movie)) return false;
		String deleteQuery = "DELETE FROM MOVIE WHERE id = ?;";
		try (
				PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)
		) {
			preparedStatement.setLong(1, movie.getId());
			if (!preparedStatement.executeQuery().next()) throw new SQLException();
		} catch (SQLException e) {
			logger.error(e.getMessage());
			return false;
		}
		logger.info("movie successfully deleted from database");
		return true;
	}

	public static MovieCollection getCollection() {
		String query = "SELECT * FROM MOVIE LEFT JOIN PERSON ON PERSON.movie_id = MOVIE.id JOIN USERS ON USERS.id = MOVIE.user_id;";
		MovieCollection movieCollection = new MovieCollection(LocalDate.now());
		try (
				PreparedStatement preparedStatement = connection.prepareStatement(query)
		) {
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				Person person = resultSet.getString("person.name") == null ? null :
						new Person(
								resultSet.getString("person.name"),
								LocalDateTime.of(resultSet.getDate("birthday").toLocalDate(), LocalTime.of(0, 0)),
								(Color) resultSet.getObject("hair_color"),
								(Country) resultSet.getObject("nationality")
						);
				Movie movie = new Movie(
						resultSet.getLong("movie.id"),
						resultSet.getString("movie.name"),
						new Coordinates(
								resultSet.getFloat("x"),
								resultSet.getDouble("y")
						),
						resultSet.getDate("creation_date").toLocalDate(),
						resultSet.getInt("oscars_count"),
						(MovieGenre) resultSet.getObject("genre"),
						(MpaaRating) resultSet.getObject("mpaa_rating"),
						person,
						new User(
								resultSet.getLong("id"),
								resultSet.getString("username"),
								resultSet.getString("password")
						)
				);
				movieCollection.addMovie(movie.getOwner(), movie);
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
			System.exit(1);
			return null;
		}
		return movieCollection;
	}
}
