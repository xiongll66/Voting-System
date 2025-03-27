package main;

import java.io.*;
import java.util.*;

class STVAlgorithm extends VotingAlgorithm {
    public Election election;
    public int droopQuota; 
    public List<Ballot>[] counterList;
    public int numWinners;
    public HashMap<Integer, Integer> electedList; 
    public HashMap<Integer, Integer> nonElectedList; 
    public  int[] firstBallotTimes;
    final List<String> auditLog = new ArrayList<>();



    public STVAlgorithm() {
        super();
        this.numWinners = 0;
        this.electedList = new HashMap<>();
        this.nonElectedList = new HashMap<>();
    }

    @Override
    public void runAlgorithm(List<STVBallot> ballots) {
        // Declare counterList as List<STVBallot>[]
        this.counterList = new List[election.candidates.size()];

        // Initialize each element of counterList with a new ArrayList of STVBallot
        for (int i = 0; i < counterList.length; i++) {
            counterList[i] = new ArrayList<>();  // Initializes each list in the array to hold STVBallot
        }


        shuffleBallots(ballots, false);
        calculateDroopQuota(ballots.size());
        redistributeCandidateBallots(ballots);
        generateAuditFile("audit_report.txt");
    }

    public void shuffleBallots(List<STVBallot> ballots, boolean shuffle) {
        if (shuffle) {
            Collections.shuffle(ballots);
            System.out.println("Ballots shuffled.");
        } else {
            System.out.println("Shuffle is off for testing.");
        }
    }

    public void calculateDroopQuota(int numBallots) {
        droopQuota = (numBallots / (election.getInput().numSeats + 1)) + 1;
        System.out.println("Droop Quota: " + droopQuota); 
    }

    // Distribute ballots into piles for candidates
    public void redistributeCandidateBallots(List<STVBallot> ballots) {
        int firstPref; 
        
        // Initialize piles for each candidate
        for (int i = 0; i < counterList.length; i++) { //counterList = [[], [], []] ,3 empty lists for the 3 candidates.
            counterList[i] = new ArrayList<>();
        }

        auditLog.add("Ballots DISTRIBUTION");
        for (int i = 0; i < ballots.size(); i++) {

            STVBallot ballot = ballots.get(i);
            int[] ranks = ballot.getVote();
            firstPref = ballot.getPreference();

            for (int candidateId = 0; candidateId < ranks.length; candidateId++) {
                if (ranks[candidateId] == firstPref) {
                    if (!electedList.containsKey(candidateId)) {
                        counterList[candidateId].add(ballot); // assign the ballot to the candidate
                        auditLog.add("Ballot number " + ballot.getId() + " is voted for candidate " + election.getCandidates().get(candidateId));
                    }
                    break; // stop once we assign the ballot
                }
            }
        }

        while (electedList.size() < election.getInput().numSeats) {
            boolean electedThisRound = false;

            for (int candidate = 0; candidate < counterList.length; candidate++) {
                if (!electedList.containsKey(candidate) && counterList[candidate].size() >= droopQuota) {
                    electedList.put(candidate, counterList[candidate].size());
                    System.out.println("Candidate " + election.getCandidates() +
                            " elected with " + counterList[candidate].size() + " votes.");
                    auditLog.add("Candidate " + election.getCandidates() +
                            " has reached the quota and is elected.");
                    redistributeSurplusBallots(candidate);
                    electedThisRound = true;
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

    // Generate an audit file
    private void generateAuditFile(String fileName) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println("Election Type: STV");
            writer.println("Number of Seats: " + election.getInput().numSeats);
            writer.println("Number of Candidates: " + election.getCandidates().length);
            writer.println("Droop Quota: " + droopQuota);
            
            // Convert winner and loser indices to candidate names
            List<String> winnerNames = new ArrayList<>();
            for (Integer winnerId : electedList.keySet()) {
                winnerNames.add(election.candidates.get(winnerId)); // Get candidate name from the index
            }

            List<String> loserNames = new ArrayList<>();
            for (Integer loserId : nonElectedList.keySet()) {
                loserNames.add(election.candidates.get(loserId)); // Get candidate name from the index
            }
            writer.println("Winners: " + winnerNames);
            writer.println("Losers: " + loserNames);

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
    public void displayResults() {
        System.out.println("**************** Election Results ****************");
        System.out.println("Election Type: STV");
        System.out.println("Number of Seats: " + election.getInput().numSeats);
        System.out.println("Number of Candidates: " + election.getCandidates().length);
        // Convert winner and loser indices to candidate names
        List<String> winnerNames = new ArrayList<>();
        for (Integer winnerId : electedList.keySet()) {
            winnerNames.add(election.getCandidates().get(winnerId)); // Get candidate name from the index
        }

        List<String> loserNames = new ArrayList<>();
        for (Integer loserId : nonElectedList.keySet()) {
            loserNames.add(election.getCandidates().get(loserId)); // Get candidate name from the index
        }
        System.out.println("Winners: " + winnerNames);
        System.out.println("Losers: " + loserNames);
    }

    @Override
    public void breakTieSTV() {
        System.out.println("working on it");
    }


    ///////Helper Functions/////////
 
    // Helper method to get the first preference from a ballot
    private int getFirstPreference(List<Integer> choices) {
        for (int i = 0; i < choices.size(); i++) {
            if (choices.get(i) != null) {
                return i;
            }
        }
        return -1;
    }

    // Helper method to redistribute surplus ballots
    private void redistributeSurplusBallots(int candidateIndex) {
        List<Ballot> surplusBallots = new ArrayList<>(counterList[candidateIndex].subList(droopQuota, counterList[candidateIndex].size()));
        counterList[candidateIndex] = new ArrayList<>(counterList[candidateIndex].subList(0, droopQuota));

        for (Ballot ballot : surplusBallots) {
            int nextPreference = getNextPreference(ballot.choices, candidateIndex);
            if (nextPreference != -1) {
                counterList[nextPreference].add(ballot);
            }
        }
    }

    // Helper method to get the next preference from a ballot
    private int getNextPreference(List<Integer> choices, int currentPreference) {
        for (int i = currentPreference + 1; i < choices.size(); i++) {
            if (choices.get(i) != null) {
                return i;
            }
        }
        return -1;
    }

    // Helper method to eliminate the weakest candidate
    private void eliminateWeakestCandidate() {
        int weakestCandidate = -1;
        int minVotes = Integer.MAX_VALUE;

        for (int i = 0; i < counterList.length; i++) {
            if (!electedList.containsKey(i) && counterList[i].size() < minVotes) {
                weakestCandidate = i;
                minVotes = counterList[i].size();
            }
        }

        if (weakestCandidate != -1) {
            nonElectedList.put(weakestCandidate, counterList[weakestCandidate].size());
            System.out.println("Candidate " + election.candidates.get(weakestCandidate) + " eliminated with " + counterList[weakestCandidate].size() + " votes.");

            // Redistribute ballots of the eliminated candidate
            redistributeEliminatedBallots(weakestCandidate);
        }
    }

    // Helper method to redistribute ballots of the eliminated candidate
    private void redistributeEliminatedBallots(int candidateIndex) {
        List<Ballot> eliminatedBallots = new ArrayList<>(counterList[candidateIndex]);
        counterList[candidateIndex].clear();

        for (Ballot ballot : eliminatedBallots) {
            int nextPreference = getNextPreference(ballot.choices, candidateIndex);
            if (nextPreference != -1) {
                counterList[nextPreference].add(ballot);
            }
        }
    }


    
    // Helper method to determine winners and losers
    private void determineWinnersAndLosers() {
        for (int i = 0; i < election.candidates.size(); i++) {
            if (electedList.containsKey(i)) {
                winners.add(election.candidates.get(i));
            } else {
                losers.add(election.candidates.get(i));
            }
        }

    }

}