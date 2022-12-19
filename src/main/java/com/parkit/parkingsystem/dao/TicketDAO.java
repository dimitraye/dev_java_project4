package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Manage the database access to the table Ticket
 */
public class TicketDAO {

  /**
   *  The logger allow to display logs on the application.
   */
  private static final Logger logger = LogManager.getLogger("TicketDAO");

  public DataBaseConfig dataBaseConfig = new DataBaseConfig();

  /**
   * Saves the ticket in the database.
   * @param ticket
   * @return a boolean.
   */
  public boolean saveTicket(Ticket ticket) {
    Connection con = null;
    PreparedStatement ps = null;
    try {
      con = dataBaseConfig.getConnection();
      ps = con.prepareStatement(DBConstants.SAVE_TICKET);
      ps.setInt(1, ticket.getParkingSpot().getId());
      ps.setString(2, ticket.getVehicleRegNumber());
      ps.setDouble(3, ticket.getPrice());
      ps.setTimestamp(4, Timestamp.valueOf(ticket.getInTime()));
      ps.setTimestamp(5,
          (ticket.getOutTime() == null) ? null : (Timestamp.valueOf(ticket.getOutTime())));
      return ps.execute();
    } catch (Exception ex) {
      logger.error("Error fetching next available slot", ex);
    } finally {
      dataBaseConfig.closePreparedStatement(ps);
      dataBaseConfig.closeConnection(con);
    }
    return false;
  }

  /**
   * Get a ticket.
   * @param vehicleRegNumber
   * @param isParked
   * @return the ticket.
   */
  public Ticket getTicket(String vehicleRegNumber, boolean isParked) {
    Connection con = null;
    Ticket ticket = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      con = dataBaseConfig.getConnection();
      String query = isParked ? DBConstants.GET_TICKET_PARKED : DBConstants.GET_TICKET_UNPARKED;
      ps = con.prepareStatement(query);
      ps.setString(1, vehicleRegNumber);
      rs = ps.executeQuery();
      if (rs.next()) {
        ticket = new Ticket();
        ParkingSpot parkingSpot =
            new ParkingSpot(rs.getInt(1), ParkingType.valueOf(rs.getString(6)), false);
        ticket.setParkingSpot(parkingSpot);
        ticket.setId(rs.getInt(2));
        ticket.setVehicleRegNumber(vehicleRegNumber);
        ticket.setPrice(rs.getDouble(3));
        ticket.setInTime(rs.getTimestamp(4).toLocalDateTime());
        LocalDateTime outTime =
            rs.getTimestamp(5) == null ? null : rs.getTimestamp(5).toLocalDateTime();
        ticket.setOutTime(outTime);
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
    return ticket;
  }

  /**
   * Update a ticket.
   * @param ticket
   * @return a boolean.
   */
  public boolean updateTicket(Ticket ticket) {
    Connection con = null;
    PreparedStatement ps = null;
    try {
      con = dataBaseConfig.getConnection();
      ps = con.prepareStatement(DBConstants.UPDATE_TICKET);
      ps.setDouble(1, ticket.getPrice());
      ps.setTimestamp(2, Timestamp.valueOf(ticket.getOutTime()));
      ps.setInt(3, ticket.getId());
      ps.execute();
      return true;
    } catch (Exception ex) {
      logger.error("Error saving ticket info", ex);
    } finally {
      dataBaseConfig.closePreparedStatement(ps);
      dataBaseConfig.closeConnection(con);
    }
    return false;
  }

  /**
   * Checks if the parking is available.
   * @param vehicleRegNumber
   * @return a boolean.
   */
  public boolean checkParkVehicule(String vehicleRegNumber) {
    Connection con = null;
    boolean isParked = false;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      con = dataBaseConfig.getConnection();
      ps = con.prepareStatement(DBConstants.CHECK_VEHICLE_REGISTRATION);
      ps.setString(1, vehicleRegNumber);
      rs = ps.executeQuery();
      if (rs.next()) {
        isParked = true;
      }
      dataBaseConfig.closeResultSet(rs);
      dataBaseConfig.closePreparedStatement(ps);
    } catch (Exception ex) {
      logger.error("Error when checking if vehicule is already parked", ex);
    } finally {
      dataBaseConfig.closeResultSet(rs);
      dataBaseConfig.closePreparedStatement(ps);
      dataBaseConfig.closeConnection(con);
    }
    return isParked;
  }


  /**
   * Checks if the person that try to park his car is recurrent client.
   * @param vehicleRegNumber
   * @return a boolean.
   */
  public boolean isClient(String vehicleRegNumber) {
    Connection con = null;
    boolean isClient = false;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      con = dataBaseConfig.getConnection();
      ps = con.prepareStatement(DBConstants.CHECK_VEHICLE_RECURRENT);
      ps.setString(1, vehicleRegNumber);
      rs = ps.executeQuery();
      if (rs.next()) {
        isClient = true;
      }
      dataBaseConfig.closeResultSet(rs);
      dataBaseConfig.closePreparedStatement(ps);
    } catch (Exception ex) {
      logger.error("Error when checking if the client is a recurrent client", ex);
    } finally {
      dataBaseConfig.closeConnection(con);
      dataBaseConfig.closePreparedStatement(ps);
      dataBaseConfig.closeResultSet(rs);
    }
    return isClient;
  }
}
