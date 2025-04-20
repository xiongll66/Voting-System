

package Project2.testing.stv;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import main.*;

public class STVAlgorithmTesting {
    private STVAlgorithm stvAlgorithm;
    private Election election;
    private List<Ballot> ballots;

    @BeforeEach
    void setUp() {
        election = new Election();
        // Initialize with test data
        election.setCandidates(new String[]{"A", "B", "C"});
        election.setInput(new STVInput("s", 1, "elimination.csv", "audit", false));
        stvAlgorithm = new STVAlgorithm(election);
        stvAlgorithm.counterList = (List<Ballot>[]) new ArrayList[election.getCandidates().length];
        for (int i = 0; i < stvAlgorithm.counterList.length; i++) {
            stvAlgorithm.counterList[i] = new ArrayList<>();  // Initializes each list in the array to hold STVBallot
        }
        stvAlgorithm.firstBallotTimes = new int[election.getCandidates().length];
        ballots = new ArrayList<>();
    }

    @Test
    void testCalculateDroopQuota() {
        // Test with 100 ballots and 2 seats
        stvAlgorithm.calculateDroopQuota(100, 2);
        assertEquals(34, stvAlgorithm.droopQuota); // 100/(2+1) + 1 = 34
        
        
        // Test edge case with minimal ballots
        stvAlgorithm.calculateDroopQuota(3,  ((STVInput) election.getInput()).getNumSeats());
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
        stvAlgorithm.shuffleBallots(testBallots, ((STVInput) election.getInput()).getShuffle());
        assertIterableEquals(original, testBallots);

        // Shuffle on - should be different
        stvAlgorithm.shuffleBallots(testBallots, true);
        assertNotEquals(original, testBallots);
    }

    @Test
    void testRedistributeCandidateBallots() {
        // Create test ballots (5 ballots, 3 candidates)
        ballots.add(new STVBallot(1, new int[]{1, 2, 3})); // A first
        ballots.add(new STVBallot(2, new int[]{1, 2, 3})); // A first
        ballots.add(new STVBallot(3, new int[]{3, 1, 2})); // B first 
        ballots.add(new STVBallot(4, new int[]{3, 1, 2})); // B first
        ballots.add(new STVBallot(5, new int[]{2, 3, 1})); // C first
        stvAlgorithm.calculateDroopQuota(ballots.size(), election.getInput().getNumSeats());
        stvAlgorithm.redistributeCandidateBallots(ballots);

        // Verify results (quota = 3 votes needed to win)
        assertEquals(1, stvAlgorithm.winnerList.size()); // only one winner
        assertEquals("A", stvAlgorithm.winnerList.get(0)); // A wins with 3 votes
        
        assertEquals(2, stvAlgorithm.loserList.size()); // two losers
        assertEquals("C", stvAlgorithm.loserList.get(0)); // C eliminated first
        assertEquals("B", stvAlgorithm.loserList.get(1));
    }
}
