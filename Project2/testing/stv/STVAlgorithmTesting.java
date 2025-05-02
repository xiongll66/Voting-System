

package Project2.testing.stv;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Project1.src.main.STVInput;
import p2main.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class STVAlgorithmTesting {
    private STVAlgorithm stvAlgorithm;
    private Election election;
    private BallotFileReader ballotFileReader;


    @BeforeEach
    void setUp() {
        election = new Election();
        ballotFileReader = new BallotFileReader();
        stvAlgorithm = null;
    }

    @Test
    void testCalculateDroopQuota() {
        // Test with 100 ballots and 2 seats
        stvAlgorithm.calculateDroopQuota(100, 2);
        assertEquals(34, stvAlgorithm.droopQuota); // 100/(2+1) + 1 = 34
        
        
        // Test edge case with minimal ballots
        stvAlgorithm.calculateDroopQuota(3,  1);
        assertEquals(2, stvAlgorithm.droopQuota); // 3/(1+1) + 1 = 2
    }

    @Test
    void testShuffleBallots() {
        List<Ballot> testBallots = new ArrayList<>();
        testBallots.add(new STVBallot(1, new int[]{1, 2, 3}));
        testBallots.add(new STVBallot(2, new int[]{1, 3, 2}));
        testBallots.add(new STVBallot(3, new int[]{2, 1, 3}));
        
        List<Ballot> original = new ArrayList<>(testBallots);

        // Shuffle off - should remain same
        stvAlgorithm.shuffleBallots(testBallots, false);
        assertIterableEquals(original, testBallots);

        // Shuffle on - should be different
        stvAlgorithm.shuffleBallots(testBallots, true);
        assertNotEquals(original, testBallots);
    }

    @Test
    void testRedistributeCandidateBallots() {
        List<Ballot> ballots = new ArrayList<>();
        // Create test ballots (5 ballots, 3 candidates)
        ballots.add(new STVBallot(1, new int[]{1, 2, 3})); // A first
        ballots.add(new STVBallot(2, new int[]{1, 2, 3})); // A first
        ballots.add(new STVBallot(3, new int[]{1, 3, 2})); // B first 
        ballots.add(new STVBallot(4, new int[]{3, 1, 2})); // B first
        ballots.add(new STVBallot(5, new int[]{2, 3, 1})); // C first
        stvAlgorithm.redistributeCandidateBallots(ballots);

        // Verify results (quota = 3 votes needed to win)
        assertEquals(1, stvAlgorithm.winnerList.size()); // only one winner
        assertEquals("A", stvAlgorithm.winnerList.get(0)); // A wins with 3 votes
        
        assertEquals(2, stvAlgorithm.loserList.size()); // two losers
        assertEquals("B", stvAlgorithm.loserList.get(0)); // B eliminated first
        assertEquals("C", stvAlgorithm.loserList.get(1)); // C eliminated second
    }

    @Test
    public void elimination() {
        Path csvPath = Paths.get("Project2/testing/municipal/elimination.csv");
        assertTrue(Files.exists(csvPath), "File not found: " + csvPath);

        // get user input with the correct path
        Scanner scanner = new Scanner(csvPath.toString());
        try {
            election.promptForInput(scanner, ballotFileReader);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }

        election.processBallotFile(ballotFileReader);

        stvAlgorithm = new STVAlgorithm(election); 
        stvAlgorithm.runAlgorithm(election.getBallots());

        assertEquals(1, stvAlgorithm.winnerList.size()); // should have 1 winner 
        assertEquals("A", stvAlgorithm.winnerList.get(0)); // winner shouold be A

        assertEquals(2, stvAlgorithm.loserList.size()); // two losers
        assertEquals("C", stvAlgorithm.loserList.get(0)); // C eliminated first
        assertEquals("B", stvAlgorithm.loserList.get(1));

    }

    @Test
    public void eliminationTie() {
        Path csvPath = Paths.get("Project2/testing/municipal/eliminationTie.csv");
        assertTrue(Files.exists(csvPath), "File not found: " + csvPath);

        // get user input with the correct path
        Scanner scanner = new Scanner(csvPath.toString());
        try {
            election.promptForInput(scanner, ballotFileReader);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }

        election.processBallotFile(ballotFileReader);

        stvAlgorithm = new STVAlgorithm(election); 
        stvAlgorithm.runAlgorithm(election.getBallots());

        assertEquals(1, stvAlgorithm.winnerList.size()); // should have 1 winner 
        assertEquals("A", stvAlgorithm.winnerList.get(0)); // winner shouold be A

        assertEquals(2, stvAlgorithm.loserList.size()); // two losers
        assertEquals("C", stvAlgorithm.loserList.get(0)); // C eliminated first

    }

    @Test
    public void quotaMet() {
        Path csvPath = Paths.get("Project2/testing/municipal/quotaMet.csv");
        assertTrue(Files.exists(csvPath), "File not found: " + csvPath);

        // get user input with the correct path
        Scanner scanner = new Scanner(csvPath.toString());
        try {
            election.promptForInput(scanner, ballotFileReader);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }

        election.processBallotFile(ballotFileReader);

        stvAlgorithm = new STVAlgorithm(election); 
        stvAlgorithm.runAlgorithm(election.getBallots());
        
        assertEquals(1, stvAlgorithm.winnerList.size()); // should have 1 winner 
        assertEquals("A", stvAlgorithm.winnerList.get(0)); // A met quota become winner 

    }

    @Test
    public void redistribute() {
        Path csvPath = Paths.get("Project2/testing/municipal/redistribute.csv");
        assertTrue(Files.exists(csvPath), "File not found: " + csvPath);

        // get user input with the correct path
        Scanner scanner = new Scanner(csvPath.toString());
        try {
            election.promptForInput(scanner, ballotFileReader);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }

        election.processBallotFile(ballotFileReader);

        stvAlgorithm = new STVAlgorithm(election); 
        stvAlgorithm.runAlgorithm(election.getBallots());

        assertEquals(1, stvAlgorithm.winnerList.size()); // should have 1 winner 
        assertEquals("A", stvAlgorithm.winnerList.get(0)); // A, first winner 
        assertEquals("B", stvAlgorithm.winnerList.get(1)); // B, second winner

        assertEquals(1, stvAlgorithm.loserList.size()); // one losers
        assertEquals("C", stvAlgorithm.loserList.get(0)); // C eliminated first

    }   

}
