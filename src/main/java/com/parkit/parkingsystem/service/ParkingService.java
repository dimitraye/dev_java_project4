package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * This class is in charge of all the services related to the parking.
 */
//
public class ParkingService {

  /**
   * To have different types of messages and more detailed.
   */
  //Logger is used to make messages with more details.
  private static final Logger logger = LogManager.getLogger("ParkingService");

  /**
   * Properti that allow to use the class FareCalculatorService's methods.
   */
  // Properti that allow to use the class's methods.
  private static FareCalculatorService fareCalculatorService = new FareCalculatorService();

  /**
   * Properti that allow to use the class InputReaderUtil's methods.
   */
  // Properti that allow to use the class's methods.
  private InputReaderUtil inputReaderUtil;
  /**
   * Properti that allow to use the class ParkingSpotDAO's methods.
   */
  // Properti that allow to use the class's methods.
  private ParkingSpotDAO parkingSpotDAO;
  /**
   * Properti that allow to use the class TicketDAO's methods.
   */
  // Properti that allow to use the class's methods.
  private TicketDAO ticketDAO;

  /**
   * Allow to use the properties that have been created above.
   *
   * @param inputReaderUtil
   * @param parkingSpotDAO
   * @param ticketDAO
   */
  //
  public ParkingService(InputReaderUtil inputReaderUtil, ParkingSpotDAO parkingSpotDAO,
                        TicketDAO ticketDAO) {
    this.inputReaderUtil = inputReaderUtil;
    this.parkingSpotDAO = parkingSpotDAO;
    this.ticketDAO = ticketDAO;
  }

  /**
   * This method treats about the processus that need to be done when a vehicule enters the parking.
   *
   * @return a ticket.
   */
  // A vehicule enters the parking.
  public Ticket processIncomingVehicle() {
    Ticket ticket = new Ticket();
    try {
      String vehicleRegNumber = getVehichleRegNumber();
      if (ticketDAO.checkParkVehicule(vehicleRegNumber)) {
        throw new Exception("Error vehicule is already parked.");
      }

      if (ticketDAO.isClient(vehicleRegNumber)) {
        System.out.println(
            "Welcome back! As a recurring user of our parking lot, you'll benefit from a 5% discount.");
      }

      ParkingSpot parkingSpot = getNextParkingNumberIfAvailable();
      if (parkingSpot != null && parkingSpot.getId() > 0) {
        parkingSpot.setAvailable(false);
        parkingSpotDAO.updateParking(
            parkingSpot);//allot this parking space and mark it's availability as false

        LocalDateTime inTime = LocalDateTime.now().withNano(0);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber(vehicleRegNumber);
        ticket.setPrice(0);
        ticket.setInTime(inTime);
        ticket.setOutTime(null);
        ticketDAO.saveTicket(ticket);
        System.out.println("Generated Ticket and saved in DB");
        System.out.println("Please park your vehicle in spot number:" + parkingSpot.getId());
        System.out.println(
            "Recorded in-time for vehicle number:" + vehicleRegNumber + " is:" + inTime);
      }
    } catch (Exception e) {
      logger.error("Unable to process incoming vehicle", e);
    }
    return ticket;
  }

  /**
   * This method get the registration number of the vehicule.
   *
   * @return the registration number of the vehicule.
   * @throws Exception
   */
  // Get the registration number of the vehicule the return it.
  private String getVehichleRegNumber() throws Exception {
    System.out.println("Please type the vehicle registration number and press enter key");
    return inputReaderUtil.readVehicleRegistrationNumber();
  }

  /**
   * Method that get a parking spot for the vehicule.
   *
   * @return a parking spot.
   */
  // Get a parking Spot.
  public ParkingSpot getNextParkingNumberIfAvailable() {
    int parkingNumber = 0;
    ParkingSpot parkingSpot = null;
    try {
      ParkingType parkingType = getVehichleType();
      parkingNumber = parkingSpotDAO.getNextAvailableSlot(parkingType);
      if (parkingNumber > 0) {
        parkingSpot = new ParkingSpot(parkingNumber, parkingType, true);
      } else {
        throw new Exception("Error fetching parking number from DB. Parking slots might be full");
      }
    } catch (IllegalArgumentException ie) {
      logger.error("Error parsing user input for type of vehicle", ie);
    } catch (Exception e) {
      logger.error("Error fetching next available parking slot", e);
    }
    return parkingSpot;
  }

  /**
   * Method that get the type of the vehicule.
   *
   * @return the type of the vehicule.
   */
  // Get the type of the vehicule.
  private ParkingType getVehichleType() {
    System.out.println("Please select vehicle type from menu");
    System.out.println("1 CAR");
    System.out.println("2 BIKE");
    int input = inputReaderUtil.readSelection();
    switch (input) {
      case 1: {
        return ParkingType.CAR;
      }
      case 2: {
        return ParkingType.BIKE;
      }
      default: {
        System.out.println("Incorrect input provided");
        throw new IllegalArgumentException("Entered input is invalid");
      }
    }
  }

  /**
   * This method treats about the processus that need to be done when a vehicule leaves the parking.
   *
   * @return a ticket.
   */
  // A vehicule leaves the parking.
  public Ticket processExitingVehicle() {
    Ticket ticket = null;
    try {
      String vehicleRegNumber = getVehichleRegNumber();

      ticket = ticketDAO.getTicket(vehicleRegNumber, true);
      if (ticket == null) {
        throw new IllegalArgumentException(
            "There's no ticket assiociated to this registration number.");
      }
      ticket.setClient(ticketDAO.isClient(vehicleRegNumber));
      LocalDateTime outTime = LocalDateTime.now().withNano(0);
      ticket.setOutTime(outTime);
      fareCalculatorService.calculateFare(ticket);
      if (ticketDAO.updateTicket(ticket)) {
        ParkingSpot parkingSpot = ticket.getParkingSpot();
        parkingSpot.setAvailable(true);
        parkingSpotDAO.updateParking(parkingSpot);
        System.out.println("Please pay the parking fare:" + ticket.getPrice());
        System.out.println(
            "Recorded out-time for vehicle number:" + ticket.getVehicleRegNumber() + " is:" +
                outTime);
      } else {
        System.out.println("Unable to update ticket information. Error occurred");
      }
    } catch (Exception e) {
      logger.error("Unable to process exiting vehicle", e);
    }
    return ticket;
  }
}
