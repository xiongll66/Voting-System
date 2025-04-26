/**
 * MvAlgorithm.java
 * 
 * This class implements the  municipal voting algorithm that allow voter to vote for multiple candidates
 * It counts votes from ballots, determines winners and losers, and handles ties.
 *
 * Author: Ly Xiong
 */

 package p2main;

 import java.util.*;
 
public class MunicipalAlgorithm extends VotingAlgorithm {
    /** The election object containing candidates and ballots. */
    public Election election;
    
    /** Stores the number of votes each candidate received. */
    public int[] counterList;

    /** Sorted list of candidate names based on votes. */
    public List<String> sortCanList;
    private Map<String, Integer> candidateVoteMap = new HashMap<>();

    /**
     * Constructor for MvAlgorithm.
     * 
     * @param election the Election object that provides candidate and seat information.
     */
    public MunicipalAlgorithm(Election election) {
        this.election = election;
        this.counterList = new int[election.getCandidates().length];
        this.sortCanList = new ArrayList<>();
    }

    /**
     * Executes the municipal voting algorithm:
     * 1. Counts votes from ballots.
     * 2. Determines the winners and losers.
     * 3. Displays results.
     *
     * @param ballots list of ballots cast in the election.
     */
    @Override
    public void runAlgorithm(List<Ballot> ballots) {
        MvAlgorithmFunction(ballots);
        calculateWinner();
        displayResults();
    }

    /**
     * Counts the number of 1s for each candidate in all ballots.
     *
     * @param ballots list of ballots to be processed.
     * 
     */
    private void MvAlgorithmFunction(List<Ballot> ballots) {
        int numCandidates = election.getCandidates().length;
        counterList = new int[numCandidates];

        for (Ballot ballot : ballots) {
            int[] votes = ballot.getVote();
            //Count each candidate gets a vote for a 1
            for (int i = 0; i < votes.length; i++) {
                if (votes[i] == 1) {
                    counterList[i]++;
                } else if (votes[i] != 0) {
                    throw new IllegalArgumentException("Votes must be 0 or 1.");
                }
            }
        }
    }

    /**
     * Calculates the winning candidates based on the number of votes and seats available
     * Handles ties by calling breakTie() to randomize within tied groups
     */
    private void calculateWinner() {
        int numSeats = election.getNumSeats();
        String[] candidates = election.getCandidates();
        
        // Create a list of (candidate index, vote count) pairs
        List<int[]> candidateVotePairs = new ArrayList<>();
        for (int i = 0; i < candidates.length; i++) {
            candidateVotePairs.add(new int[]{i, counterList[i]});
            candidateVoteMap.put(candidates[i], counterList[i]); // Save votes for later display
        }

        // Sort candidates by vote count descending
        candidateVotePairs.sort((a, b) -> Integer.compare(b[1], a[1]));

        List<int[]> finalSortedCandidates = new ArrayList<>();
        int i = 0;

        while (i < candidateVotePairs.size()) {
            int currentVote = candidateVotePairs.get(i)[1];
            List<Integer> tieGroupIndices = new ArrayList<>();

            // Collect all candidates with the same vote count
            while (i < candidateVotePairs.size() && candidateVotePairs.get(i)[1] == currentVote) {
                tieGroupIndices.add(candidateVotePairs.get(i)[0]);
                i++;
            }

            // Break the tie using breakTie() to randomize
            List<Integer> orderedTieGroup = breakTie(tieGroupIndices);

            // Add candidates back into final list based on the random tie order
            for (Integer candidateIndex : orderedTieGroup) {
                finalSortedCandidates.add(new int[]{candidateIndex, currentVote});
            }
        }

        // Pick winners and losers
        for (int j = 0; j < candidates.length; j++) {
            int candidateIndex = finalSortedCandidates.get(j)[0];
            if (j < numSeats) {
                winnerList.add(candidates[candidateIndex]);
            } else {
                loserList.add(candidates[candidateIndex]);
            }
        }
    }


    /**
     * Displays the final election results, including vote totals for each winner and loser.
     */
    @Override
    protected void displayResults() {
        System.out.println("=== Municipal Voting Results ===");

        System.out.println("\nWinners:");
        for (String winner : winnerList) {
            System.out.println("  " + winner + " - " + candidateVoteMap.get(winner) + " votes");
        }

        System.out.println("\nLosers:");
        for (String loser : loserList) {
            System.out.println("  " + loser + " - " + candidateVoteMap.get(loser) + " votes");
        }
    }
 
    /**
     * Breaks a tie by randomly selecting a number of candidates equal to the number of remaining seats.
     *
     * @param tieList list of candidate indices tied at the cutoff vote count.
     * @return list of selected candidate indices to break the tie.
     */
    protected List<Integer> breakTie(List<Integer> tieList) {

        Random random = new Random();
        // Store winners in order from first winner to last winner 
        List<Integer> selected = new ArrayList<>(); 
        // Randomly select candidates until all are ordered
        while (!tieList.isEmpty()) {
            int randomIndex = random.nextInt(tieList.size()); // Pick a random index
            int candidateIndex = tieList.remove(randomIndex); // Remove from tieList
            selected.add(candidateIndex); // Store in ordered list
        }
        
        return selected; 
    }
     /**
     * Retrieves the list of winners from the election.
     * 
     * This method returns a list of candidates who have won the election based on
     * the results of the Plurality algorithm. The list may contain one or more candidates
     * depending on the election results (e.g., in the case of ties).
     * 
     * @return a list of candidate names who are the winners of the election.
    */
    public List<String> getWinnerList() {
        return this.winnerList; 
    }
    
    /**
     * Retrieves the list of losers from the election.
     * 
     * This method returns a list of candidates who have lost the election based on
     * the results of the Plurality algorithm. The list may contain one or more candidates
     * depending on the election results.
     * 
     * @return a list of candidate names who are the losers of the election.
     */
    public List<String> getLoserList() {
        return this.loserList; 
    }
}
 
 