/**
 * STVAlgorithm.java
 *
 * The STValgorithm handles vote redistribution through multiple rounds until 
 * all seats are filled or no more candidates can be elected.
 * 
 * This class handles vote redistribution, surplus management, candidate elimination,
 * winner and loser determination, generate an audit report for an STV election process.
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
    public Election election;
    public int droopQuota; 
    public List<Ballot>[] counterList;
    public LinkedHashMap<Integer, Integer> electedList;  // Preserves insertion order
    public LinkedHashMap<Integer, Integer> nonElectedList;  
    private int time;
    private int[] firstBallotTimes;
    final List<String> auditLog = new ArrayList<>();

    /**
     * Constructor for STVAlgorithm. Initializes elected and non-elected lists.
     */
    public STVAlgorithm() {
        super();
        this.numWinners = 0;
        this.electedList = new LinkedHashMap<>();
        this.nonElectedList = new LinkedHashMap<>();
    }

    /**
     * Executes the STV voting algorithm.
     * 
     * @param ballots The list of ballots to process.
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
        calculateDroopQuota(ballots.size());
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
     * The Droop quota based on the number of ballots and seats.
     *
     * @param numBallots The total number of ballots.
     */
    public void calculateDroopQuota(int numBallots) {
        droopQuota = (numBallots / (election.getInput().numSeats + 1)) + 1;
    }

    public void redistributeCandidateBallots(List<Ballot> ballots) {
        shuffleBallots(ballots, ((STVInput) election.getInput()).getShuffle());
        for (int i = 0; i < counterList.length; i++) {
            counterList[i] = new ArrayList<>();
        }


        auditLog.add("Ballots DISTRIBUTION");

        //assign all ballots to all candidates
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

        while (electedList.size() < election.getInput().numSeats) {
            boolean electedThisRound = false;

            for (int candidate = 0; candidate < counterList.length; candidate++) { //for all candidate
                if (!electedList.containsKey(candidate) && counterList[candidate].size() >= droopQuota) { //if not in the elected list and reach the quota
                    electedList.put(candidate, counterList[candidate].size()); // added the candidate to the elective list
                    auditLog.add("Candidate " + election.getCandidates()[candidate] +
                            " has reached the quota and is elected.");
                    redistributeSurplusBallots(candidate); // redistributed surplus ballot
                    electedThisRound = true;
                    break;
                }
            }
                
            if (!electedThisRound) {
                eliminateWeakestCandidate();
            }

            if (electedList.size() + nonElectedList.size() >= election.getCandidates().length) {
                break;
            }
        }

        determineWinnersAndLosers();
    }

    private void generateAuditFile(String fileName) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println("Election Type: STV");
            writer.println("Number of Seats: " + election.getInput().numSeats);
            writer.println("Number of Candidates: " + election.getCandidates().length);
            writer.println("Droop Quota: " + droopQuota);
            writer.println("Winners: " + winnerList);
            writer.println("Losers: " + loserList);

            writer.println("\nAudit Log:");
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
     * Displays the final results of the STV election.
     */
    public void displayResults() {
        System.out.println("**************** Election Results ****************");
        System.out.println("Election Type: STV");
        System.out.println("Number of Seats: " + election.getInput().numSeats);
        System.out.println("Number of Candidates: " + election.getCandidates().length);
        System.out.println("Winners: " + winnerList);
        System.out.println("Losers: " + loserList);
    }

    /**
     * Breaks ties between candidates using ballot timestamp logic.
     *
     * @param tieList A list of candidate indexes with equal votes.
     * @return A list of candidate indexes sorted based on tie-breaking rules.
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


    /**
     * Helper method to redistribute surplus ballots
     * Redistributes surplus ballots for a given elected candidate.
     *
     * @param candidateIndex The index of the elected candidate.
     */
    private void redistributeSurplusBallots(int candidateIndex) {
        List<Ballot> allBallots = counterList[candidateIndex];
        int surplusCount = allBallots.size() - droopQuota;
        if (surplusCount <= 0) return;
    
        List<Ballot> surplusBallots = new ArrayList<>(allBallots.subList(droopQuota, allBallots.size()));
        counterList[candidateIndex] = new ArrayList<>(allBallots.subList(0, droopQuota));
    
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
                        // Check if candidate is eligible
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



    private void eliminateWeakestCandidate() {
        int minVotes = Integer.MAX_VALUE; // track the lowest num vote
        List<Integer> tiedCandidates = new ArrayList<>(); //track tie candidate

        //Loop through counterList to find the lowest vote count for candidates not already elected or eliminated.
        for (int i = 0; i < counterList.length; i++) {
            if (!electedList.containsKey(i) && !nonElectedList.containsKey(i)) {
                int votes = counterList[i].size();
                if (votes < minVotes) {
                    minVotes = votes;
                    tiedCandidates.clear();
                    tiedCandidates.add(i); // adding to candidate idex to the tiedCandidates list
                } else if (votes == minVotes) {
                    tiedCandidates.add(i); // Add this candidate to the tie list if same vote count as minVotes
                }
            }
        }

        if (tiedCandidates.size() > 1) {
            tiedCandidates = breakTie(tiedCandidates);
        }

        if (!tiedCandidates.isEmpty()) {
            int weakestCandidate = tiedCandidates.get(0); // get the first index of the sorted tie list based on the last ballot received
            
            nonElectedList.put(weakestCandidate, counterList[weakestCandidate].size()); // put that candidate in the nonelected list and their votes
            auditLog.add("Candidate " + election.getCandidates()[weakestCandidate] +
                    " eliminated with " + counterList[weakestCandidate].size() + " votes.");
            redistributeEliminatedBallots(weakestCandidate);
        }
    }

    private void redistributeEliminatedBallots(int candidateIndex) {
        List<Ballot> eliminatedBallots = new ArrayList<>(counterList[candidateIndex]); // get the ballots from that eliminated candidate
        counterList[candidateIndex].clear(); //remove the ballot from the pile 


        if (eliminatedBallots.size() <= 0) return;
    
        for (Ballot ballot : eliminatedBallots) { // for all eliminatedBallots
            STVBallot stvBallot = (STVBallot) ballot;
            int nextPreference = stvBallot.getPreference();
            if (nextPreference != -1) {
                stvBallot.incrementPreference(); // Increment to track the next preference
                counterList[nextPreference].add(stvBallot);
                time++;
                firstBallotTimes[nextPreference] = time;
            }
        }
    }

    private void determineWinnersAndLosers() {
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
    
        // Add remaining candidates (not elected/eliminated) in their original index order
        for (int i = 0; i < election.getCandidates().length; i++) {
            if (!electedList.containsKey(i) && !nonElectedList.containsKey(i)) {
                loserList.add(election.getCandidates()[i]);
            }
        }
    }

    
}



