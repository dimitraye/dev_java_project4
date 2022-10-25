package com.parkit.parkingsystem;

import com.parkit.parkingsystem.service.InteractiveShell;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

 /**
 * Main class of the project
 */
// Comments
public class App {
    /**
     *
     */
    //Logger is used to make messages with more details
    private static final Logger logger = LogManager.getLogger("App");

    /**
     *
     * @param args
     */
    //
    public static void main(String[] args) {
        logger.info("Initializing Parking System");
        InteractiveShell.loadInterface();
    }
}
