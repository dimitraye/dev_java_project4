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

/**
 * This class is in charge of all the services related to the parking.
 */
public class ParkingService {

  /**
   *  The logger allow to display logs on the application.
   */
  private static final Logger logger = LogManager.getLogger("ParkingService");

  /**
   * Property that allow to use the class FareCalculatorService's methods.
   */
  private static FareCalculatorService fareCalculatorService = new FareCalculatorService();

  /**
   * Property that allow to use the class InputReaderUtil's methods.
   */
  private InputReaderUtil inputReaderUtil;
  /**
   * Property that allow to use the class ParkingSpotDAO's methods.
   */
  private ParkingSpotDAO parkingSpotDAO;
  /**
   * Property that allow to use the class TicketDAO's methods.
   */
  private TicketDAO ticketDAO;


  public ParkingService(InputReaderUtil inputReaderUtil, ParkingSpotDAO parkingSpotDAO,
                        TicketDAO ticketDAO) {
    this.inputReaderUtil = inputReaderUtil;
    this.parkingSpotDAO = parkingSpotDAO;
    this.ticketDAO = ticketDAO;
  }

  /**
   * This method is about the process that is needed to be done when a vehicule enters the parking.
   * @return a ticket.
   */
  public Ticket processIncomingVehicle() {
    Ticket ticket = new Ticket();
    try {
      String vehicleRegNumber = getVehichleRegNumber();
      if (ticketDAO.checkParkVehicule(vehicleRegNumber)) {
        throw new Exception("Error vehicule is already parked.");
      }

      //Story 2 : Cehck if the vehicule registration number is arleady registered in the database.
      if (ticketDAO.isClient(vehicleRegNumber)) {
        System.out.println(
            "Welcome back! As a recurring user of our parking lot, you'll benefit from a 5% discount.");
      }

      ParkingSpot parkingSpot = getNextParkingNumberIfAvailable();
      if (parkingSpot != null && parkingSpot.getId() > 0) {
        parkingSpot.setAvailable(false);
        parkingSpotDAO.updateParking(parkingSpot);

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
   * @return the registration number of the vehicule.
   * @throws Exception
   */
  private String getVehichleRegNumber() throws Exception {
    System.out.println("Please type the vehicle registration number and press enter key");
    return inputReaderUtil.readVehicleRegistrationNumber(System.in);
  }

  /**
   * Method that get a parking spot number for the vehicule if it is available.
   * @return a parking spot.
   */
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
   * @return the type of the vehicule.
   */
  public ParkingType getVehichleType() {
    System.out.println("Please select vehicle type from menu");
    System.out.println("1 CAR");
    System.out.println("2 BIKE");
    int input = inputReaderUtil.readSelection(System.in);
    switch (input) {
      case 1: {
        return ParkingType.CAR;
      }
      case 2: {
        return ParkingType.BIKE;
      }
      default: {
        System.err.println("Incorrect input provided");
        throw new IllegalArgumentException("Entered input is invalid");
      }
    }
  }

  /**
   * This method treats about the processus that need to be done when a vehicule leaves the parking.
   * @return a ticket.
   */
  public Ticket processExitingVehicle() {
    Ticket ticket = null;
    try {
      String vehicleRegNumber = getVehichleRegNumber();

      ticket = ticketDAO.getTicket(vehicleRegNumber, true);
      if (ticket == null) {
        throw new IllegalArgumentException(
            "There's no ticket assiociated to this registration number.");
      }
      //Story 2 : Si le vehicule est un client régulier alors setClient ==> true
       ticket.setClient(ticketDAO.isClient(vehicleRegNumber));
     //Set nano seconds to 0 in order to compare the dates without problems
      LocalDateTime outTime = LocalDateTime.now().withNano(0);
      ticket.setOutTime(outTime);
      fareCalculatorService.calculateFare(ticket);
      if (ticketDAO.updateTicket(ticket)) {
        ParkingSpot parkingSpot = ticket.getParkingSpot();
        parkingSpot.setAvailable(true);
        parkingSpotDAO.updateParking(parkingSpot);
        System.out.println("Please pay the parking fare:" + ticket.getPrice());
        System.out.println("Recorded out-time for vehicle number:"
            + ticket.getVehicleRegNumber() + " is:"
            + outTime);
      } else {
        System.out.println("Unable to update ticket information. Error occurred");
      }
    } catch (Exception e) {
      logger.error("Unable to process exiting vehicle", e);
    }
    return ticket;
  }
}
