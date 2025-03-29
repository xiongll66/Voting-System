/**
 * PluralityAlgorithm.java
 * 
 * This class implements the Plurality voting algorithm.
 * It counts votes from ballots, determines winners and losers, and handles ties.
 *
 * Author: Kongmeng Thao
 */
package main;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Implements the Plurality voting algorithm.
 * This voting system assigns votes based on the first-choice candidate
 * in each ballot. The candidate with the most votes wins.
 * If a tie occurs, a random tie-breaking mechanism is used.
 */
public class PluralityAlgorithm extends VotingAlgorithm{
    /** The election object containing candidates and ballots. */
    protected Election election; 

     /** List of candidates who won the election. */
    public List<String> winnerList;

    /** List of candidates who lost the election. */
    public List<String> loserList;

    /** Stores the number of votes each candidate received. */
    int[] counterList;

    /** Sorted list of candidate names based on votes. */
    List<String> sortCanList;

    /** Sorted list of vote counts corresponding to `sortCanList`. */
    int[] sortVoteList; 
    
    /**
     * Constructor for PluralityAlgorithm.
     * Initializes the necessary data structures for processing votes.
     * 
     * @param election The election object containing candidates and ballots.
     * @throws IllegalArgumentException if the election object or candidate list is null or empty.
     */
    public PluralityAlgorithm(Election election) {
        if (election == null || election.getCandidates() == null) {
            throw new IllegalArgumentException("Election or candidate list cannot be null or empty.");
        }
        
        this.election = election; 
        this.winnerList = new ArrayList<>();
        this.loserList = new ArrayList<>();
        this.sortCanList = new ArrayList<>(Arrays.asList(election.getCandidates())); // clone to be sort
        this.counterList = new int[election.getCandidates().length];
        this.sortVoteList = counterList.clone(); // clone to be sort
        
    }

    /**
     * Counts the votes from ballots and assigns them to candidates.
     * 
     * @param ballots The list of ballots to process.
     * @throws IllegalArgumentException if ballots are null, empty, or improperly formatted.
     */
    private void pluralityAlgorithmFunction(List<Ballot> ballots) {
        // Check if there is ballots 
        if (election.getBallots().isEmpty() || election.getBallots() == null) {
            throw new IllegalArgumentException("Ballot from Election cannot be null or empty.");
        }
       
        for (int i = 0; i < ballots.size(); i++) {
            // Count candidate index
            int canIndexCounter = 0; 
            boolean foundVote = false; 
            int[] currBallotVote = ballots.get(i).getVote(); 
            // check if ballot exist or is in a valid format
            if (currBallotVote == null || currBallotVote.length != election.getCandidates().length) {
                throw new IllegalArgumentException("Ballot is null or is in invalid format.");
            }
            // Loop through each ballot votes until vote found or exit 
            while (canIndexCounter < currBallotVote.length) {
                // Check if ballot vote is either 1 or 0 
                if (currBallotVote[canIndexCounter] != 0 && currBallotVote[canIndexCounter] != 1) {
                    throw new IllegalArgumentException("invalid ballot format (vote is not 0 or 1).");
                }
                // Found vote
                if (currBallotVote[canIndexCounter] == 1) {
                    foundVote = true; 
                    break; 
                }
                canIndexCounter++; 
            }
            // Give candidate in that index the vote 
            if (foundVote){
                this.counterList[canIndexCounter]++;
            }
            foundVote = false; 
        }  
    }

    /**
    * Breaks ties by randomly ordering tied candidates.

    * @param tieList The list of tied candidate indices.
    * @return A list of candidate indices in a random order.
    */
    protected List<Integer> breakTie(List<Integer> tieList) {
        Random random = new Random();
        // Store winners in order from first winner to last winner 
        List<Integer> orderedTieList = new ArrayList<>(); 
        // Randomly select candidates until all are ordered
        while (!tieList.isEmpty()) {
            int randomIndex = random.nextInt(tieList.size()); // Pick a random index
            int candidateIndex = tieList.remove(randomIndex); // Remove from tieList
            orderedTieList.add(candidateIndex); // Store in ordered list
        }
        
        return orderedTieList;
    }

    /**
     * Calculates the winner(s) by sorting votes and handling ties.
     * 
     * @throws IllegalArgumentException if vote counts are missing or mismatched with candidates.
     */
    private void calculateWinner() {  // Can be improve with better sort algorithm(later)
        // Check if there is vote from ballot to calculate 
        if (sortVoteList == null || sortVoteList.length == 0) {
            throw new IllegalArgumentException("No votes found.");
        }
        
        // Check if number of candidates and numbers of votes match 
        if (sortVoteList.length != sortCanList.size()) {
            throw new IllegalArgumentException("Candidate and vote count mismatch.");
        }
    
        if (election.getInput().getNumSeats() > sortCanList.size()) {
            throw new IllegalArgumentException("Number of seats exceeds the number of candidates.");
        }

        for (int i = 0; i < sortVoteList.length; i++) {
            for (int j = i + 1; j < sortVoteList.length; j++) {
                // If current vote count is greater, swap values and names
                if (sortVoteList[i] < sortVoteList[j]) {
                    // Swap vote counts
                    int tempVotes = sortVoteList[i];
                    sortVoteList[i] = sortVoteList[j];
                    sortVoteList[j] = tempVotes; 
                    
                    // Swap corresponding candidate names
                    String tempCandidate = sortCanList.get(i);
                    sortCanList.set(i, sortCanList.get(j));
                    sortCanList.set(j, tempCandidate);
                }
            }
        }
        // Index in the sorted list where they are tied
        List<Integer> tieIndexList = new ArrayList<>(); 

        // the final combined index list of the tie sorted list
        List<Integer> finalSortedWinners = new ArrayList<>(); 

        // Process the sorted list and handle ties
        for (int i = 0; i < sortVoteList.length; i++) {
            tieIndexList.clear(); // Clear the tie list for new tie
            tieIndexList.add(i);  // Start with the current candidate
        
            // Check if there's a tie
            while (i + 1 < sortVoteList.length && sortVoteList[i + 1] ==  sortVoteList[i]) {
                tieIndexList.add(i + 1); // Add the tied candidate
                i++;  
            }
            // If there's a tie and the next candidate doesn't tie, call the tie-breaker
            if (tieIndexList.size() > 1) {
                finalSortedWinners.addAll(breakTie(tieIndexList));
            } else { // else if there no tie at all, directly add the candidate 
                finalSortedWinners.add(tieIndexList.get(0));
            }
            
        }

        // Add everything to winner list based on seat available 
        int i;
        for (i = 0; i < election.getInput().getNumSeats(); i++) {
            winnerList.add(sortCanList.get(finalSortedWinners.get(i))); 
        } // Add reminding to loser 
        for (; i < finalSortedWinners.size(); i++) {
            loserList.add(sortCanList.get(finalSortedWinners.get(i))); 
        }
    }

    /**
     * Displays the election results to the console.
     */
    protected void displayResults() {
        System.out.println("Plurality Results:");
        System.out.println("Election Type: " + election.getInput().getAlgorithm());
        System.out.println("Number of Seats: " + election.getInput().getNumSeats());
        System.out.println("Number of Candidates: " + election.getCandidates().length);
        System.out.println("Number of Ballots: " + election.getBallots().size());
        System.out.println();    

        int totalVotes = 0;
        for (int votes : sortVoteList) {
            totalVotes += votes;
        }

        System.out.println("Election Results:");
        
        // Display winners with vote percentages
        System.out.println("\nWinners:");
        for (String winner : winnerList) {
            int winnerIndex = sortCanList.indexOf(winner);
            double percentage = (sortVoteList[winnerIndex] / (double) totalVotes) * 100;
            System.out.printf("%s - Votes: %d (%.2f%%)\n", winner, sortVoteList[winnerIndex], percentage);
        }

        // Display losers with vote percentages
        System.out.println("\nLosers:");
        for (String loser : loserList) {
            int loserIndex = sortCanList.indexOf(loser);
            double percentage = (sortVoteList[loserIndex] / (double) totalVotes) * 100;
            System.out.printf("%s - Votes: %d (%.2f%%)\n", loser, sortVoteList[loserIndex], percentage);
        }

    }
    @Override
    /**
     * Executes the Plurality voting algorithm.
     * 
     * @param ballots The list of ballots to process.
     */
    public void runAlgorithm(List<Ballot> ballots) {
        pluralityAlgorithmFunction(ballots);
        calculateWinner(); 
        displayResults();
    }
}
   