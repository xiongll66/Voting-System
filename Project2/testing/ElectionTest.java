package Project2.testing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import main.BallotFileReader;
import main.Election;
import main.InitialInput;

/**
 * Unit tests for Election class.
 */
public class ElectionTest {
    private Election election;
    private BallotFileReader ballotFileReader;

    @BeforeEach
    public void setup() {
        this.election = new Election();
        ballotFileReader = new BallotFileReader();
    }

    @Test
    public void testPromptForInputPlurality() {
        Path csvPath = Paths.get("Project1/testing/plurality/0tie1seat.csv");
        assertTrue(Files.exists(csvPath), "File not found: " + csvPath);

        // Simulate user input with the correct path
        Scanner scanner = new Scanner("p\n" + csvPath.toString() + "\n1\n");
        try {
            election.promptForInput(scanner);
        } catch (Exception e) {
            e.printStackTrace();
        }
        election.processBallotFile(ballotFileReader);
        
        InitialInput input = election.getInput();
        assertNotNull(input, "Input object was not initialized");
        assertEquals("plurality", input.getAlgorithm());
    }

    @Test
    public void testPromptForInputSTV() {
        Path csvPath = Paths.get("Project1/testing/stv/elimination.csv");
        assertTrue(Files.exists(csvPath), "File not found: " + csvPath);

        // Simulate user input with the correct path
        Scanner scanner = new Scanner("s\n" + csvPath.toString() + "\nauditFile\n1\n1\n");
        try {
            election.promptForInput(scanner);
        } catch (Exception e) {
            e.printStackTrace();
        }
        election.processBallotFile(ballotFileReader);
        
        InitialInput input = election.getInput();
        assertNotNull(input, "Input object was not initialized");
        assertEquals("stv", input.getAlgorithm());
    }

    @Test
    public void testPromptForInputWrongElectionTypeInput() {
        Path csvPath = Paths.get("Project1/testing/stv/elimination.csv");
        assertTrue(Files.exists(csvPath), "File not found: " + csvPath);

        // Simulate user input 't' for election type
        Scanner scanner = new Scanner("t\n" + csvPath.toString() + "\nauditFile\n1\n1\n");
        assertThrows(Exception.class, () -> {
            election.promptForInput(scanner);
        });
    }

    @Test
    public void testPromptForInputEmptyElectionTypeInput() {
        Path csvPath = Paths.get("Project1/testing/stv/elimination.csv");
        assertTrue(Files.exists(csvPath), "File not found: " + csvPath);

        // Simulate user inputting an empty input for election type
        Scanner scanner = new Scanner("\n" + csvPath.toString() + "\nauditFile\n1\n1\n");
        assertThrows(Exception.class, () -> {
            election.promptForInput(scanner);
        });
    }

    @Test
    public void testPromptForInputInvalidCSV() {
        // Simulate user inputting an invalid csv file
        Scanner scanner = new Scanner("p\ninvalid.csv\nauditFile\n1\n1\n");
        assertThrows(Exception.class, () -> {
            election.promptForInput(scanner);
        });
    }
    
}
