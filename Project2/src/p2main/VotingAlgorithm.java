package p2main;
import java.util.ArrayList;
import java.util.List;

import p2main.Ballot;
import p2main.Election;

/**
 * Abstract class representing a voting algorithm.
 * This class defines the structure for any voting algorithm,
 * including handling election data, processing results and 
 * displaying it on console.
 */
public abstract class VotingAlgorithm {
    /** The election associated with this voting algorithm. */
    protected Election election; 

    /** List of winners determined by the voting algorithm. */
    public List<String> winnerList;

     /** List of losers determined by the voting algorithm. */
    public List<String> loserList;

    /** Number of winners to be selected in the election. */
    public int numWinners; 


    /**
     * Constructor for VotingAlgorithm.
     * Initializes the winner and loser lists.
     */
    public VotingAlgorithm() {
        this.winnerList = new ArrayList<>();
        this.loserList = new ArrayList<>();
    }

    /**
     * Runs the voting algorithm on the given list of ballots.
     * Implementations must define how the algorithm processes votes.
     *
     * @param ballots List of ballots cast in the election.
     */
    public abstract void runAlgorithm(List<Ballot> ballots);

    /**
     * Displays the results of the election.
     * Implementations must define how results are print to console.
     */
    protected abstract void displayResults();

    /**
     * Breaks a tie between candidates in case of equal votes.
     * Implementations must define a method to resolve ties.
     *
     * @param tieList List of candidate indices involved in a tie.
     * @return A list of candidate indices ordered based on tie resolution.
     */
    protected abstract List<Integer> breakTie(List<Integer> tieList);

}