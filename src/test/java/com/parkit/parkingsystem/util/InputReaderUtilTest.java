package com.parkit.parkingsystem.util;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import org.junit.jupiter.api.Test;

/**
 * Test the reading of the different inputs.
 */
class InputReaderUtilTest {

  /**
   * Test that the registration number is read.
   * @throws Exception
   */
  @Test
  void readVehicleRegistrationNumber() throws Exception {

    InputReaderUtil inputReaderUtil = new InputReaderUtil();
    String regNumber = "test";
    InputStream in = new ByteArrayInputStream(regNumber.getBytes("UTF-8"));
    String actualResult = inputReaderUtil.readVehicleRegistrationNumber(in);

    assertEquals(regNumber, actualResult);
  }

  /**
   * Verify that an exception is thrown when the vehicle registration number is incorrect.
   * @throws Exception
   */
  @Test
  void readWrongArgumentVehicleRegistrationNumber() throws Exception {

    InputReaderUtil inputReaderUtil = new InputReaderUtil();
    String regNumber = " ";
    InputStream in = new ByteArrayInputStream(regNumber.getBytes("UTF-8"));

    assertThrows(IllegalArgumentException.class, () -> inputReaderUtil.readVehicleRegistrationNumber(in));

  }

  /**
   * Verify that the input entered is a string that can be converted into an integer.
   */
  @Test
  void readSelectionTest() {
    InputReaderUtil inputReaderUtil = new InputReaderUtil();
    String initialString = "2";
    InputStream in = new ByteArrayInputStream(initialString.getBytes(Charset.defaultCharset()));
    int actualResult = inputReaderUtil.readSelection(in);
    int expectedResult = 2;

    assertEquals(expectedResult, actualResult);
  }

  /**
   * Verify that -1 is returned when the string entered cannot be converted into an integer.
   */
  @Test
  void readWrongSelectionTest() {
    InputReaderUtil inputReaderUtil = new InputReaderUtil();
    String initialString = "dfg";
    InputStream in = new ByteArrayInputStream(initialString.getBytes(Charset.defaultCharset()));
    int actualResult = inputReaderUtil.readSelection(in);
    int expectedResult = -1;

    assertEquals(expectedResult, actualResult);
  }
}