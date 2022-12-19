package com.parkit.parkingsystem.model;

import com.parkit.parkingsystem.constants.ParkingType;

/**
 * Model of the Parking Spot.
 */
//
public class ParkingSpot {

    private int number;

    private ParkingType parkingType;

    // Availability of a spot.
    private boolean isAvailable;

    /**
     * Verify if a parking spot is empty or already taken.
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
    public void setId(int number) {
        this.number = number;
    }

    /**
     *
     * @return
     */
    public ParkingType getParkingType() {
        return parkingType;
    }

    /**
     *
     * @param parkingType
     */
    public void setParkingType(ParkingType parkingType) {
        this.parkingType = parkingType;
    }

    /**
     * Boolean that say if the parking spot is available.
     * @return isAvailable.
     */
    public boolean isAvailable() {
        return isAvailable;
    }

    /**
     * Method that set the value of available into isAvailable.
     * @param available
     */
    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    /**
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParkingSpot that = (ParkingSpot) o;
        return number == that.number;
    }

    /**
     *
     * @return
     */
    @Override
    public int hashCode() {
        return number;
    }
}
