package Project1.testing.stv;


import main.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;

public class STVAlgorithmTesting {
    
    private STVAlgorithm stv;
    private Election election;
    private STVInput input;
    
    @Before
    public void setUp() {
        stv = new STVAlgorithm();
        election = new Election();
        // Initialize through reflection since fields are private
        try {
            // Set candidates
            java.lang.reflect.Field candidatesField = Election.class.getDeclaredField("candidates");
            candidatesField.setAccessible(true);
            candidatesField.set(election, new String[]{"A", "B", "C", "D"});
            
            // Set input
            input = new STVInput("STV", 2, "ballots.csv", "audit.txt", false);
            java.lang.reflect.Field inputField = Election.class.getDeclaredField("input");
            inputField.setAccessible(true);
            inputField.set(election, input);
            
            // Set election in STVAlgorithm
            java.lang.reflect.Field electionField = STVAlgorithm.class.getDeclaredField("election");
            electionField.setAccessible(true);
            electionField.set(stv, election);
        } catch (Exception e) {
            fail("Failed to setup test due to reflection error: " + e.getMessage());
        }
    }
    
    @Test
    public void testCalculateDroopQuota() {
        // Test with 100 ballots and 2 seats
        election.getInput().numSeats = 2;
        stv.calculateDroopQuota(100);
        assertEquals(34, stv.droopQuota); // (100/(2+1))+1 = 34
        
        // Test with 10 ballots and 1 seat
        election.getInput().numSeats = 1;
        stv.calculateDroopQuota(10);
        assertEquals(6, stv.droopQuota); // (10/(1+1))+1 = 6
    }
    
    @Test
    public void testRedistributeSurplusBallots() {
        // Setup test data
        try {
            java.lang.reflect.Field candidatesField = Election.class.getDeclaredField("candidates");
            candidatesField.setAccessible(true);
            candidatesField.set(election, new String[]{"A", "B", "C"});
        } catch (Exception e) {
            fail("Failed to set candidates");
        }
        
        stv.counterList = new ArrayList[3];
        for (int i = 0; i < 3; i++) {
            stv.counterList[i] = new ArrayList<>();
        }
        
        // Create ballots for candidate 0 (A)
        for (int i = 1; i <= 10; i++) {
            int[] votes = {1, 2, 3}; // A is first preference
            stv.counterList[0].add(new STVBallot(i, votes));
        }
        
        // Set droop quota to 5 (so 5 surplus ballots)
        stv.droopQuota = 5;
        stv.electedList = new LinkedHashMap<>();
        stv.electedList.put(0, 10); // Candidate A elected with 10 votes
        
        stv.redistributeSurplusBallots(0);
        
        // Should keep 5 ballots and redistribute 5
        assertEquals(5, stv.counterList[0].size());
    }
    
    @Test
    public void testEliminateWeakestCandidate() {
        // Setup test data
        String[] candidates = {"A", "B", "C"};
        election.setCandidates(candidates);
        stv.counterList = new ArrayList[3];
        for (int i = 0; i < 3; i++) {
            stv.counterList[i] = new ArrayList<>();
        }
        
        // Add votes (A:3, B:1, C:2)
        for (int i = 1; i <= 3; i++) stv.counterList[0].add(new STVBallot(i, new int[]{1,2,3}));
        stv.counterList[1].add(new STVBallot(4, new int[]{2,1,3}));
        for (int i = 5; i <= 6; i++) stv.counterList[2].add(new STVBallot(i, new int[]{3,1,2}));
        
        stv.firstBallotTimes = new int[]{1, 2, 3}; // A got first ballot, then B, then C
        
        stv.eliminateWeakestCandidate();
        
        // B should be eliminated (only 1 vote)
        assertEquals(1, stv.nonElectedList.size());
        assertTrue(stv.nonElectedList.containsKey(1));
    }
    
    @Test
    public void testBreakTie() {
        // Setup test data
        String[] candidates = {"A", "B", "C"};
        election.setCandidates(candidates);
        stv.firstBallotTimes = new int[]{3, 1, 2}; // B first, then C, then A
        
        List<Integer> tieList = new ArrayList<>();
        tieList.add(0); // A
        tieList.add(1); // B
        tieList.add(2); // C
        
        List<Integer> result = stv.breakTie(tieList);
        
        // Should be ordered by most recent first (A, C, B)
        assertEquals(0, (int)result.get(0));
        assertEquals(2, (int)result.get(1));
        assertEquals(1, (int)result.get(2));
    }
    
    @Test
    public void testDetermineWinnersAndLosers() {
        // Setup test data
        String[] candidates = {"A", "B", "C", "D"};
        election.setCandidates(candidates);
        election.getInput().numSeats = 2;
        
        stv.electedList = new LinkedHashMap<>();
        stv.electedList.put(0, 10); // A elected
        stv.electedList.put(2, 8);  // C elected
        
        stv.nonElectedList = new LinkedHashMap<>();
        stv.nonElectedList.put(1, 5); // B eliminated
        stv.nonElectedList.put(3, 3); // D eliminated
        
        stv.determineWinnersAndLosers();
        
        // Verify winners and losers
        assertEquals(2, stv.winnerList.size());
        assertEquals("A", stv.winnerList.get(0));
        assertEquals("C", stv.winnerList.get(1));
        
        assertEquals(2, stv.loserList.size());
        assertEquals("B", stv.loserList.get(0));
        assertEquals("D", stv.loserList.get(1));
    }
    
    @Test
    public void testValidateBallots() {
        // Setup test data
        String[] candidates = {"A", "B", "C", "D"};
        election.setCandidates(candidates);
        
        List<Ballot> ballots = new ArrayList<>();
        ballots.add(new STVBallot(1, new int[]{1,2,0,0})); // Valid (2/4 ranked)
        ballots.add(new STVBallot(2, new int[]{1,2,3,0})); // Valid (3/4 ranked)
        
        // Should not throw exception
        stv.validateBallots(ballots, 4);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testValidateBallotsInvalid() {
        // Setup test data
        String[] candidates = {"A", "B", "C", "D"};
        election.setCandidates(candidates);
        
        List<Ballot> ballots = new ArrayList<>();
        ballots.add(new STVBallot(1, new int[]{1,0,0,0})); // Invalid (only 1/4 ranked)
        
        // Should throw exception
        stv.validateBallots(ballots, 4);
    }

    @Test
    public void testFullAlgorithmRun() {
        // Setup test data
        try {
            java.lang.reflect.Field candidatesField = Election.class.getDeclaredField("candidates");
            candidatesField.setAccessible(true);
            candidatesField.set(election, new String[]{"A", "B", "C"});
            
            java.lang.reflect.Field seatsField = STVInput.class.getDeclaredField("numSeats");
            seatsField.setAccessible(true);
            seatsField.set(election.getInput(), 1);
        } catch (Exception e) {
            fail("Failed to setup test data");
        }
        
        List<Ballot> ballots = new ArrayList<>();
        // 4 votes for A
        ballots.add(new STVBallot(1, new int[]{1,2,3}));
        ballots.add(new STVBallot(2, new int[]{1,3,2}));
        ballots.add(new STVBallot(3, new int[]{1,2,3}));
        ballots.add(new STVBallot(4, new int[]{1,3,2}));
        // 3 votes for B
        ballots.add(new STVBallot(5, new int[]{2,1,3}));
        ballots.add(new STVBallot(6, new int[]{2,3,1}));
        ballots.add(new STVBallot(7, new int[]{2,1,3}));
        // 2 votes for C
        ballots.add(new STVBallot(8, new int[]{3,1,2}));
        ballots.add(new STVBallot(9, new int[]{3,2,1}));
        
        stv.runAlgorithm(ballots);
        
        // A should win (droop quota is 5, but since no one reaches quota, A has most votes)
        assertEquals(1, stv.winnerList.size());
        assertEquals("A", stv.winnerList.get(0));
    }
}