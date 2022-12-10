package com.parkit.parkingsystem;

import com.parkit.parkingsystem.service.InteractiveShell;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Main class of the project.l
 */
// This class is used to call the others classes  of this project.
public class App {
  /**
   * To have different types of messages and more detailed.
   */
  //Logger is used to make messages with more details.
  private static final Logger logger = LogManager.getLogger("App");

  /**
   * Principal method of the project.
   * @param args arguments.
   */
  // This method is used to call the others classes  of this project.
  public static void main(String[] args) {
    logger.info("Initializing Parking System");
    InteractiveShell.loadInterface();
  }
}
