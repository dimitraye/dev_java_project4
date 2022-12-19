package com.parkit.parkingsystem.model;

import java.time.LocalDateTime;

/**
 * Model that represent a parking ticket.
 */
public class Ticket {
  /**
   * Id ticket
   */
  private int id;
  /**
   * Rerepresent a place of parking
   */
  private ParkingSpot parkingSpot;
  /**
   * Represent the matricule
   */
  private String vehicleRegNumber;
  /**
   * Price of the ticket
   */
  private double price;
  /**
   * Date (when the vehicule enters the parking)
   */
  private LocalDateTime inTime;
  /**
   * Date (when the vehicule leave the parking)
   */
  private LocalDateTime outTime;
  /**
   * Property that determine if the client is recurrent
   * If the matricule (vehicleRegNumber) is already saved in the database
   */
  private boolean isClient;

  public boolean isClient() {
    return isClient;
  }

  /**
   * Set a client.
   * @param client
   */
  public void setClient(boolean client) {
    isClient = client;
  }

  /**
   * Get the Id.
   * @return the Id.
   */
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  /**
   * Get the parking place the vehicle is using.
   * @return the parking place.
   */
  public ParkingSpot getParkingSpot() {
    return parkingSpot;
  }

  public void setParkingSpot(ParkingSpot parkingSpot) {
    this.parkingSpot = parkingSpot;
  }

  /**
   * Get the vehicle matricule.
   * @return the vehicle matricule.
   */
  public String getVehicleRegNumber() {
    return vehicleRegNumber;
  }

  public void setVehicleRegNumber(String vehicleRegNumber) {
    this.vehicleRegNumber = vehicleRegNumber;
  }

  /**
   * Get the price of the ticket.
   * @return the price.
   */
  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  /**
   * Get the date and time when the vehicle enters the parking.
   * @return the date and time "inTime".
   */
  public LocalDateTime getInTime() {
    return inTime;
  }

  public void setInTime(LocalDateTime inTime) {
    this.inTime = inTime;
  }

  /**
   * Get the date and time when the vehicle leaves the parking.
   * @return the date and time "outTime".
   */
  public LocalDateTime getOutTime() {
    return outTime;
  }

  public void setOutTime(LocalDateTime outTime) {
    this.outTime = outTime;
  }
}
