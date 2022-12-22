package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Manage the database access to the table Parking
 */
public class ParkingSpotDAO {
  /**
   *  The logger allow to display logs on the application.
   */
  private static final Logger logger = LogManager.getLogger("ParkingSpotDAO");

  /**
   * Allow to access the database connection.
   */
  public DataBaseConfig dataBaseConfig = new DataBaseConfig();

  /**
   * Get the next slot available.
   * @param parkingType
   * @return the next abailable slot.
   */
  public int getNextAvailableSlot(ParkingType parkingType) {
    Connection con = null;
    int result = -1;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      con = dataBaseConfig.getConnection();
      ps = con.prepareStatement(DBConstants.GET_NEXT_PARKING_SPOT);
      ps.setString(1, parkingType.toString());
      rs = ps.executeQuery();
      if (rs.next()) {
        result = rs.getInt(1);
        ;
      }
      dataBaseConfig.closeResultSet(rs);
      dataBaseConfig.closePreparedStatement(ps);
    } catch (Exception ex) {
      logger.error("Error fetching next available slot", ex);
    } finally {
      dataBaseConfig.closeConnection(con);
      dataBaseConfig.closePreparedStatement(ps);
      dataBaseConfig.closeResultSet(rs);
    }
    return result;
  }

  /**
   * Update the parking table.
   * @param parkingSpot
   * @return the parking table from the database.
   */
  public boolean updateParking(ParkingSpot parkingSpot) {
    Connection con = null;
    PreparedStatement ps = null;
    try {
      con = dataBaseConfig.getConnection();
      ps = con.prepareStatement(DBConstants.UPDATE_PARKING_SPOT);
      ps.setBoolean(1, parkingSpot.isAvailable());
      ps.setInt(2, parkingSpot.getId());
      int updateRowCount = ps.executeUpdate();
      dataBaseConfig.closePreparedStatement(ps);
      return (updateRowCount == 1);
    } catch (Exception ex) {
      logger.error("Error updating parking info", ex);
    } finally {
      dataBaseConfig.closeConnection(con);
      dataBaseConfig.closePreparedStatement(ps);
    }
    return false;
  }

  /**
   * This method tells if the parking spot is available or not.
   * @param number
   * @return a boolean.
   */
  public boolean getParkingAvailability(int number) {
    Connection con = null;
    boolean avvailability = false;
    ResultSet rs = null;
    PreparedStatement ps = null;
    try {
      con = dataBaseConfig.getConnection();
      ps = con.prepareStatement(DBConstants.GET_PARKING_AVAILABILITY);
      ps.setInt(1, number);
      rs = ps.executeQuery();
      if (rs.next()) {
        avvailability = rs.getBoolean(1);

      }
      dataBaseConfig.closeResultSet(rs);
      dataBaseConfig.closePreparedStatement(ps);
    } catch (Exception ex) {
      logger.error("Error fetching next available slot", ex);
    } finally {
      dataBaseConfig.closePreparedStatement(ps);
      dataBaseConfig.closeConnection(con);
      dataBaseConfig.closeResultSet(rs);
    }
    return avvailability;
  }


}
