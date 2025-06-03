package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CommandLineInterfaceTest {

    private CommandLineInterface cli;

    @Before
    public void setUp() {
        CommandLineInterface.resetInstance();
        cli = CommandLineInterface.getInstance();
    }

    @Test
    public void testSingletonInstance() {
        CommandLineInterface cli2 = CommandLineInterface.getInstance();
        assertSame("Should return the same singleton instance", cli, cli2);
    }

    @Test
    public void testIsRunningInitiallyFalse() {
        assertFalse(cli.isRunning());
    }

    @Test
    public void testExitApplicationMakesNotRunning() {
        cli.exitApplication();
        assertFalse(cli.isRunning());
    }
}
