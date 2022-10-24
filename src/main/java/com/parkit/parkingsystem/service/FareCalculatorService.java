package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

import java.util.concurrent.TimeUnit;

public class FareCalculatorService {
    public final int FREE_TIME_THIRTY_MINS = 30;
    public final int MINUTES_IN_HOUR = 60;
    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        long duration = ticket.getOutTime().getTime() - ticket.getInTime().getTime();
        long durationInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);

        if (durationInMinutes < FREE_TIME_THIRTY_MINS) {
          ticket.setPrice(Fare.RATE_UNDER_HALF_HOUR);
          return;
        }

        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice(durationInMinutes * Fare.CAR_RATE_PER_HOUR / MINUTES_IN_HOUR);
                break;
            }
            case BIKE: {
                ticket.setPrice(durationInMinutes * Fare.BIKE_RATE_PER_HOUR / MINUTES_IN_HOUR);
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
    }
}