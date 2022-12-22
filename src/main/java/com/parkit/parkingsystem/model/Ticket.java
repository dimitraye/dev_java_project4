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
   * Rerepresent a place of parking.
   */
  private ParkingSpot parkingSpot;
  /**
   * Represent the matricule.
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


  public void setClient(boolean client) {
    isClient = client;
  }


  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }


  public ParkingSpot getParkingSpot() {
    return parkingSpot;
  }

  public void setParkingSpot(ParkingSpot parkingSpot) {
    this.parkingSpot = parkingSpot;
  }


  public String getVehicleRegNumber() {
    return vehicleRegNumber;
  }

  public void setVehicleRegNumber(String vehicleRegNumber) {
    this.vehicleRegNumber = vehicleRegNumber;
  }


  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }


  public LocalDateTime getInTime() {
    return inTime;
  }

  public void setInTime(LocalDateTime inTime) {
    this.inTime = inTime;
  }

  public LocalDateTime getOutTime() {
    return outTime;
  }

  public void setOutTime(LocalDateTime outTime) {
    this.outTime = outTime;
  }
}
