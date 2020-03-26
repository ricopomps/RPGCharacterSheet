package user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import user.model.User;

public class UserDAO {

	//private String jdbcURL = "jdbc:mysql://localhost:3306/db_rpg_character_sheet?useTimezone=true&serverTimezone=America/Sao_Paulo&useSSL=false";
	private String jdbcURL = "jdbc:postgresql://localhost:5432/db_rpg_character_sheet?useTimezone=true&serverTimezone=America/Sao_Paulo&useSSL=false";
	private String jdbcUsername = "root";
	private String jdbcPassword = "root";
	private Connection jdbcConnection;

	public UserDAO() {

	}

	protected void connect() throws SQLException {
		if (jdbcConnection == null || jdbcConnection.isClosed()) {
			try {
				Class.forName("org.postgresql.Driver");
			} catch (ClassNotFoundException e) {
				throw new SQLException(e);
			} catch (Exception e) {
				throw new SQLException(e);
			}
			jdbcConnection = DriverManager.getConnection(
					"jdbc:postgresql://localhost:5432/db_rpg_character_sheet?useTimezone=true&serverTimezone=America/Sao_Paulo&useSSL=false",
					"root", "root");
		}
	}

	protected void disconnect() throws SQLException {
		if (jdbcConnection != null && !jdbcConnection.isClosed()) {
			jdbcConnection.close();
		}
	}

	public void insertUser(User user) throws SQLException {

		connect();
		PreparedStatement preparedStatement = jdbcConnection
				.prepareStatement("INSERT INTO users (name,password) VALUES (?,?)");
		preparedStatement.setString(1, user.getName());
		preparedStatement.setString(2, user.getPassword());
		preparedStatement.executeUpdate();
		disconnect();
	}

	public List listAllUsers() throws SQLException {
		List<User> userList = new ArrayList<>();

		String sql = "SELECT * FROM users";

		connect();

		Statement statement = jdbcConnection.createStatement();
		ResultSet resultSet = statement.executeQuery(sql);

		while (resultSet.next()) {
			int id = resultSet.getInt("id");
			String name = resultSet.getString("name");

			User user = new User();
			user.setId(id);
			user.setName(name);

			userList.add(user);

		}
		resultSet.close();
		statement.close();

		disconnect();

		return userList;
	}

	public void deleteUser(int id) throws SQLException {
		String sql = "DELETE FROM users WHERE id = ?";

		connect();

		PreparedStatement statement = jdbcConnection.prepareStatement(sql);
		statement.setInt(1, id);

		statement.executeUpdate();
		statement.close();
		disconnect();
	}

	public void updateUser(User user) throws SQLException {
		String sql = "UPDATE users SET name = ?, password = ? WHERE id = ?";
		connect();

		PreparedStatement statement = jdbcConnection.prepareStatement(sql);
		statement.setString(1, user.getName());
		statement.setString(2, user.getPassword());
		statement.setInt(3, user.getId());

		statement.executeUpdate();
		statement.close();
		disconnect();
	}

	public User getUser(String name, String password) throws SQLException {
		User user = null;
		String sql = "SELECT * FROM users WHERE name = ? and password = ? ";
		connect();
		PreparedStatement statement = jdbcConnection.prepareStatement(sql);
		statement.setString(1, name);
		statement.setString(2, password);
		ResultSet resultSet = statement.executeQuery();

		if (resultSet.next()) {
			name = resultSet.getString("name");
			password = resultSet.getString("password");
			int id = resultSet.getInt("id");
			user = new User();
			user.setId(id);
			user.setName(name);
			user.setPassword(password);

		}
		resultSet.close();
		statement.close();
		disconnect();
		return user;
	}

	public User getUser(int id) throws SQLException {
		User user = null;
		String sql = "SELECT * FROM users WHERE id = ?";
		connect();
		PreparedStatement statement = jdbcConnection.prepareStatement(sql);
		statement.setInt(1, id);
		ResultSet resultSet = statement.executeQuery();

		if (resultSet.next()) {
			String name = resultSet.getString("name");
			String password = resultSet.getString("password");

			user = new User();
			user.setId(id);
			user.setName(name);
			user.setPassword(password);
		}

		resultSet.close();
		statement.close();
		disconnect();
		return user;
	}
}
