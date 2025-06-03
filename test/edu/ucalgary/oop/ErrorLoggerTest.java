package edu.ucalgary.oop;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;

public class ErrorLoggerTest {

    @Test
    public void testConstructorDefault() {
        ErrorLogger logger = new ErrorLogger();
        assertNotNull("Logger default constructor exists", logger);
    }

    @Test
    public void testConstructorPath() {
        ErrorLogger logger = new ErrorLogger("mylog.txt");
        assertNotNull(logger);
    }

    @Test
    public void testLogErrorException() {
        String tempPath = "test_errorlog.txt";
        ErrorLogger logger = new ErrorLogger(tempPath);
        try {
            logger.logError(new Exception("TestEx"));
        } catch (Exception e) {
            fail("Should not throw exception: " + e.getMessage());
        }
        File f = new File(tempPath);
        assertTrue(f.exists());
        f.delete();
    }

    @Test
    public void testLogErrorMessageAndException() {
        String tempPath = "test_errorlog2.txt";
        ErrorLogger logger = new ErrorLogger(tempPath);
        try {
            logger.logError("Custom message", new RuntimeException("RunEx"));
        } catch (Exception e) {
            fail("Should not fail logging: " + e.getMessage());
        }
        File f = new File(tempPath);
        assertTrue(f.exists());
        f.delete();
    }
}
