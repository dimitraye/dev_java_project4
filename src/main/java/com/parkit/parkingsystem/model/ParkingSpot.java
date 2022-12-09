package com.parkit.parkingsystem.model;

import com.parkit.parkingsystem.constants.ParkingType;

/**
 * Model of the Parking Spot
 */
//
public class ParkingSpot {

    /**
     * Number represent the number of the spot.
     */
    //Numer of the spot.
    private int number;

    /**
     * Type of parking spot (car / bike).
     */
    // Parking spot's type.
    private ParkingType parkingType;

    /**
     * Status of the availability of a spot.
     */
    // Availability of a spot.
    private boolean isAvailable;

    /**
     *
     * @param number
     * @param parkingType
     * @param isAvailable
     */
    //
    public ParkingSpot(int number, ParkingType parkingType, boolean isAvailable) {
        this.number = number;
        this.parkingType = parkingType;
        this.isAvailable = isAvailable;
    }

    /**
     * Method that return the number of a parking spot.
     * This number acts as an Id.
     * @return number.
     */

    public int getId() {
        return number;
    }

    /**
     *
     * @param number
     */
    //
    public void setId(int number) {
        this.number = number;
    }

    /**
     *
     * @return
     */
    //
    public ParkingType getParkingType() {
        return parkingType;
    }

    /**
     *
     * @param parkingType
     */
    //
    public void setParkingType(ParkingType parkingType) {
        this.parkingType = parkingType;
    }

    /**
     * Boolean that say if the parking spot is available.
     * @return isAvailable.
     */
    //Boolean .
    public boolean isAvailable() {
        return isAvailable;
    }

    /**
     * Method that set the value of available into isAvailable.
     * @param available.
     */
    // Affect available to isAvailable.
    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParkingSpot that = (ParkingSpot) o;
        return number == that.number;
    }

    @Override
    public int hashCode() {
        return number;
    }
}
