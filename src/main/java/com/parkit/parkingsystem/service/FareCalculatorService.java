package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

import java.time.temporal.ChronoUnit;

/**
 * Class used in order to calculate the price for the parking.
 */
public class FareCalculatorService {
  /**
   * This constant will be used in order to compare if the client stayed in the parking for less than 30 minutes.
   */
  public static final int FREE_TIME_THIRTY_MINS = 30;

  /**
   * Represents the minutes in 1 hour.
   */
  public static final int MINUTES_IN_HOUR = 60;

  /**
   * This constant will be used in order to reduce the price of the parking by 5%.
   */
  public static final double REDUCTION_CLIENT = 0.95;

  /**
   * This method calculs the price.
   * if the client is a regular client then he will have a 5% discount.
   * if the client stayed less than 30 minutes, he will pay nothing.
   * @param ticket
   */
  public void calculateFare(Ticket ticket) {
    if ((ticket.getOutTime() == null) || (ticket.getOutTime().isBefore(ticket.getInTime()))) {
      throw new IllegalArgumentException(
          "Out time provided is incorrect:" + ticket.getOutTime().toString());
    }

    //Todo1 : Expression de la durée de stationnement en minutes entre deux dates
    long durationInMinutes = ChronoUnit.MINUTES.between(ticket.getInTime(), ticket.getOutTime());
    //Story 2 : Mise en place (ou non) de la réduction client
    double reductionClient = ticket.isClient() ? REDUCTION_CLIENT : 1;

    //Story 1 : If the car has been parked for less than an hour the Ticket price is free.
    if (durationInMinutes < FREE_TIME_THIRTY_MINS) {
      ticket.setPrice(Fare.RATE_UNDER_HALF_HOUR);
      return;
    }

    switch (ticket.getParkingSpot().getParkingType()) {
      case CAR: {
        ticket.setPrice(
            durationInMinutes * Fare.CAR_RATE_PER_HOUR / MINUTES_IN_HOUR * reductionClient);
        break;
      }
      case BIKE: {
        ticket.setPrice(
            durationInMinutes * Fare.BIKE_RATE_PER_HOUR / MINUTES_IN_HOUR * reductionClient);
        break;
      }
      default:
        throw new IllegalArgumentException("Unkown Parking Type");
    }
  }
}