package hk.edu.polyu.comp.comp2021.tms.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintStream;


/**
 * Test class for Task Management System (TMS).
 *
 * @author Liu Yuyang
 * @version 1.0
 */
public class TMSTest {

    @Test
    public void testTMSConstructor() {
        TMS tms = new TMS();
    }


    private final InputStream systemIn = System.in;

    @Before
    public void setUpOutput() {
        // Mocking user input for testing
        // Note: Removed the empty line to avoid null commandLine
        String simulatedUserInput = "command1\nparam1\nparam2\nexit\n";
        System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));
    }

    @After
    public void restoreSystemInputOutput() {
        System.setIn(systemIn);
    }

    @Test
    public void testActivate() {
        TMS tms = new TMS();
        tms.activate();
    }

}
