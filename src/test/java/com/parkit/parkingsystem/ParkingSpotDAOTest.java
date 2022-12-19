package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

  private static ParkingService parkingService;

  @Mock
  private static InputReaderUtil inputReaderUtil;
  @Mock
  private static ParkingSpotDAO parkingSpotDAO;
  @Mock
  private static TicketDAO ticketDAO;


  @BeforeEach
  private void setUpPerTest() {
    parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

  }

  /**
   *
   */
  @Test
  public void processExitingVehicleTest() {

    //----------------- start of setting up mock test -----------------
    try {
      when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");

      ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
      Ticket ticket = new Ticket();
      ticket.setInTime(LocalDateTime.now().minusHours(1));
      ticket.setParkingSpot(parkingSpot);
      ticket.setVehicleRegNumber("ABCDEF");
      when(ticketDAO.getTicket(anyString(), eq(true))).thenReturn(ticket);
      when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);

      when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Failed to set up test mock objects");
    }
    //----------------- end of setting up mock test -----------------

    parkingService.processExitingVehicle();
    verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
  }

  @Test
  public void processInComingVehicleTest() {

    //----------------- start of setting up mock test -----------------
    try {
      when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
      when(inputReaderUtil.readSelection(System.in)).thenReturn(1);

      ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
      Ticket ticket = new Ticket();
      ticket.setInTime(LocalDateTime.now().minusHours(1));
      ticket.setParkingSpot(parkingSpot);
      ticket.setVehicleRegNumber("ABCDEF");
      when(ticketDAO.checkParkVehicule(anyString())).thenReturn(false);

      when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(1);
      when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

      when(ticketDAO.saveTicket(any(Ticket.class))).thenReturn(true);

    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Failed to set up test mock objects");
    }
    //----------------- end of setting up mock test -----------------

    parkingService.processIncomingVehicle();
    verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
  }


  @Test
  public void getVehicleTypeShouldReturnCar() {
    when(inputReaderUtil.readSelection(System.in)).thenReturn(1);
    ParkingType parkingType = parkingService.getVehichleType();

    assertEquals(ParkingType.CAR, parkingType);
  }

  @Test
  public void getVehicleTypeShouldReturnBike() {
    when(inputReaderUtil.readSelection(System.in)).thenReturn(2);
    ParkingType parkingType = parkingService.getVehichleType();

    assertEquals(ParkingType.BIKE, parkingType);
  }

  @Test
  public void getVehicleTypeShouldThrowException() {
    when(inputReaderUtil.readSelection(System.in)).thenReturn(3);

    assertThrows(IllegalArgumentException.class, () -> parkingService.getVehichleType());
  }
}
