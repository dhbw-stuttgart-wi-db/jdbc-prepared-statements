package preparedstatements;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcAccess {
	private String jdbcUrl;

	private String username;

	private String password;

	private Connection connection;

	public JdbcAccess(String jdbcUrl, String username, String password) throws SQLException {
		super();
		this.jdbcUrl = jdbcUrl;
		this.username = username;
		this.password = password;
	}

	/**
	 * Close the connection
	 */
	public synchronized void connect() throws SQLException {
		if (connection == null)
			connection = DriverManager.getConnection(jdbcUrl, username, password);
	}

	/**
	 * Close the connection
	 */
	public synchronized void disconnect() throws SQLException {
		if (connection != null)
			connection.close();
		connection = null;
	}

	public Connection getConnection() {
		return connection;
	}

	/**
	 * A method to execute a select statement
	 */
	public ResultSet executeQueryStatement(String statement) throws SQLException {
		Statement sqlStmt = connection.createStatement();

		// execute the statement and check whether there is a result
		return sqlStmt.executeQuery(statement);
	}

	/**
	 * A method to execute non-select statements
	 * 
	 * @return The number of affected rows
	 */
	public int executeUpdateStatement(String statement) throws SQLException {
		Statement sqlStmt = connection.createStatement();
		// execute the statement and check whether there is a result
		return sqlStmt.executeUpdate(statement);
	}

}
