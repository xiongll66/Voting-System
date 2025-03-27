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
    public void redistributeCandidateBallots(List<Ballot> ballots) {
        // Initialize piles for each candidate
        for (int i = 0; i < counterList.length; i++) {
            counterList[i] = new ArrayList<>();
        }

        // Distribute ballots to candidates based on first preference
        for (Ballot ballot : ballots) {
            int candidateIndex = getFirstPreference(ballot.choices);
            if (candidateIndex != -1) {
                counterList[candidateIndex].add(ballot);
            }
        }

        // Elect candidates who meet the quota and redistribute ballots
        while (electedList.size() < election.numSeats) {
            // Check if any candidate meets the quota
            for (int i = 0; i < counterList.length; i++) {
                if (counterList[i].size() >= droopQuota && !electedList.containsKey(i)) {
                    electedList.put(i, counterList[i].size());
                    System.out.println("Candidate " + election.candidates.get(i) + " elected with " + counterList[i].size() + " votes.");

                    // Redistribute surplus ballots
                    redistributeSurplusBallots(i);
                }
            }

            // Eliminate the candidate with the fewest votes
            eliminateWeakestCandidate();
        }
        //Finalize winners and losers
        determineWinnersAndLosers();
    }

    // Generate an audit file
    public void generateAuditFile(String fileName) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println("Election Type: STV");
            writer.println("Number of Seats: " + election.numSeats);
            writer.println("Number of Candidates: " + election.candidates.size());
            writer.println("Winners: " + winners);
            writer.println("Losers: " + losers);
            writer.println("Droop Quota: " + droopQuota);

            writer.println("\nBallot Distribution:");
            for (int i = 0; i < counterList.length; i++) {
                writer.println("Candidate " + election.candidates.get(i) + ": " + counterList[i].size() + " ballots");
            }

            System.out.println("Audit file generated: " + fileName);
        } catch (IOException e) {
            System.err.println("Error generating audit file: " + e.getMessage());
        }
    }

    @Override
    public void displaySTVResults() {
        System.out.println("STV Results:");
        System.out.println("Election Type: STV");
        System.out.println("Number of Seats: " + election.numSeats);
        System.out.println("Number of Candidates: " + election.candidates.size());
        System.out.println("Winners: " + winners);
        System.out.println("Losers: " + losers);
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