package com.parkit.parkingsystem.constants;

/**
 * This class contains the different SQL requests.
 */
public class DBConstants {

  /**
   * Get the next parking spot.
   */
  public static final String GET_NEXT_PARKING_SPOT =
      "select min(PARKING_NUMBER) from parking where AVAILABLE = true and TYPE = ?";

  /**
   * Update the parking spot availablity status.
   */
  public static final String UPDATE_PARKING_SPOT =
      "update parking set available = ? where PARKING_NUMBER = ?";

  /**
   * Save the ticket in the database.
   */
  public static final String SAVE_TICKET =
      "insert into ticket(PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME) "
          + "values(?,?,?,?,?)";
  /**
   * Update the ticket (price and outTime).
   */
  public static final String UPDATE_TICKET = "update ticket set PRICE=?, OUT_TIME=? where ID=?";


  /**
   * Get the ticket associated to a vehicle that is not parked.
   */
  public static final String GET_TICKET_UNPARKED = ""
      + "select t.PARKING_NUMBER, t.ID, t.PRICE, t.IN_TIME, t.OUT_TIME, p.TYPE "
      + "from ticket t,parking p "
      + "where p.parking_number = t.parking_number and t.VEHICLE_REG_NUMBER=? and OUT_TIME "
      + "is not null "
      + "order by t.IN_TIME  "
      + "limit 1";

  /**
   * Get the ticket associated to a vehicle that is parked.
   */
  public static final String GET_TICKET_PARKED =
      "select t.PARKING_NUMBER, t.ID, t.PRICE, t.IN_TIME, t.OUT_TIME, p.TYPE "
      + "from ticket t,parking p "
      + "where p.parking_number = t.parking_number and t.VEHICLE_REG_NUMBER=? "
      + "and OUT_TIME is null "
      + "order by t.IN_TIME  "
      + "limit 1";

  /**
   * Get the status of the parking.
   */
  public static final String GET_PARKING_AVAILABILITY =
      "select AVAILABLE from parking where PARKING_NUMBER = ?";

  /**
   * Check if the vehicle is already parked.
   */
  public static final String CHECK_VEHICLE_REGISTRATION =
      "select PARKING_NUMBER "
          + "from ticket "
          + "where VEHICLE_REG_NUMBER = ? "
          + "and OUT_TIME is null";

  /**
   * Check if the vehicle has already been registered in the database.
   */
  public static final String CHECK_VEHICLE_RECURRENT =
      "select PARKING_NUMBER "
          + "from ticket "
          + "where VEHICLE_REG_NUMBER = ? "
          + "and OUT_TIME is not null";
}
