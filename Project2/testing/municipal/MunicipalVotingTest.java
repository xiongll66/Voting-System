package Project2.testing.municipal;

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

import main.InitialInput;
import p2main.BallotFileReader;
import p2main.Election;
import p2main.MunicipalAlgorithm;


/**
 * Unit tests for the Municipal voting algorithm in the Election system.
 * 
 * This class contains multiple test cases that simulate different election
 * scenarios using the Municipal algorithm. Each test ensures the correct
 * behavior of the Municipal algorithm when processing various voting results,
 * including ties and winners.
 */
public class MunicipalVotingTest {
    private Election election;
    private BallotFileReader ballotFileReader;
    private MunicipalAlgorithm municipalAlgorithm; 

     /**
     * Sets up the test environment by initializing the Election object and
     * the BallotFileReader before each test.
     */
    @BeforeEach
    public void setup() {
        this.election = new Election();
        ballotFileReader = new BallotFileReader();
    }

    @Test
    public void zeroTieOneSeatThreeCan() {
        Path csvPath = Paths.get("Project2/testing/municipal/0tie1seat.csv");
        assertTrue(Files.exists(csvPath), "File not found: " + csvPath);

        // Simulate user input with the correct path
        Scanner scanner = new Scanner(csvPath.toString());
        try {
            election.promptForInput(scanner, ballotFileReader);
        } catch (Exception e) {
            e.printStackTrace();
        }
        election.processBallotFile(ballotFileReader);

        municipalAlgorithm = new MunicipalAlgorithm(election); 
        municipalAlgorithm.runAlgorithm(election.getBallots());

        List<String> expectedWinners = new ArrayList<>();
        expectedWinners.add("A");
        List<String> expectedLoser = new ArrayList<>();
        expectedLoser.add("C");
        expectedLoser.add("B");
        assertEquals(1, municipalAlgorithm.getWinnerList().size());
        assertTrue(expectedWinners.containsAll(municipalAlgorithm.getWinnerList()));
        assertEquals(2, municipalAlgorithm.getLoserList().size());
        assertTrue(municipalAlgorithm.getLoserList().containsAll(expectedLoser));
    }

    @Test
    public void zeroTieTwoSeatThreeCan() {
        Path csvPath = Paths.get("Project2/testing/municipal/0tie2seat.csv");
        assertTrue(Files.exists(csvPath), "File not found: " + csvPath);

        // Simulate user input with the correct path
        Scanner scanner = new Scanner(csvPath.toString());
        try {
            election.promptForInput(scanner, ballotFileReader);
        } catch (Exception e) {
            e.printStackTrace();
        }
        election.processBallotFile(ballotFileReader);

        municipalAlgorithm = new MunicipalAlgorithm(election); 
        municipalAlgorithm.runAlgorithm(election.getBallots());

        List<String> expectedWinners = new ArrayList<>();
        expectedWinners.add("B");
        expectedWinners.add("A");
        List<String> expectedLoser = new ArrayList<>();
        expectedLoser.add("C");
        assertEquals(2, municipalAlgorithm.getWinnerList().size());
        assertTrue(expectedWinners.containsAll(municipalAlgorithm.getWinnerList()));
        assertEquals(1, municipalAlgorithm.getLoserList().size());
        assertTrue(municipalAlgorithm.getLoserList().containsAll(expectedLoser));
    }
    
    @Test
    public void oneTieOneSeatThreeCan() {
        Path csvPath = Paths.get("Project2/testing/municipal/1tie1seat.csv");
        assertTrue(Files.exists(csvPath), "File not found: ");

        // Simulate user input with the correct path
        Scanner scanner = new Scanner(csvPath.toString());
        try {
            election.promptForInput(scanner, ballotFileReader);
        } catch (Exception e) {
            e.printStackTrace();
        }
        election.processBallotFile(ballotFileReader);

        municipalAlgorithm = new MunicipalAlgorithm(election); 
        municipalAlgorithm.runAlgorithm(election.getBallots());

        List<String> expectedLoser = new ArrayList<>();
        expectedLoser.add("A");
        expectedLoser.add("B");
        expectedLoser.add("C");

        assertEquals(1, municipalAlgorithm.getWinnerList().size());
        assertTrue(municipalAlgorithm.getWinnerList().get(0).equals("A") || 
        municipalAlgorithm.getWinnerList().get(0).equals("B"));


        assertEquals(2, municipalAlgorithm.getLoserList().size());
        assertTrue(expectedLoser.containsAll(municipalAlgorithm.getLoserList()));
    }

    @Test
    public void oneTieTwoSeatThreeCan() {
        Path csvPath = Paths.get("Project2/testing/municipal/1tie2seat.csv");
        assertTrue(Files.exists(csvPath), "File not found: " + csvPath);

        // Simulate user input with the correct path
        Scanner scanner = new Scanner(csvPath.toString());
        try {
            election.promptForInput(scanner, ballotFileReader);
        } catch (Exception e) {
            e.printStackTrace();
        }
        election.processBallotFile(ballotFileReader);

        municipalAlgorithm = new MunicipalAlgorithm(election); 
        municipalAlgorithm.runAlgorithm(election.getBallots());

        assertEquals(2, municipalAlgorithm.getWinnerList().size());
        assertTrue(municipalAlgorithm.getWinnerList().contains("A") && 
        municipalAlgorithm.getWinnerList().contains("B"));


        assertEquals(1, municipalAlgorithm.getLoserList().size());
        assertTrue(municipalAlgorithm.getLoserList().contains("C"));
    }

    @Test
    public void twoTieTwoSeatThreeCan() {
        Path csvPath = Paths.get("Project2/testing/municipal/2tie2seat.csv");
        assertTrue(Files.exists(csvPath), "File not found: " + csvPath);

        // Simulate user input with the correct path
        Scanner scanner = new Scanner( csvPath.toString());
        try {
            election.promptForInput(scanner, ballotFileReader);
        } catch (Exception e) {
            e.printStackTrace();
        }
        election.processBallotFile(ballotFileReader);

        municipalAlgorithm = new MunicipalAlgorithm(election); 
        municipalAlgorithm.runAlgorithm(election.getBallots());

        assertEquals(2, municipalAlgorithm.getWinnerList().size());
        assertTrue(
        (municipalAlgorithm.getWinnerList().contains("A") && 
        municipalAlgorithm.getWinnerList().contains("B")) 
        || 
        (municipalAlgorithm.getWinnerList().contains("B") && 
        municipalAlgorithm.getWinnerList().contains("C")) 
        || 
        (municipalAlgorithm.getWinnerList().contains("C") && 
        municipalAlgorithm.getWinnerList().contains("A"))
        );


        assertEquals(1, municipalAlgorithm.getLoserList().size());
        assertTrue(municipalAlgorithm.getLoserList().contains("C") ||
        municipalAlgorithm.getLoserList().contains("A") ||
        municipalAlgorithm.getLoserList().contains("B"));
    }

    @Test
    public void threeTieOneSeatThreeCan() {
        Path csvPath = Paths.get("Project2/testing/municipal/3tie1seat.csv");
        assertTrue(Files.exists(csvPath), "File not found: " + csvPath);

        // Simulate user input with the correct path
        Scanner scanner = new Scanner(csvPath.toString());
        try {
            election.promptForInput(scanner, ballotFileReader);
        } catch (Exception e) {
            e.printStackTrace();
        }
        election.processBallotFile(ballotFileReader);
        

        municipalAlgorithm = new MunicipalAlgorithm(election); 
        municipalAlgorithm.runAlgorithm(election.getBallots());

        assertEquals(1, municipalAlgorithm.getWinnerList().size());
        assertTrue(
            municipalAlgorithm.getWinnerList().contains("C") ||
            municipalAlgorithm.getWinnerList().contains("A") ||
            municipalAlgorithm.getLoserList().contains("B"));


        assertEquals(2, municipalAlgorithm.getLoserList().size());

        assertTrue(
            (municipalAlgorithm.getLoserList().contains("A") && 
            municipalAlgorithm.getLoserList().contains("B")) 
            || 
            (municipalAlgorithm.getLoserList().contains("B") && 
            municipalAlgorithm.getLoserList().contains("C")) 
            || 
            (municipalAlgorithm.getLoserList().contains("C") && 
            municipalAlgorithm.getLoserList().contains("A")
            )
        );
    }

    @Test
    public void threeTieTwoSeatThreeCanOne() {
        Path csvPath = Paths.get("Project2/testing/municipal/3tie2seat1.csv");
        assertTrue(Files.exists(csvPath), "File not found: " + csvPath);

        // Simulate user input with the correct path
        Scanner scanner = new Scanner( csvPath.toString() );
        try {
            election.promptForInput(scanner, ballotFileReader);
        } catch (Exception e) {
            e.printStackTrace();
        }
        election.processBallotFile(ballotFileReader);
        
        
        municipalAlgorithm = new MunicipalAlgorithm(election); 
        municipalAlgorithm.runAlgorithm(election.getBallots());

        assertEquals(2, municipalAlgorithm.getWinnerList().size());
        assertTrue(
            (municipalAlgorithm.getWinnerList().contains("A") && 
        municipalAlgorithm.getWinnerList().contains("B")) 
        || 
        (municipalAlgorithm.getWinnerList().contains("B") && 
        municipalAlgorithm.getWinnerList().contains("C")) 
        || 
        (municipalAlgorithm.getWinnerList().contains("C") && 
        municipalAlgorithm.getWinnerList().contains("A")));


        assertEquals(1, municipalAlgorithm.getLoserList().size());

        assertTrue(municipalAlgorithm.getLoserList().contains("C") ||
        municipalAlgorithm.getLoserList().contains("A") ||
        municipalAlgorithm.getLoserList().contains("B"));
    }

    @Test
    public void threeTieTwoSeatThreeCanTwo() {
        Path csvPath = Paths.get("Project2/testing/municipal/3tie2seat2.csv");
        assertTrue(Files.exists(csvPath), "File not found: " + csvPath);

        // Simulate user input with the correct path
        Scanner scanner = new Scanner(csvPath.toString());
        try {
            election.promptForInput(scanner, ballotFileReader);
        } catch (Exception e) {
            e.printStackTrace();
        }
        election.processBallotFile(ballotFileReader);
      

        municipalAlgorithm = new MunicipalAlgorithm(election); 
        municipalAlgorithm.runAlgorithm(election.getBallots());

        assertEquals(2, municipalAlgorithm.getWinnerList().size());
        assertTrue(
            (municipalAlgorithm.getWinnerList().contains("A") && 
        municipalAlgorithm.getWinnerList().contains("B")) 
        || 
        (municipalAlgorithm.getWinnerList().contains("A") && 
        municipalAlgorithm.getWinnerList().contains("C")) 
        || 
        (municipalAlgorithm.getWinnerList().contains("A") && 
        municipalAlgorithm.getWinnerList().contains("D"))
        || 
        (municipalAlgorithm.getWinnerList().contains("B") && 
        municipalAlgorithm.getWinnerList().contains("C"))
        || 
        (municipalAlgorithm.getWinnerList().contains("B") && 
        municipalAlgorithm.getWinnerList().contains("D"))
        || 
        (municipalAlgorithm.getWinnerList().contains("C") && 
        municipalAlgorithm.getWinnerList().contains("D")));


        assertEquals(2, municipalAlgorithm.getLoserList().size());

        assertTrue((municipalAlgorithm.getLoserList().contains("A") && 
        municipalAlgorithm.getLoserList().contains("B")) 
        || 
        (municipalAlgorithm.getLoserList().contains("A") && 
        municipalAlgorithm.getLoserList().contains("C")) 
        || 
        (municipalAlgorithm.getLoserList().contains("A") && 
        municipalAlgorithm.getLoserList().contains("D"))
        || 
        (municipalAlgorithm.getLoserList().contains("B") && 
        municipalAlgorithm.getLoserList().contains("C"))
        || 
        (municipalAlgorithm.getLoserList().contains("B") && 
        municipalAlgorithm.getLoserList().contains("D"))
        || 
        (municipalAlgorithm.getLoserList().contains("C") && 
        municipalAlgorithm.getLoserList().contains("D")));
    }
}
