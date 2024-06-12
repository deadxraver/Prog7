package serverlogic;

import elements.*;
import exceptions.NoSuchMovieException;
import exceptions.PasswordException;
import exceptions.UserAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
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
		String set = "UPDATE USERS SET superuser = ? WHERE username = ?;";
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
			preparedStatementSet.executeUpdate();
			return !superuser;
		}
	}

	public static User getUser(String login, String password) {
		String query = "SELECT id, salt, password, superuser FROM USERS WHERE username = ? LIMIT 1;";
		try (
				PreparedStatement ps = connection.prepareStatement(query)
		) {
			ps.setString(1, login);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				String hashedPassword = PasswordHandler.encrypt(password + rs.getString("salt"));
				if (hashedPassword == null) throw new SQLException();
				if (!hashedPassword.equals(rs.getString("password"))) throw new PasswordException();
				return new User(
						rs.getLong("id"),
						login,
						rs.getString("password"),
						rs.getBoolean("superuser")
				);
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
			logger.error(e.getMessage());
			return false;
		}
		logger.info("place is not taken");
		String addQuery = "INSERT INTO USERS (username, password, salt) VALUES (?, ?, ?);";
		try (
				PreparedStatement ps = connection.prepareStatement(addQuery)
		) {
			String salt = PasswordHandler.generateSalt();
			ps.setString(1, login);
			ps.setString(2, PasswordHandler.encrypt(password + salt));
			ps.setString(3, salt);
			ps.executeUpdate();
			return true;
		} catch (SQLException e) {
			logger.error(e.toString());
			return false;
		}
	}

	public static boolean addMovie(User user, Movie movie) {
		movie.setOwner(user);
		String addQuery = """
				INSERT INTO MOVIE (user_id, name, oscars_count, genre, mpaa_rating, x, y)
								VALUES (?, ?, ?, ?::movie_genre, ?::mpaa_rating, ?, ?)
				RETURNING movie.id movie_id, creation_date;
				""";
		try (
				PreparedStatement ps = connection.prepareStatement(addQuery)
		) {
			ps.setLong(1, user.getId());
			ps.setString(2, movie.getName());
			ps.setInt(3, movie.getOscarsCount());
			ps.setString(4, movie.getGenre().toString());
			ps.setString(5, movie.getMpaaRating().toString());
			ps.setDouble(6, movie.getCoordinates().getX());
			ps.setDouble(7, movie.getCoordinates().getY());
			ResultSet resultSet = ps.executeQuery();
			if (resultSet.next()) {
				movie.setId(resultSet.getLong("movie_id"));
				movie.setCreationDate(resultSet.getDate("creation_date").toLocalDate());
				logger.info("Movie successfully added");
				if (movie.getOperator() == null) return true;
			} else return false;
		} catch (SQLException e) {
			logger.error(e.getMessage());
			return false;
		}
		String operatorQuery = "INSERT INTO PERSON (movie_id, name, birthday, hair_color, nationality) " +
				"VALUES (?, ?, ?, ?::color, ?::country);";
		try (
				PreparedStatement ps = connection.prepareStatement(operatorQuery)
		) {
			ps.setLong(1, movie.getId());
			ps.setString(2, movie.getOperator().getName());
			ps.setTimestamp(3, Timestamp.valueOf(movie.getOperator().getBirthday()));
			ps.setString(4, movie.getOperator().getHairColor().toString());
			ps.setString(5, movie.getOperator().getNationality().toString());
			ps.executeUpdate();
			logger.info("Movie and Operator successfully added");
			return true;
		} catch (SQLException e) {
			logger.error(e.getMessage());
			return false;
		}
	}

	public static boolean replaceMovie(long id, Movie newMovie, User user) {
		String deleteQuery = """
				UPDATE movie
				SET name         = ?,
				    oscars_count = ?,
				    genre        = ?::movie_genre,
				    x            = ?,
				    y            = ?
				WHERE id = ?;
				""";
		try (
				PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)
		) {
			preparedStatement.setString(1, newMovie.getName());
			preparedStatement.setLong(2, newMovie.getOscarsCount());
			preparedStatement.setString(3, newMovie.getGenre().toString());
			preparedStatement.setFloat(4, newMovie.getCoordinates().getX());
			preparedStatement.setDouble(5, newMovie.getCoordinates().getY());
			preparedStatement.setLong(6, id);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			logger.error(e.getMessage());
			return false;
		}
		if (newMovie.getOperator() == null) return true;
		String addQuery = """
				UPDATE person
				SET name        = ?,
				    birthday    = ?,
				    hair_color  = ?::color,
				    nationality = ?::country
				WHERE movie_id = ?;
				""";
		try (
				PreparedStatement preparedStatement = connection.prepareStatement(addQuery)
		) {
			preparedStatement.setString(1, newMovie.getOperator().getName());
			preparedStatement.setTimestamp(2, Timestamp.valueOf(newMovie.getOperator().getBirthday()));
			preparedStatement.setString(3, newMovie.getOperator().getHairColor().toString());
			preparedStatement.setString(4, newMovie.getOperator().getHairColor().toString());
			preparedStatement.setLong(5, newMovie.getId());
			preparedStatement.executeUpdate();
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
		if (!user.isSuperuser()) {
			String checkQuery = "SELECT user_id FROM MOVIE JOIN USERS ON user_id = USERS.id WHERE user_id = ? AND MOVIE.id = ? LIMIT 1;";
			try (
					PreparedStatement preparedStatement = connection.prepareStatement(checkQuery)
			) {
				preparedStatement.setLong(1, user.getId());
				preparedStatement.setLong(2, movie.getId());
				ResultSet resultSet = preparedStatement.executeQuery();
				if (!resultSet.next()) throw new NoSuchMovieException();
			} catch (SQLException | NoSuchMovieException e) {
				logger.error(e.getMessage());
				return false;
			}
		}
		String deleteMovie = "DELETE FROM MOVIE WHERE id = ?;";
		try (
				PreparedStatement statement = connection.prepareStatement(deleteMovie)
		) {
			statement.setLong(1, movie.getId());
			statement.executeUpdate();
		} catch (SQLException e) {
			logger.error(e.getMessage());
			return false;
		}
		logger.info("movie successfully deleted from database");
		return true;
	}

	public static MovieCollection getCollection() {
		String query = """
				SELECT movie.id    movie_id,
				       user_id,
				       movie.name  movie_name,
				       creation_date,
				       oscars_count,
				       genre,
				       mpaa_rating,
				       x,
				       y,
				       person.name person_name,
				       birthday,
				       hair_color,
				       nationality,
				       username,
				       password,
				       salt,
				       superuser,
				       registration_date
				FROM MOVIE
				         FULL JOIN PERSON ON PERSON.movie_id = MOVIE.id
				         JOIN USERS ON USERS.id = MOVIE.user_id;
				""";
		MovieCollection movieCollection = new MovieCollection();
		try (
				Statement statement = connection.createStatement()
		) {
			ResultSet resultSet = statement.executeQuery(query);
			while (resultSet.next()) {
				Person person = resultSet.getString("person_name") == null ? null :
						new Person(
								resultSet.getString("person_name"),
								LocalDateTime.of(resultSet.getDate("birthday").toLocalDate(), LocalTime.of(0, 0)),
								Color.valueOf(resultSet.getString("hair_color")),
								Country.valueOf(resultSet.getString("nationality"))
						);
				Movie movie = new Movie(
						resultSet.getLong("movie_id"),
						resultSet.getString("movie_name"),
						new Coordinates(
								resultSet.getFloat("x"),
								resultSet.getDouble("y")
						),
						resultSet.getDate("creation_date").toLocalDate(),
						resultSet.getInt("oscars_count"),
						MovieGenre.valueOf(resultSet.getString("genre")),
						MpaaRating.valueOf(resultSet.getString("mpaa_rating")),
						person,
						new User(
								resultSet.getLong("user_id"),
								resultSet.getString("username"),
								resultSet.getString("password"),
								resultSet.getBoolean("superuser")
						)
				);
				movieCollection.addMovie(movie);
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			System.exit(1);
			return null;
		}
		return movieCollection;
	}

	public static User get(String username, User user) {
		if (!user.isSuperuser()) return null;
		String query = "SELECT id, password, superuser FROM USERS WHERE username = ?;";
		try (
				PreparedStatement preparedStatement = connection.prepareStatement(query)
				) {
			preparedStatement.setString(1, username);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				return new User(
						resultSet.getLong("id"),
						username,
						resultSet.getString("password"),
						resultSet.getBoolean("superuser")
				);
			}
			return null;
		} catch (SQLException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	public static boolean checkIfBelongs(String username, String password, Movie movie) {
		String query = "SELECT 1 FROM MOVIE WHERE id = ? AND user_id = ?;";
		User user = getUser(username, password);
		if (user == null) return false;
		try (
				PreparedStatement preparedStatement = connection.prepareStatement(query)
				) {
			preparedStatement.setLong(1, movie.getId());
			preparedStatement.setLong(2, user.getId());
			if (!preparedStatement.executeQuery().next()) return false;
		} catch (SQLException e) {
			logger.error(e.getMessage());
			return false;
		}
		return true;
	}

	public static boolean checkIfCanModify(String username, String password, Movie movie) {
		User user = getUser(username, password);
		if (user == null) return false;
		if (user.isSuperuser()) return true;
		return checkIfBelongs(username, password, movie);
	}

	public static boolean truncate() {
		String movieQuery = "TRUNCATE TABLE MOVIE CASCADE;";
		try (
				Statement statement = connection.createStatement()
				) {
			statement.executeUpdate(movieQuery);
			return true;
		} catch (SQLException e) {
			logger.error(e.getMessage());
			return false;
		}
	}
}
