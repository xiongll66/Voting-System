import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PluralityAlgorithm extends VotingAlgorithm{
    private Election election; 
    private List<String> winnerList;
    private List<String> loserList;
    int[] counterList;
    

    public PluralityAlgorithm(Election election) {
        this.election = election; 
        this.winnerList = new ArrayList<>();
        this.loserList = new ArrayList<>();
    }

    public void runSTVAlgorithm(List<Ballot> ballots){ 
        return;
    }
    public void displaySTVResults() {
        return;
    }
    public void breakTieSTV() {
        return;
    }
    // filling in seats ?
    // Count the votes from ballots and give to candidate 
    // Make a checker to check if there's more then 1 1s
    private void pluralityAlgorithmFunction(List<Ballot> ballots) {
        // Check if ballot have candidate
        if (ballots.size() <= 0) {
            System.out.println("Error: ballots have no cadidate");
            return; 
        }
        // Find how many candidate and initialize counterList to keep count of votes
        this.counterList = new int[ballots.get(0).getVote().length];       
        for (int i = 0; i < ballots.size(); i++) {
            // Count candidate index
            int canIndexCounter = 0; 
            boolean foundVote = false; 
            int[] currBallotVote = ballots.get(i).getVote(); 

            // Loop through each ballot votes until vote found or exit 
            while (canIndexCounter < currBallotVote.length) {
                // Check if ballot vote is either 1 or 0 
                if (currBallotVote[canIndexCounter] != 0 && currBallotVote[canIndexCounter] != 1) {
                    System.out.println("Error: invalid ballot vote (vote is not 0 or 1)");
                    return; 
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
 
    
    // Break tie
    private void breakTie(List<Integer> tieList) {
        Random random = new Random();
        // Randomly pick winner 
        int randomWinner = tieList.get(random.nextInt(tieList.size()));

        // Set a winner and losers 
        winnerList.add(election.candidates.get(randomWinner)); 
        for (int i = 0; i < counterList.length; i++) {
            if (i != randomWinner) {
                loserList.add(election.candidates.get(i));
            }
        }
    }
    // Count the vote to find winner or find a tie
    private void calculateWinner() {
        int mostVote = 0; 
        List<Integer> tieList = new ArrayList<>();

        // Find most voted candidate
        for (int i = 0; i < counterList.length; i++) { 
            if (counterList[i] > counterList[mostVote]) {
                mostVote = i; 
                tieList.clear();  // Clear previous ties
                tieList.add(i); 
            }
            // Check for tie
            if (counterList[i] == counterList[mostVote]) {
                tieList.add(i);
            }
        }
        // If there is a tie, move to tie phase 
        if (tieList.size() > 1) {
            breakTie(tieList); // Randomly select a winner
            return;  
        }
        
        // Set a winner and losers 
        winnerList.add(election.candidates.get(mostVote));
        for (int i = 0; i < counterList.length; i++) {
            if (i != mostVote) {
                loserList.add(election.candidates.get(i));
            }
        }
    }
    // Print result to screen
    private void displayResult() {
        System.out.println("Election Type: " + election.algorithmType);
        System.out.println("Number of Ballots: " + election.ballots.size());
        System.out.println("Number of Seats: " + election.numSeats);
        System.out.println("Number of Candidates: " + election.candidates.size());
        System.out.println();    

        int totalVotes = 0;
        for (int votes : counterList) {
            totalVotes += votes;
        }

        System.out.println("Election Results:");
        
        // Display winners with vote percentages
        System.out.println("\nWinners:");
        for (String winner : winnerList) {
            int winnerIndex = election.candidates.indexOf(winner);
            double percentage = (counterList[winnerIndex] / (double) totalVotes) * 100;
            System.out.printf("%s - Votes: %d (%.2f%%)\n", winner, counterList[winnerIndex], percentage);
        }

        // Display losers with vote percentages
        System.out.println("\nLosers:");
        for (String loser : loserList) {
            int loserIndex = election.candidates.indexOf(loser);
            double percentage = (counterList[loserIndex] / (double) totalVotes) * 100;
            System.out.printf("%s - Votes: %d (%.2f%%)\n", loser, counterList[loserIndex], percentage);
        }

    }
    
   
    
   // What is called when it is Plurality election 
    public void runAlgorithm(List<Ballot> ballots) {
        pluralityAlgorithmFunction(ballots); 
        calculateWinner(); 
        displayResult();
    }
}
