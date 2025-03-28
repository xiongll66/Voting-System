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
    public void runAlgorithm(List<Ballot> ballots) {
        // Declare counterList as List<STVBallot>[]
        this.counterList = new List[election.getCandidates().length];

        // Initialize each element of counterList with a new ArrayList of STVBallot
        for (int i = 0; i < counterList.length; i++) {
            counterList[i] = new ArrayList<>();  // Initializes each list in the array to hold STVBallot
        }

        // Convert Ballots to STVBallots with IDs
        List<STVBallot> stvBallots = new ArrayList<>();
        int ballotId = 1; // Starting ID
        
        for (Ballot ballot : ballots) {
            // Create new STVBallot with unique ID and the original vote
            STVBallot stvBallot = new STVBallot(ballotId++, ballot.getVote());
            stvBallots.add(stvBallot);
        }

        calculateDroopQuota(ballots.size());
        redistributeCandidateBallots(stvBallots);
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
        shuffleBallots(ballots, false);
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
                        auditLog.add("Ballot number " + ballot.getId() + " is voted for candidate " + election.getCandidates()[candidateId]);
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
                winnerNames.add(election.getCandidates()[winnerId]); // Get candidate name from the index
            }

            List<String> loserNames = new ArrayList<>();
            for (Integer loserId : nonElectedList.keySet()) {
                loserNames.add(election.getCandidates()[loserId]); // Get candidate name from the index
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
            winnerNames.add(election.getCandidates()[winnerId]); // Get candidate name from the index
        }

        List<String> loserNames = new ArrayList<>();
        for (Integer loserId : nonElectedList.keySet()) {
            loserNames.add(election.getCandidates()[loserId]); // Get candidate name from the index
        }
        System.out.println("Winners: " + winnerNames);
        System.out.println("Losers: " + loserNames);
    }

    @Override
    protected List<Integer> breakTie(List<Integer> tieList) {
        if (tieList.size() == 1) {
            return tieList; // No tie to break
        }

        return tieList;


        //choosing a surplus to transfer when tie

        //choosing a candidate to eliminate when tie

        //choosing winners for the last seat when tie

    }


    ///////Helper Functions/////////
 
    // Helper method to redistribute surplus ballots
    private void redistributeSurplusBallots(int candidateIndex) {
        List<Ballot> allBallots = counterList[candidateIndex];
        int surplusCount = allBallots.size() - droopQuota;

        if (surplusCount <= 0) return;
        List<Ballot> surplusBallots = new ArrayList<>(allBallots.subList(droopQuota, allBallots.size()));
        counterList[candidateIndex] = new ArrayList<>(allBallots.subList(0, droopQuota));

        for (Ballot ballot : surplusBallots) {
            int nextPreference = getNextPreference(ballot.getVote(), candidateIndex);
            if (nextPreference != -1) {
                counterList[nextPreference].add(ballot);
            }
        }
    }

    private int getNextPreference(int[] vote, int currentCandidate) {
        int nextRank = vote[currentCandidate] + 1; 

        for (int i = 0; i < vote.length; i++) {
            if (vote[i] == nextRank && !electedList.containsKey(i) && !nonElectedList.containsKey(i)) {
                return i;
            }
        }
        return -1; // If no valid next preference is found
    }

    private void eliminateWeakestCandidate() {
        int weakestCandidate = -1;
        int minVotes = Integer.MAX_VALUE;

        for (int i = 0; i < counterList.length; i++) {
            if (!electedList.containsKey(i) && !nonElectedList.containsKey(i)) {
                int votes = counterList[i].size();
                if (votes < minVotes) {
                    weakestCandidate = i;
                    minVotes = votes;
                }
            }
        }

        if (weakestCandidate != -1) {
            nonElectedList.put(weakestCandidate, counterList[weakestCandidate].size());
            System.out.println("Candidate " + election.getCandidates()[weakestCandidate] +
                    " eliminated with " + counterList[weakestCandidate].size() + " votes.");
            auditLog.add("Candidate " + election.getCandidates()[weakestCandidate] +
                    " eliminated with " + counterList[weakestCandidate].size() + " votes.");
            redistributeEliminatedBallots(weakestCandidate);
        }
    }

    private void redistributeEliminatedBallots(int candidateIndex) {
        List<Ballot> eliminatedBallots = new ArrayList<>(counterList[candidateIndex]);
        counterList[candidateIndex].clear();

        for (Ballot ballot : eliminatedBallots) {
            int nextPreference = getNextPreference(ballot.getVote(), candidateIndex);
            if (nextPreference != -1) {
                counterList[nextPreference].add(ballot);
            }
        }
    }

    private void determineWinnersAndLosers() {
        for (int i = 0; i < election.getCandidates().length; i++) {
            if (electedList.containsKey(i)) {
                winnerList.add(election.getCandidates()[i]);
            } else {
                loserList.add(election.getCandidates()[i]);
            }
        }
    }
}


