package com.parkit.parkingsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Test class for the class FareCalculatorService.
 */
public class FareCalculatorServiceTest {


  private static FareCalculatorService fareCalculatorService;
  private Ticket ticket;

  @BeforeAll
  private static void setUp() {
    fareCalculatorService = new FareCalculatorService();
  }

  @BeforeEach
  private void setUpPerTest() {
    ticket = new Ticket();
  }

  /**
   * Verify that when a car is parked, the price is calulated correctly.
   */
  @Test
  public void calculateFareCar() {
    LocalDateTime inTime = LocalDateTime.now().minusHours(1);
    LocalDateTime outTime = LocalDateTime.now();
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

    ticket.setInTime(inTime);
    ticket.setOutTime(outTime);
    ticket.setParkingSpot(parkingSpot);
    fareCalculatorService.calculateFare(ticket);

    long durationInMinutes = ChronoUnit.MINUTES.between(ticket.getInTime(), ticket.getOutTime());
    double expectedFare = Fare.CAR_RATE_PER_HOUR / FareCalculatorService.MINUTES_IN_HOUR * durationInMinutes;
    double actualFare = ticket.getPrice();

    assertEquals(expectedFare, actualFare);
  }

  /**
   * Verify that when a bike is parked, the price is calulated correctly.
   */
  @Test
  public void calculateFareBike() {
    LocalDateTime inTime = LocalDateTime.now().minusHours(1);
    LocalDateTime outTime = LocalDateTime.now();
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

    ticket.setInTime(inTime);
    ticket.setOutTime(outTime);
    ticket.setParkingSpot(parkingSpot);
    fareCalculatorService.calculateFare(ticket);

    long durationInMinutes = ChronoUnit.MINUTES.between(ticket.getInTime(), ticket.getOutTime());
    double expectedFare = Fare.BIKE_RATE_PER_HOUR / FareCalculatorService.MINUTES_IN_HOUR * durationInMinutes;
    double actualFare = ticket.getPrice();

    assertEquals(expectedFare, actualFare);
  }

  /**
   * Verify that an exception is thrown when we try to calculate the fare and the type of vehicle
   * is null.
   */
  @Test
  public void calculateFareUnkownType() {
    LocalDateTime inTime = LocalDateTime.now().minusHours(1);
    LocalDateTime outTime = LocalDateTime.now();
    ParkingSpot parkingSpot = new ParkingSpot(1, null, false);

    ticket.setInTime(inTime);
    ticket.setOutTime(outTime);
    ticket.setParkingSpot(parkingSpot);
    assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
  }

  /**
   * Verify that an exception is thrown when we try to calculate the fare but the time of arrival
   * is after the time of exit
   */
  @Test
  public void calculateFareBikeWithFutureInTime() {
    LocalDateTime inTime = LocalDateTime.now().plusHours(1);
    LocalDateTime outTime = LocalDateTime.now();
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

    ticket.setInTime(inTime);
    ticket.setOutTime(outTime);
    ticket.setParkingSpot(parkingSpot);
    assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
  }

  /**
   * Verify that the price for a bike that stayed for less than an hour is calculated correctly.
   */
  @Test
  public void calculateFareBikeWithLessThanOneHourParkingTime() {
    LocalDateTime inTime = LocalDateTime.now().minusMinutes(45);
    LocalDateTime outTime = LocalDateTime.now();
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

    ticket.setInTime(inTime);
    ticket.setOutTime(outTime);
    ticket.setParkingSpot(parkingSpot);
    fareCalculatorService.calculateFare(ticket);
    assertEquals((0.75 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
  }

  /**
   * Verify that the price for a car that stayed for less than an hour is calculated correctly.
   */
  @Test
  public void calculateFareCarWithLessThanOneHourParkingTime() {
    LocalDateTime inTime = LocalDateTime.now().minusMinutes(45);
    LocalDateTime outTime = LocalDateTime.now();
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

    ticket.setInTime(inTime);
    ticket.setOutTime(outTime);
    ticket.setParkingSpot(parkingSpot);
    fareCalculatorService.calculateFare(ticket);
    assertEquals((0.75 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
  }

  /**
   * Verify that the price for a car that stayed for more than a day is calculated correctly.
   */
  @Test
  public void calculateFareCarWithMoreThanADayParkingTime() {
    LocalDateTime inTime = LocalDateTime.now().minusHours(24);
    LocalDateTime outTime = LocalDateTime.now();
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

    ticket.setInTime(inTime);
    ticket.setOutTime(outTime);
    ticket.setParkingSpot(parkingSpot);
    fareCalculatorService.calculateFare(ticket);
    assertEquals((24 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
  }

  /**
   * Verify that the price for a vehicle that stayed for less than thirty minutes is
   * calculated correctly (it's should be free).
   */
  @Test
  public void calculateFareWithLessThanThirtyMinutesParkingTime() {
    LocalDateTime inTime = LocalDateTime.now().minusMinutes(25);
    LocalDateTime outTime = LocalDateTime.now();
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

    ticket.setInTime(inTime);
    ticket.setOutTime(outTime);
    ticket.setParkingSpot(parkingSpot);
    fareCalculatorService.calculateFare(ticket);
    assertEquals(Fare.RATE_UNDER_HALF_HOUR, ticket.getPrice());
  }

  /**
   * Verify that the price for a vehicle that stayed for more than a day is calculated correctly
   * with the client reduction because this time the client is a recurrent client.
   */
  @Test
  public void calculateFareWithMoreThanADayParkingTimeForRecurrentClient() {
    LocalDateTime inTime = LocalDateTime.now().minusHours(24);
    LocalDateTime outTime = LocalDateTime.now();
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
    ticket.setClient(true);
    ticket.setInTime(inTime);
    ticket.setOutTime(outTime);
    ticket.setParkingSpot(parkingSpot);
    fareCalculatorService.calculateFare(ticket);

    assertEquals((24 * Fare.CAR_RATE_PER_HOUR * fareCalculatorService.REDUCTION_CLIENT),
        ticket.getPrice());
  }
}
