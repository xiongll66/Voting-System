/**
 * MvAlgorithm.java
 * 
 * This class implements the  municipal voting algorithm.
 * It counts votes from ballots, determines winners and losers, and handles ties.
 *
 * Author: Ly Xiong
 */

package main;
import java.util.List;

public class MvAlgorithm extends VotingAlgorithm {
    /** The election object containing candidates and ballots. */
    public Election election;

    /** Stores the number of votes each candidate received. */
     public int[] counterList;

     /** Sorted list of candidate names based on votes. */
    public List<String> sortCanList;



    public MvAlgorithm(Election election) {
        
        this.election = election; 
        this.counterList = new int[election.getCandidates().length];
        
    }


    @Override
    public void runAlgorithm(List<Ballot> ballots) {
        
        MvAlgorithmFunction(ballots);
        calculateWinner(); 
        displayResults();
    }

    private void MvAlgorithmFunction(List<Ballot> ballots) {
    
        int numCandidates = election.getCandidates().length;
        counterList = new int[numCandidates];
    
        for (Ballot ballot : ballots) {
            int[] votes = ballot.getVote();
    
            if (votes == null || votes.length != numCandidates) {
                throw new IllegalArgumentException("Invalid ballot format.");
            }
    
            for (int i = 0; i < votes.length; i++) {
                if (votes[i] == 1) {
                    counterList[i]++;
                } else if (votes[i] != 0) {
                    throw new IllegalArgumentException("Votes must be 0 or 1.");
                }
            }
        }
    }
    

    private void calculateWinner() {
     
    }
    
    

    protected List<Integer> breakTie(List<Integer> tieList) {
        return tieList;
    }




    @Override
    protected void displayResults() {
        System.out.println("=== Municipal Voting Results ===");

        System.out.println("\nWinners:");
        for (String winner : winnerList) {
            System.out.println("  " + winner );
        }

        System.out.println("\nLosers:");
        for (String loser : loserList) {
            System.out.println("  " + loser);
        }
    }

}