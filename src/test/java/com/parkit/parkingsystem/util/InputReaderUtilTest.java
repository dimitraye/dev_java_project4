package com.parkit.parkingsystem.util;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.junit.jupiter.api.Test;

class InputReaderUtilTest {

  @Test
  void readVehicleRegistrationNumber() throws Exception {

    InputReaderUtil inputReaderUtil = new InputReaderUtil();
    String regNumber = "test";
    InputStream in = new ByteArrayInputStream(regNumber.getBytes());
    String actualResult = inputReaderUtil.readVehicleRegistrationNumber(in);

    assertEquals(regNumber, actualResult);
  }

  @Test
  void readWrongArgumentVehicleRegistrationNumber() throws Exception {

    InputReaderUtil inputReaderUtil = new InputReaderUtil();
    String regNumber = " ";
    InputStream in = new ByteArrayInputStream(regNumber.getBytes());

    assertThrows(IllegalArgumentException.class, () -> inputReaderUtil.readVehicleRegistrationNumber(in));

  }

  @Test
  void readSelectionTest() {
    InputReaderUtil inputReaderUtil = new InputReaderUtil();
    String initialString = "2";
    InputStream in = new ByteArrayInputStream(initialString.getBytes());
    int actualResult = inputReaderUtil.readSelection(in);
    int expectedResult = 2;

    assertEquals(expectedResult, actualResult);
  }

  @Test
  void readWrongSelectionTest() {
    InputReaderUtil inputReaderUtil = new InputReaderUtil();
    String initialString = "dfg";
    InputStream in = new ByteArrayInputStream(initialString.getBytes());
    int actualResult = inputReaderUtil.readSelection(in);
    int expectedResult = -1;

    assertEquals(expectedResult, actualResult);
  }
}