package Project1.testing.plurality;

import main.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.util.Scanner;

public class PluralityAlgorithmTest {
    private Election election;
    private PluralityAlgorithm algorithm;

    @BeforeEach
    void setUp() {
        election = new Election();
        algorithm = new PluralityAlgorithm(election); 
    }
    
    @Test
    public void zeroTieOneSeatThreeCandidate () {
        // Simulate user input
        String simulatedInput = "p\n0tie1seat.csv\n1\n";
        InputStream originalSystemIn = System.in; // Save the original System.in
        try {
            // Set System.in to the simulated input
            System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

            // Run the main method of your Election class
            Election.main(new String[0]);

            // Add assertions here to verify the behavior of your program
            // For example, check the output or the state of your Election object

        } finally {
            // Restore the original System.in
            System.setIn(originalSystemIn);
        }
    }
    
    
}
