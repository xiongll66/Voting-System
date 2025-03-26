package Project1.testFiles.plurality;
import main.PluralityBallot;
import main.Election;
import main.PluralityAlgorithm;
import main.Ballot;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

/**
 * Unit tests for the PluralityAlgorithm class.
 */
public class PluralityAlgorithmTest {
    private Election election;
    private PluralityAlgorithm algorithm;

    @BeforeEach
    void setUp() {
        List<String> candidates = Arrays.asList("Alice", "Bob", "Charlie");
        List<Ballot> ballots = new ArrayList<>();
        ballots.add(new PluralityBallot(new int[]{1, 0, 0})); // Alice
        ballots.add(new PluralityBallot(new int[]{1, 0, 0})); // Alice
        ballots.add(new PluralityBallot(new int[]{0, 1, 0})); // Bob
        ballots.add(new PluralityBallot(new int[]{0, 0, 1})); // Charlie
        ballots.add(new PluralityBallot(new int[]{0, 1, 0})); // Bob
        ballots.add(new PluralityBallot(new int[]{0, 0, 1})); // Charlie
        ballots.add(new PluralityBallot(new int[]{1, 0, 0})); // Alice

        election = new Election(candidates, 1, "Plurality", ballots);
        algorithm = new PluralityAlgorithm(election);
    }

    @Test
    void testConstructor_ValidElection() {
        assertNotNull(algorithm);
        assertEquals(3, algorithm.counterList.length);
        assertEquals(3, algorithm.sortCanList.size());
    }

    @Test
    void testConstructor_InvalidElection() {
        assertThrows(IllegalArgumentException.class, () -> new PluralityAlgorithm(null));
        assertThrows(IllegalArgumentException.class, () -> new PluralityAlgorithm(new Election(new ArrayList<>(), 1, "Plurality", new ArrayList<>())));
    }

    @Test
    void testPluralityAlgorithmFunction() {
        algorithm.runAlgorithm(election.ballots);
        assertEquals(3, algorithm.counterList[0]); // Alice votes
        assertEquals(2, algorithm.counterList[1]); // Bob votes
        assertEquals(2, algorithm.counterList[2]); // Charlie votes
    }

    @Test
    void testCalculateWinner_SingleWinner() {
        algorithm.runAlgorithm(election.ballots);
        assertEquals(1, algorithm.winnerList.size());
        assertTrue(algorithm.winnerList.contains("Alice"));
    }

    @Test
    void testCalculateWinner_TieBreaker() {
        List<Ballot> tieBallots = new ArrayList<>();
        tieBallots.add(new PluralityBallot(new int[]{1, 0, 0}));
        tieBallots.add(new PluralityBallot(new int[]{0, 1, 0}));
        tieBallots.add(new PluralityBallot(new int[]{0, 1, 0}));
        tieBallots.add(new PluralityBallot(new int[]{1, 0, 0}));

        Election tieElection = new Election(Arrays.asList("Alice", "Bob"), 1, "Plurality", tieBallots);
        PluralityAlgorithm tieAlgorithm = new PluralityAlgorithm(tieElection);
        tieAlgorithm.runAlgorithm(tieElection.ballots);

        assertEquals(1, tieAlgorithm.winnerList.size());
        assertTrue(tieAlgorithm.winnerList.contains("Alice") || tieAlgorithm.winnerList.contains("Bob"));
    }

    @Test
    void testInvalidBallots() {
        List<Ballot> invalidBallots = new ArrayList<>();
        invalidBallots.add(new PluralityBallot(new int[]{2, 0, 0})); // Invalid vote

        Election invalidElection = new Election(Arrays.asList("Alice", "Bob", "Charlie"), 1, "Plurality", invalidBallots);
        PluralityAlgorithm invalidAlgorithm = new PluralityAlgorithm(invalidElection);

        assertThrows(IllegalArgumentException.class, () -> invalidAlgorithm.runAlgorithm(invalidElection.ballots));
    }
}