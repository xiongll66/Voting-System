/**
 * MvAlgorithm.java
 * 
 * This class implements the  municipal voting algorithm.
 * It counts votes from ballots, determines winners and losers, and handles ties.
 *
 * Author: Ly Xiong
 */

package p2main;

import java.util.*;

import p2main.Ballot;
import p2main.Election;
import p2main.VotingAlgorithm;

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
     * Calculates the winning candidates based on the number of votes and seats available.
     * Handles ties by randomly selecting candidates from those with the same vote count at the cutoff.
     */
    private void calculateWinner() {
        int numSeats = election.getNumSeats();
        String[] candidates = election.getCandidates();

        // Pair candidate index with vote count
        List<int[]> candidateVotes = new ArrayList<>();
        for (int i = 0; i < candidates.length; i++) {
            candidateVotes.add(new int[]{i, counterList[i]});
            candidateVoteMap.put(candidates[i], counterList[i]); // Save votes for later display
        }

        // Sort candidates by vote count in descending order
        candidateVotes.sort((a, b) -> Integer.compare(b[1], a[1]));

        // Determine the minimum number of votes needed to win (cutoff)
        int cutoffVotes = candidateVotes.get(numSeats - 1)[1];
        List<Integer> tieIndices = new ArrayList<>();

        // Identify candidates with vote counts equal to the cutoff
        for (int[] pair : candidateVotes) {
            if (pair[1] == cutoffVotes) {
                tieIndices.add(pair[0]);
            }
        }

        List<Integer> winners = new ArrayList<>();
        // If there are ties at the cutoff
        if (tieIndices.size() > 1) {
            List<Integer> selectedFromTies = breakTie(tieIndices);
            // Break the tie and add selected candidates
            for (int[] pair : candidateVotes) {
                if (pair[1] > cutoffVotes || (pair[1] == cutoffVotes && selectedFromTies.contains(pair[0]))) {
                    winners.add(pair[0]);
                }
            }
        } else {
            // No tie — select top N candidates as winners
            for (int i = 0; i < numSeats; i++) {
                winners.add(candidateVotes.get(i)[0]);
            }
        }

        //Put candidates into winners and losers
        for (int i = 0; i < candidates.length; i++) {
            if (winners.contains(i)) {
                winnerList.add(candidates[i]);
            } else {
                loserList.add(candidates[i]);
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
    List<Integer> selected = new ArrayList<>();
    Random rand = new Random();

    // Number of seats left to be filled
    int seatsLeft = election.getNumSeats() - selected.size();

    // Randomly select candidates from the tie list
    while (selected.size() < seatsLeft && !tieList.isEmpty()) {
        int chosenIdx = rand.nextInt(tieList.size());
        selected.add(tieList.get(chosenIdx));
        tieList.remove(chosenIdx);  // Remove the selected candidate
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
 
 