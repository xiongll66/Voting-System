/**
 * STVAlgorithm.java
 *
 * The STValgorithm handles vote redistribution through multiple rounds until 
 * all seats are filled or no more candidates can be elected.
 * 
 * This class handles vote redistribution, surplus management, candidate elimination,
 * winner and loser determination, as well as break tie and generate an audit report for an STV election process.
 *
 * Author: Ly Xiong
 */

package main;

import java.io.*;
import java.util.*;



/**
 * STVAlgorithm class implementing the STV voting method.
 * Inherits from VotingAlgorithm.
 */
public class STVAlgorithm extends VotingAlgorithm {
    /** The election instance being processed */
    public Election election;

    /** The minimum votes required to win a seat (Droop quota) */
    public int droopQuota;

    /** Array of lists tracking ballots assigned to each candidate */
    public List<Ballot>[] counterList;

    /** Ordered map of elected candidates (candidate index → vote count) */
    public LinkedHashMap<Integer, Integer> electedList;

    /** Ordered map of non-elected candidates (candidate index → vote count) */
    public LinkedHashMap<Integer, Integer> nonElectedList;

    /** Timestamp counter for ballot assignments */
    public  int time;

    /** Tracks when each candidate received their first ballot */
    public  int[] firstBallotTimes;

    /** Audit log entries for the election process */
    final List<String> auditLog = new ArrayList<>();

    /**
     * Constructs an STVAlgorithm instance.
     * Initializes elected and non-elected candidate lists.
     */
    public STVAlgorithm() {
        super();
        this.electedList = new LinkedHashMap<>();
        this.nonElectedList = new LinkedHashMap<>();
    }

    /**
     * Executes the STV voting algorithm.
     * 
     * @param ballots The list of ballots to process
     * 
     */
    @Override
    public void runAlgorithm(List<Ballot> ballots) {
        // Declare counterList as List<STVBallot>[]
        this.counterList = (List<Ballot>[]) new ArrayList[election.getCandidates().length];
        

        // Initialize each element of counterList with a new ArrayList of STVBallot
        for (int i = 0; i < counterList.length; i++) {
            counterList[i] = new ArrayList<>();  // Initializes each list in the array to hold STVBallot
        }

        time = 0;
        firstBallotTimes = new int[election.getCandidates().length];
        calculateDroopQuota(ballots.size(), election.getInput().getNumSeats());
        redistributeCandidateBallots(ballots);
        generateAuditFile("audit_report.txt");
    }

    /**
     * Shuffles the list of ballots if shuffle is true.
     *
     * @param ballots The list of ballots.
     * @param shuffle Boolean flag indicating whether to shuffle.
     */
    public void shuffleBallots(List<Ballot> ballots, boolean shuffle) {

        if (shuffle) {
            Collections.shuffle(ballots);
            System.out.println("Ballots shuffled.");
        } else {
            System.out.println("Shuffle is off for testing.");
        }
    }

    /**
     * Calculates the minimum votes needed to win a seat (Droop quota).
     * Formula: (total_ballots / (seats + 1)) + 1
     * 
     * @param numBallots The total number of ballots
     * @param numSeats The number of seats
     */
    public void calculateDroopQuota(int numBallots, int numSeats) {
        droopQuota = (numBallots / (numSeats + 1)) + 1;
    }

    /**
     * Manages the core STV process including:
     * - Call shuffleBallots()
     * - Distribute all ballot to allCandidate
     * - Election of candidates who reach quota
     * - Call redistributeSurplusBallots()
     * - Call eliminateWeakestCandidate()
     * - Call determineWinnersAndLosers()
     * 
     * @param ballots The list of ballots to process
     */
    public void redistributeCandidateBallots(List<Ballot> ballots) {
        shuffleBallots(ballots, ((STVInput) election.getInput()).getShuffle());

        int totalCandidates = election.getCandidates().length;
    
        // Validate ballots before processing
        validateBallots(ballots, totalCandidates);

        auditLog.add("Ballots DISTRIBUTION");

        //Assign all ballots to all candidates
        for (Ballot ballot : ballots) {
            STVBallot stvBallot = (STVBallot) ballot;
            int[] ballotRanks = stvBallot.getVote();
            int firstPref = stvBallot.getPreference(); //  preference == 1

            for (int candidateId = 0; candidateId < ballotRanks.length; candidateId++) {
                if (ballotRanks[candidateId] == firstPref) { // if the rank at that index == firstPref
                    counterList[candidateId].add(stvBallot); // assign the ballot to the candidate
                    auditLog.add("Ballot number " + stvBallot.getId() + " is voted for candidate " + election.getCandidates()[candidateId]);
                    time++;
                    firstBallotTimes[candidateId] = time;
                    break; // stop once we assign the ballot
                }
            }
        }

        //Main election rounds loops until all seats are filled
        while (electedList.size() < election.getInput().getNumSeats()) {
            boolean electedThisRound = false;

            // Check for candidates meeting quota
            for (int candidate = 0; candidate < counterList.length; candidate++) { 
                if (!electedList.containsKey(candidate) && counterList[candidate].size() >= droopQuota) { 
                    // Elect candidate and redistribute surplus
                    electedList.put(candidate, counterList[candidate].size()); 
                    auditLog.add("Candidate " + election.getCandidates()[candidate] +
                            " has reached the quota and is elected.");
                    redistributeSurplusBallots(candidate); // redistributed surplus ballot
                    electedThisRound = true;
                    break;
                }
            }
            // If no one elected, eliminate weakest candidate
            if (!electedThisRound) {
                auditLog.add("No candidates reached quota - eliminating weakest");
                eliminateWeakestCandidate();
            }

            // Stop if we've processed all candidates
            if (electedList.size() + nonElectedList.size() >= election.getCandidates().length) {
                break;
            }
        }
        // Finalize results
        determineWinnersAndLosers();
    }

    /**
     * Generates an audit file documenting the election process.
     * 
     * @param fileName The name of the audit file to create
     * @throws IOException if file writing fails
     */
    private void generateAuditFile(String fileName) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            // Header information
            writer.println("=== STV ELECTION AUDIT REPORT ===");
            writer.println("Date: " + new Date());
            writer.println("\nELECTION RESULTS:");
            writer.println("Type: STV");
            writer.println("Number of seats: " + election.getInput().getNumSeats());
            writer.println("Number of candidates: " + election.getCandidates().length);
            writer.println("Droop Quota: " + droopQuota);
            writer.println("Winners: " + winnerList);
            writer.println("Losers: " + loserList);
            
            // Detailed audit trail
            writer.println("\nDETAILED PROCESS LOG:");
            for (String log : auditLog) {
                writer.println(log);
            }

            System.out.println("Audit file generated: " + fileName);
        } catch (IOException e) {
            System.err.println("Error generating audit file: " + e.getMessage());
        }
    }

    @Override
    /**
     * Displays the final election results to standard output.
     * Shows election type, number of seats, candidates, winners and losers.
     */
    public void displayResults() {
        System.out.println("**************** Election Results ****************");
        System.out.println("Election Type: STV");
        System.out.println("Number of Seats: " + election.getInput().getNumSeats());
        System.out.println("Number of Candidates: " + election.getCandidates().length);
        System.out.println("Winners: " + winnerList);
        System.out.println("Losers: " + loserList);
    }

     /**
     * Breaks ties between candidates using ballot timestamp logic.
     * Candidates who received their first ballot most recently lose the tie.
     * 
     * @param tieList List of candidate indexes with equal votes
     * @return Ordered list of candidates after tie-breaking
     */
    @Override
    protected List<Integer> breakTie(List<Integer> tieList) {
        if (tieList.size() == 1) {
            return tieList;
        }


        // Sort by firstBallotTimes in descending order (most recent first)
        tieList.sort((a, b) -> Integer.compare(firstBallotTimes[b], firstBallotTimes[a]));
        return tieList; 

    }



    /*---------------------------------------- Helper Functions --------------------------------------- */


   

     /**
     * Redistributes surplus ballots from an elected candidate.
     * Ballots are transferred to next preferred candidates who are still in contention.
     * 
     * @param candidateIndex The index of the elected candidate with surplus
     */
    public void redistributeSurplusBallots(int candidateIndex) {
        List<Ballot> allBallots = counterList[candidateIndex];
        int surplusCount = allBallots.size() - droopQuota;
        if (surplusCount <= 0) {
            auditLog.add("No surplus votes to redistribute");
            return;
        }

        auditLog.add("Redistributing " + surplusCount + 
                   " surplus votes from " + election.getCandidates()[candidateIndex]);
        
        // Take ballots above quota for redistribution
        List<Ballot> surplusBallots = new ArrayList<>(allBallots.subList(droopQuota, allBallots.size()));
        // Only keep up to quota ballots for elected candidate
        counterList[candidateIndex] = new ArrayList<>(allBallots.subList(0, droopQuota));
    
        // Process each surplus ballot
        for (Ballot ballot : surplusBallots) {
            STVBallot stvBallot = (STVBallot) ballot;
            boolean transferred = false;
            
            // Keep searching preferences until valid candidate found
            while (!transferred) {
                stvBallot.incrementPreference();
                int nextPref = stvBallot.getPreference();
                if (nextPref == -1) break; // No more preferences
    
                // Find candidate with this preference rank
                for (int i = 0; i < stvBallot.getVote().length; i++) {
                    if (stvBallot.getVote()[i] == nextPref) {
                        // Check if candidate is still in running
                        if (!electedList.containsKey(i) && !nonElectedList.containsKey(i)) {
                            counterList[i].add(stvBallot);
                            time++;
                            firstBallotTimes[i] = time;
                            transferred = true;
                            
                            // Audit log update
                            auditLog.add("Surplus ballot from " + election.getCandidates()[candidateIndex]
                                + " transferred to " + election.getCandidates()[i]);
                            break;
                        }
                    }
                }
            }
            
            if (!transferred) {
                auditLog.add("Ballot " + stvBallot.getId() + " exhausted - no valid preferences");
            }
        }
    }


    /**
     * Identifies and eliminates the candidate with the fewest votes.
     * Handles ties using breakTie() method if any.
     * Updates nonElectedList and triggers ballot redistribution.
     */
    public void eliminateWeakestCandidate() {
        // track the lowest num vote
        int minVotes = Integer.MAX_VALUE; 
        //List tie candidates
        List<Integer> tiedCandidates = new ArrayList<>(); 

        //Loop through counterList to find the lowest vote count for candidates not already elected or eliminated.
        for (int i = 0; i < counterList.length; i++) {
            if (!electedList.containsKey(i) && !nonElectedList.containsKey(i)) {
                int votes = counterList[i].size();
                if (votes < minVotes) {
                    minVotes = votes;
                    tiedCandidates.clear();
                    // Adding to candidate idex to the tiedCandidates list
                    tiedCandidates.add(i); 
                } else if (votes == minVotes) {
                    // Add this candidate to the tie list if same vote count as minVotes
                    tiedCandidates.add(i); 
                }
            }
        }

        // Break tie if needed
        if (tiedCandidates.size() > 1) {
            tiedCandidates = breakTie(tiedCandidates);
        }

        if (!tiedCandidates.isEmpty()) {
            // Get the first index of the sorted tie list based on the last ballot received
            int weakestCandidate = tiedCandidates.get(0); 
            // put that candidate in the nonelected list and their votes
            nonElectedList.put(weakestCandidate, counterList[weakestCandidate].size()); 
            auditLog.add("Candidate " + election.getCandidates()[weakestCandidate] +
                    " eliminated with " + counterList[weakestCandidate].size() + " votes.");
            redistributeEliminatedBallots(weakestCandidate);
        }
    }


    /**
     * Redistributes ballots from an eliminated candidate.
     * Ballots are transferred to next preferred candidates who are still in contention.
     * 
     * @param candidateIndex The index of the eliminated candidate
     */
    public void redistributeEliminatedBallots(int candidateIndex) {
        // get the ballots from that eliminated candidate
        List<Ballot> eliminatedBallots = new ArrayList<>(counterList[candidateIndex]); 
        //remove the ballot from the pile 
        counterList[candidateIndex].clear(); 


        if (eliminatedBallots.size() <= 0) return;
    
        for (Ballot ballot : eliminatedBallots) { 
            STVBallot stvBallot = (STVBallot) ballot;
            int nextPreference = stvBallot.getPreference();
            if (nextPreference != -1) {
                // Increment to track the next preference
                stvBallot.incrementPreference();
                counterList[nextPreference].add(stvBallot);
                time++;
                firstBallotTimes[nextPreference] = time;
            }
        }
    }

    /**
     * Finalizes the winner and loser lists after all rounds complete.
     * Winners are those who reached quota, in order of election.
     * Losers include eliminated candidates and remaining unelected candidates.
     */
    public void determineWinnersAndLosers() {
        winnerList.clear();
        loserList.clear();
    
        // Winners in order of election
        for (int candidateIndex : electedList.keySet()) {
            winnerList.add(election.getCandidates()[candidateIndex]);
        }
    
        // Losers in order of elimination
        for (int candidateIndex : nonElectedList.keySet()) {
            loserList.add(election.getCandidates()[candidateIndex]);
        }

        //Open seat, select the last candidate from the nonElected list
        if (winnerList.size() < election.getInput().getNumSeats()) {
            // Get the last candidate added to the nonElectedList
            List<Integer> nonElectedKeys = new ArrayList<>(nonElectedList.keySet());
       
            if (!nonElectedKeys.isEmpty()) {
                int lastIndex = nonElectedKeys.get(nonElectedKeys.size() - 1);
       
                // Promote to winner
                winnerList.add(election.getCandidates()[lastIndex]);
                auditLog.add("Candidate " + election.getCandidates()[lastIndex] + " from nonElected list is elected");
                loserList.remove(election.getCandidates()[lastIndex]);
            }
        }
 
    
        // Add remaining candidates (not elected/eliminated) in their original index order
        for (int i = 0; i < election.getCandidates().length; i++) {
            if (!electedList.containsKey(i) && !nonElectedList.containsKey(i)) {
                loserList.add(election.getCandidates()[i]);
            }
        }
    }

    /**
     * Validates that all ballots meet the minimum ranking requirements
     * @param ballots List of ballots to validate
     * @param totalCandidates Total number of candidates in the election
     * @throws IllegalArgumentException if any ballot is invalid
     */
    public void validateBallots(List<Ballot> ballots, int totalCandidates) {
        int minRequiredRankings = (int) Math.ceil(totalCandidates / 2.0);
        
        for (Ballot ballot : ballots) {
            //Get all ballots
            STVBallot stvBallot = (STVBallot) ballot;
            int[] ranks = stvBallot.getVote();
            int rankedCount = 0;
            
            for (int rank : ranks) {
                if (rank > 0) rankedCount++;
            }
            
            //
            if (rankedCount < minRequiredRankings) {
                String errorMsg = String.format(
                    "Ballot %d invalid: ranked %d candidates (minimum %d required)",
                    stvBallot.getId(), rankedCount, minRequiredRankings
                );
                throw new IllegalArgumentException(errorMsg);
            }
        }
    }
    

}



