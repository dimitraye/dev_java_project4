package com.parkit.parkingsystem.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Manage the connexion to the database.
 */
public class DataBaseConfig {

  /**
   *  The logger allow to display logs on the application.
   */
  private static final Logger logger = LogManager.getLogger("DataBaseConfig");

  /**
   * Get the connection to the database with the username and the password.
   * @return connection.
   * @throws ClassNotFoundException to be thrown.
   * @throws SQLException to be thrown.
   */
  public Connection getConnection() throws ClassNotFoundException, SQLException {
    Properties prop = new Properties();
    String fileName = "src/main/resources/connection.properties";

    try (FileInputStream fis = new FileInputStream(fileName)) {
      prop.load(fis);
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    String user = prop.getProperty("db.user");
    String password = prop.getProperty("db.password");
    logger.info("Create DB connection");
    Class.forName("com.mysql.cj.jdbc.Driver");
    return DriverManager.getConnection(
        "jdbc:mysql://localhost:3306/prod?serverTimezone=UTC", user, password);
  }

  /**
   * Close the connection.
   * @param con represents the connection to be closed
   */
  public void closeConnection(Connection con) {
    if (con != null) {
      try {
        con.close();
        logger.info("Closing DB connection");
      } catch (SQLException e) {
        logger.error("Error while closing connection", e);
      }
    }
  }

  /**
   * Close the prepared statement.
   * @param ps represents the prepare statement to be closed.
   */
  public void closePreparedStatement(PreparedStatement ps) {
    if (ps != null) {
      try {
        ps.close();
        logger.info("Closing Prepared Statement");
      } catch (SQLException e) {
        logger.error("Error while closing prepared statement", e);
      }
    }
  }

  /**
   * Close the result set.
   * @param rs represents the result set to be closed.
   */
  public void closeResultSet(ResultSet rs) {
    if (rs != null) {
      try {
        rs.close();
        logger.info("Closing Result Set");
      } catch (SQLException e) {
        logger.error("Error while closing result set", e);
      }
    }
  }
}
