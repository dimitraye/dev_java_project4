package com.parkit.parkingsystem.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

/**
 * This class will read different inputs.
 */
// Read different inputs.
public class InputReaderUtil {

    /**
     * Object that will contain the simple inputs like int, double, Strings, etc.
     */
    // Read inputs
    private static Scanner scan = new Scanner(System.in);
    /**
     * To have different types of messages and more detailed.
     */
    //Logger is used to make messages with more details
    private static final Logger logger = LogManager.getLogger("InputReaderUtil");

    /**
     * Method used to read the choice made by the user between 1, 2 and 3.
     * @return input.
     */
    // Read the choice made by the user.
    public int readSelection() {
        try {
            int input = Integer.parseInt(scan.nextLine());
            return input;
        } catch (Exception e) {
            logger.error("Error while reading user input from Shell", e);
            System.out.println("Error reading input. Please enter valid number for proceeding further");
            return -1;
        }
    }

    /**
     * Method used in order to read the number registration of the vehicule.
     * @return the number of the vehicule.
     * @throws Exception
     */
    // Read the number registration of the vehicule.
    public String readVehicleRegistrationNumber() throws Exception {
        try {
            String vehicleRegNumber = scan.nextLine();
            if (vehicleRegNumber == null || vehicleRegNumber.trim().length() == 0) {
                throw new IllegalArgumentException("Invalid input provided");
            }
            return vehicleRegNumber;
        } catch (Exception e) {
            logger.error("Error while reading user input from Shell", e);
            System.out.println("Error reading input. Please enter a valid string for vehicle registration number");
            throw e;
        }
    }


}
