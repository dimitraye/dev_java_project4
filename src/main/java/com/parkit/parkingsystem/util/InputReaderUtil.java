package com.parkit.parkingsystem.util;

import java.io.InputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

/**
 * This class will read different inputs.
 */
public class InputReaderUtil {

  /**
   * Object that will contain the simple inputs like int, double, Strings, etc.
   */
  private static Scanner scan;

  /**
   *  The logger allow to display logs on the application.
   */
  private static final Logger logger = LogManager.getLogger("InputReaderUtil");

  /**
   * Method used to read the choice made by the user between 1, 2 and 3.
   *
   * @return input.
   */
  public int readSelection(InputStream in) {
    scan = new Scanner(in, "UTF-8");
    try {
      int input = Integer.parseInt(scan.nextLine());
      return input;
    } catch (Exception e) {
      logger.error("Error while reading user input from Shell", e);
      System.err.println("Error reading input. Please enter valid number for proceeding further");
      return -1;
    }
  }

  /**
   * Method used in order to read the number registration of the vehicule.
   *
   * @return the number of the vehicule.
   * @throws Exception
   */
  public String readVehicleRegistrationNumber(InputStream in) throws Exception {
    scan = new Scanner(in, "UTF-8");
    try {
      String vehicleRegNumber = scan.nextLine();
      if (vehicleRegNumber == null || vehicleRegNumber.trim().length() == 0) {
        throw new IllegalArgumentException("Invalid input provided");
      }
      return vehicleRegNumber;
    } catch (Exception e) {
      logger.error("Error while reading user input from Shell", e);
      System.err.println(
          "Error reading input. Please enter a valid string for vehicle registration number");
      throw e;
    }
  }


}
