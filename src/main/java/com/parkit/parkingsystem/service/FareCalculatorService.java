package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

public class FareCalculatorService {
    public final int FREE_TIME_THIRTY_MINS = 30;
    public final int MINUTES_IN_HOUR = 60;
    public final double REDUCTION_CLIENT = 0.95;

    public void calculateFare(Ticket ticket){

        if( (ticket.getOutTime() == null) || (ticket.getOutTime().isBefore(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        long durationInMinutes = ChronoUnit.MINUTES.between(ticket.getInTime(), ticket.getOutTime());
        double reductionClient = ticket.isClient() ? REDUCTION_CLIENT : 1;

        if (durationInMinutes < FREE_TIME_THIRTY_MINS) {
          ticket.setPrice(Fare.RATE_UNDER_HALF_HOUR);
          return;
        }

        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice(durationInMinutes * Fare.CAR_RATE_PER_HOUR / MINUTES_IN_HOUR * reductionClient);
                break;
            }
            case BIKE: {
                ticket.setPrice(durationInMinutes * Fare.BIKE_RATE_PER_HOUR / MINUTES_IN_HOUR * reductionClient);
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
    }
}