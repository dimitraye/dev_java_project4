package com.parkit.parkingsystem.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 *Test class for the class parkingDatabase
 */
@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

  private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
  private static ParkingSpotDAO parkingSpotDAO;
  private static TicketDAO ticketDAO;
  private static DataBasePrepareService dataBasePrepareService;

  @Mock
  private static InputReaderUtil inputReaderUtil;

  @BeforeAll
  private static void setUp() throws Exception {
    parkingSpotDAO = new ParkingSpotDAO();
    parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
    ticketDAO = new TicketDAO();
    ticketDAO.dataBaseConfig = dataBaseTestConfig;
    dataBasePrepareService = new DataBasePrepareService();
  }

  @BeforeEach
  private void setUpPerTest() throws Exception {
    when(inputReaderUtil.readSelection(System.in)).thenReturn(1);
    when(inputReaderUtil.readVehicleRegistrationNumber(System.in)).thenReturn("ABCDEF");
    dataBasePrepareService.clearDataBaseEntries();
  }

  @AfterAll
  private static void tearDown() {

  }

  /**
   * Test that when a car is parked, the status of the parking spot change to not Available
   * and the  status of the parking spot is saved in the database.
   */
  @Test
  public void testParkingACar() {
    ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
    Ticket ticket = parkingService.processIncomingVehicle();
    Ticket ticketFromDb = ticketDAO.getTicket(ticket.getVehicleRegNumber(), true);
    //Todo2 : Test1 : Traitement 1 : check that a ticket is actualy saved in DB
    assertNotNull(ticketFromDb);

    //Todo2 : Test1 : Traitement 2 : Check that the Parking table is updated with availability
    assertEquals(false,
        parkingSpotDAO.getParkingAvailability(ticketFromDb.getParkingSpot().getId()));
  }

  /**
   * Verify that when a vehicle leave its spot, the date of leaving the place is correctly saved
   * in the database and that the fare is calculated and registered in the adtabase.
   */
  @Test
  public void testParkingLotExit() {
    testParkingACar();
    ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
    Ticket ticket = parkingService.processExitingVehicle();
    Ticket ticketFromDB = ticketDAO.getTicket(ticket.getVehicleRegNumber(), false);
    //Todo2 : Test2 : Traitement 1 : check that the fare generated are populated correctly in the database
    assertNotNull(ticketFromDB);
    assertEquals(ticket.getPrice(), ticketFromDB.getPrice());


    //Todo2 : Test2 : Traitement 2 : check that out time are populated correctly in the database
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    assertEquals(ticket.getOutTime().format(formatter),
        ticketFromDB.getOutTime().format(formatter));
  }
}
