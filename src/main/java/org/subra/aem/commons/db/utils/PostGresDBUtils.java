package org.subra.aem.commons.db.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.subra.aem.commons.utils.SubraStringUtils;

public class PostGresDBUtils {

	public static final String PROTOCOL = "jdbc:postgresql://";

	private static final Logger LOGGER = LoggerFactory.getLogger(PostGresDBUtils.class);

	private PostGresDBUtils() {
		throw new UnsupportedOperationException();
	}

	public static Connection connect(String host, int port, String db, String user, String password)
			throws SQLException {
		return DriverManager.getConnection(createConnectionURL(host, port, db), user, password);

	}

	public static Statement connect(Connection connection) throws SQLException {
		return connection.createStatement();
	}

	public static ResultSet exceute(Statement statement, String sqlQuery) throws SQLException {
		return statement.executeQuery(sqlQuery);
	}

	public static String createConnectionURL(String host, int port, String db) {
		// "jdbc:postgresql://localhost/dvdrental";
		StringBuilder urlBuilder = new StringBuilder();
		if (StringUtils.isNoneBlank(host, db)) {
			urlBuilder.append(PROTOCOL).append(host);
			if (port != 0)
				urlBuilder.append(":").append(port);
			urlBuilder.append(SubraStringUtils.SLASH).append(db);
		}
		return urlBuilder.toString();
	}

}
