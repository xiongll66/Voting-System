/**
 * MvAlgorithm.java
 * 
 * This class implements the  municipal voting algorithm.
 * It counts votes from ballots, determines winners and losers, and handles ties.
 *
 * Author: Ly Xiong
 */

 package main;

 import java.util.*;
 
 public class MunicipalAlgorithm extends VotingAlgorithm {
     /** The election object containing candidates and ballots. */
     public Election election;
     
     /** Stores the number of votes each candidate received. */
     public int[] counterList;

     /** Sorted list of candidate names based on votes. */
     public List<String> sortCanList;
     private Map<String, Integer> candidateVoteMap = new HashMap<>();
 
     public MunicipalAlgorithm(Election election) {
         this.election = election;
         this.counterList = new int[election.getCandidates().length];
         this.sortCanList = new ArrayList<>();
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
         int numSeats = election.getNumSeats();
         String[] candidates = election.getCandidates();
 
         List<int[]> candidateVotes = new ArrayList<>();
         for (int i = 0; i < candidates.length; i++) {
             candidateVotes.add(new int[]{i, counterList[i]});
             candidateVoteMap.put(candidates[i], counterList[i]); // Save votes for later display
         }
 
         candidateVotes.sort((a, b) -> Integer.compare(b[1], a[1]));
 
         int cutoffVotes = candidateVotes.get(numSeats - 1)[1];
         List<Integer> tieIndices = new ArrayList<>();
 
         for (int[] pair : candidateVotes) {
             if (pair[1] == cutoffVotes) {
                 tieIndices.add(pair[0]);
             }
         }
 
         List<Integer> winners = new ArrayList<>();
 
         if (tieIndices.size() > 1) {
             List<Integer> selectedFromTies = breakTie(tieIndices);
             for (int[] pair : candidateVotes) {
                 if (pair[1] > cutoffVotes || (pair[1] == cutoffVotes && selectedFromTies.contains(pair[0]))) {
                     winners.add(pair[0]);
                 }
             }
         } else {
             for (int i = 0; i < numSeats; i++) {
                 winners.add(candidateVotes.get(i)[0]);
             }
         }
 
         for (int i = 0; i < candidates.length; i++) {
             if (winners.contains(i)) {
                 winnerList.add(candidates[i]);
             } else {
                 loserList.add(candidates[i]);
             }
         }
     }
 
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
 }
 
 