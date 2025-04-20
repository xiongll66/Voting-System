package Project2.testing.plurality;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import main.BallotFileReader;
import main.Election;
import main.InitialInput;
import main.PluralityAlgorithm;


/**
 * Unit tests for the Plurality voting algorithm in the Election system.
 * 
 * This class contains multiple test cases that simulate different election
 * scenarios using the Plurality algorithm. Each test ensures the correct
 * behavior of the Plurality algorithm when processing various voting results,
 * including ties and winners.
 */
public class PluralityAlgorithmTest {
    private Election election;
    private BallotFileReader ballotFileReader;
    private PluralityAlgorithm PluralityAlgorithm; 

    /**
     * Sets up the test environment by initializing the Election object and
     * the BallotFileReader before each test.
     */
    @BeforeEach
    public void setup() {
        this.election = new Election();
        ballotFileReader = new BallotFileReader();
    }


    /**
     * Tests the scenario where there are no ties, and only one seat is available.
     * The expected winner is "A", and the losers are "B" and "C".
     */
    @Test
    public void zeroTieOneSeatThreeCan() {
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

        PluralityAlgorithm = new PluralityAlgorithm(election); 
        PluralityAlgorithm.runAlgorithm(election.getBallots());

        List<String> expectedWinner = new ArrayList<>();
        expectedWinner.add("A");
        List<String> expectedLoser = new ArrayList<>();
        expectedLoser.add("B");
        expectedLoser.add("C");
        assertTrue(PluralityAlgorithm.getWinnerList().equals(expectedWinner));
        assertTrue(PluralityAlgorithm.getLoserList().equals(expectedLoser));

    }

    /**
     * Tests the scenario where there is a tie between candidates A and B.
     * Only one seat is available, and the loser is C.
     * The winner will be chosen randomly between A and B.
     */
    @Test
    public void twoTieOneSeatThreeCan() {
        Path csvPath = Paths.get("Project1/testing/plurality/2tie1seat.csv");
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

        PluralityAlgorithm = new PluralityAlgorithm(election); 
        PluralityAlgorithm.runAlgorithm(election.getBallots());

        List<String> expectedWinners = new ArrayList<>();
        expectedWinners.add("A");
        expectedWinners.add("B");
        List<String> expectedLoser = new ArrayList<>();
        expectedLoser.add("C");
        assertEquals(1, PluralityAlgorithm.getWinnerList().size());
        assertTrue(expectedWinners.containsAll(PluralityAlgorithm.getWinnerList()));
        assertEquals(2, PluralityAlgorithm.getLoserList().size());
        assertTrue(PluralityAlgorithm.getLoserList().containsAll(expectedLoser));
    }
    
    /**
     * Tests the scenario where there is a tie between candidates A and B,
     * and there are two seats available. The expected winners are A and B,
     * and the loser is C.
     */
    @Test
    public void twoTieTwoSeatThreeCan() {
        Path csvPath = Paths.get("Project1/testing/plurality/2tie2seat1.csv");
        assertTrue(Files.exists(csvPath), "File not found: " + csvPath);

        // Simulate user input with the correct path
        Scanner scanner = new Scanner("p\n" + csvPath.toString() + "\n2\n");
        try {
            election.promptForInput(scanner);
        } catch (Exception e) {
            e.printStackTrace();
        }
        election.processBallotFile(ballotFileReader);
        
        InitialInput input = election.getInput();
        assertNotNull(input, "Input object was not initialized");
        assertEquals("plurality", input.getAlgorithm());

        PluralityAlgorithm = new PluralityAlgorithm(election); 
        PluralityAlgorithm.runAlgorithm(election.getBallots());

        List<String> expectedWinners = new ArrayList<>();
        expectedWinners.add("A");
        expectedWinners.add("B");
        List<String> expectedLoser = new ArrayList<>();
        expectedLoser.add("C");
        assertEquals(2, PluralityAlgorithm.getWinnerList().size());
        assertTrue(expectedWinners.containsAll(PluralityAlgorithm.getWinnerList()));
        assertEquals(1, PluralityAlgorithm.getLoserList().size());
        assertTrue(PluralityAlgorithm.getLoserList().containsAll(expectedLoser));
    }

    /**
     * Tests the scenario where there are ties between candidates, and two seats
     * are available. The expected winners are A and B, with one loser, C.
     */
    @Test
    public void twoTieTwoSeatThreeCanTwo() {
        Path csvPath = Paths.get("Project1/testing/plurality/2tie2seat2.csv");
        assertTrue(Files.exists(csvPath), "File not found: " + csvPath);

        // Simulate user input with the correct path
        Scanner scanner = new Scanner("p\n" + csvPath.toString() + "\n2\n");
        try {
            election.promptForInput(scanner);
        } catch (Exception e) {
            e.printStackTrace();
        }
        election.processBallotFile(ballotFileReader);
        
        InitialInput input = election.getInput();
        assertNotNull(input, "Input object was not initialized");
        assertEquals("plurality", input.getAlgorithm());

        PluralityAlgorithm = new PluralityAlgorithm(election); 
        PluralityAlgorithm.runAlgorithm(election.getBallots());

        assertEquals(2, PluralityAlgorithm.getWinnerList().size());
        assertTrue(PluralityAlgorithm.getWinnerList().contains("A"));
        assertEquals(1, PluralityAlgorithm.getLoserList().size());
    }

    /**
     * Tests the scenario where there is a tie between three candidates, and only
     * one seat is available. The expected winner will be chosen randomly, and two
     * candidates will be losers.
     */
    @Test
    public void threeTieOneSeatThreeCan() {
        Path csvPath = Paths.get("Project1/testing/plurality/3tie1seat.csv");
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

        PluralityAlgorithm = new PluralityAlgorithm(election); 
        PluralityAlgorithm.runAlgorithm(election.getBallots());

        assertEquals(1, PluralityAlgorithm.getWinnerList().size());
        assertEquals(2, PluralityAlgorithm.getLoserList().size());
    }

    /**
     * Tests the scenario where there is a tie between three candidates, and two
     * seats are available. The expected winners will be two candidates, with one
     * loser.
     */
    @Test
    public void threeTieTwoSeatThreeCan() {
        Path csvPath = Paths.get("Project1/testing/plurality/3tie2seat1.csv");
        assertTrue(Files.exists(csvPath), "File not found: " + csvPath);

        // Simulate user input with the correct path
        Scanner scanner = new Scanner("p\n" + csvPath.toString() + "\n2\n");
        try {
            election.promptForInput(scanner);
        } catch (Exception e) {
            e.printStackTrace();
        }
        election.processBallotFile(ballotFileReader);
        
        InitialInput input = election.getInput();
        assertNotNull(input, "Input object was not initialized");
        assertEquals("plurality", input.getAlgorithm());

        PluralityAlgorithm = new PluralityAlgorithm(election); 
        PluralityAlgorithm.runAlgorithm(election.getBallots());

        assertEquals(2, PluralityAlgorithm.getWinnerList().size());
        assertEquals(1, PluralityAlgorithm.getLoserList().size());
    }

    /**
     * Tests the scenario where there is a tie between three candidates, and two
     * seats are available. The expected winners are two candidates, and there are
     * two losers.
     */
    @Test
    public void threeTieTwoSeatThreeCanTwo() {
        Path csvPath = Paths.get("Project1/testing/plurality/3tie2seat2.csv");
        assertTrue(Files.exists(csvPath), "File not found: " + csvPath);

        // Simulate user input with the correct path
        Scanner scanner = new Scanner("p\n" + csvPath.toString() + "\n2\n");
        try {
            election.promptForInput(scanner);
        } catch (Exception e) {
            e.printStackTrace();
        }
        election.processBallotFile(ballotFileReader);
        
        InitialInput input = election.getInput();
        assertNotNull(input, "Input object was not initialized");
        assertEquals("plurality", input.getAlgorithm());

        PluralityAlgorithm = new PluralityAlgorithm(election); 
        PluralityAlgorithm.runAlgorithm(election.getBallots());

        assertEquals(2, PluralityAlgorithm.getWinnerList().size());
        assertEquals(2, PluralityAlgorithm.getLoserList().size());
        assertTrue(PluralityAlgorithm.getWinnerList().contains("A"));
    }
}
