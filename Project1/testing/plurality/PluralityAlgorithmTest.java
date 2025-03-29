package Project1.testing.plurality;

import main.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PluralityAlgorithmTest {
    private Election election;
    private PluralityAlgorithm algorithm;

    @BeforeEach
    void setUp() {
        election = new Election();
        algorithm = new PluralityAlgorithm(election); 
        
    }
 
    
    public static void main(String[] args) {
        List<Ballot> ballotsList = new ArrayList<>();
        ballotsList.add(new PluralityBallot(new int[]{1, 0, 0}));
        ballotsList.add(new PluralityBallot(new int[]{1, 0, 0}));
        ballotsList.add(new PluralityBallot(new int[]{1, 0, 0}));
        ballotsList.add(new PluralityBallot(new int[]{0, 1, 0}));
        ballotsList.add(new PluralityBallot(new int[]{0, 1, 0}));
        ballotsList.add(new PluralityBallot(new int[]{0, 0, 1}));
        ballotsList.add(new PluralityBallot(new int[]{0, 0, 1})); // Ensure PluralityBallot extends Ballot
        String[] candidates = {"A", "B", "C"};
        
        InitialInput input = new PluralityInput("Plurality", 1, "0tie1seat.csv"); // Ensure compatibility with InitialInput
        PluralityAlgorithm votingAlgorithm = this.algorithm; // Ensure `this.algorithm` is of type VotingAlgorithm
        
        election.electionSet(ballotsList, candidates, input, votingAlgorithm, 'p');
        algorithm.runAlgorithm(ballotsList);
    }
}
