import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PluralityAlgorithm extends VotingAlgorithm{
    protected Election election; 
    public List<String> winnerList;
    public List<String> loserList;
    int[] counterList;
    List<String> sortCanList;
    int[] sortVoteList; 
    

    public PluralityAlgorithm(Election election) {
        this.election = election; 
        this.winnerList = new ArrayList<>();
        this.loserList = new ArrayList<>();
        this.sortCanList = new ArrayList<>(election.candidates); // clone to sort
        this.sortVoteList = counterList.clone(); // clone to sort 
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
    // Break tie if there is any 
    protected List<Integer> breakTie(List<Integer> tieList) {
        Random random = new Random();
        List<Integer> orderedTieList = new ArrayList<>(); // To store in order from first winner to last loser
        // Randomly select candidates until all are ordered
        while (!tieList.isEmpty()) {
            int randomIndex = random.nextInt(tieList.size()); // Pick a random index
            int candidateIndex = tieList.remove(randomIndex); // Remove from tieList
            orderedTieList.add(candidateIndex); // Store in ordered list
        }
        
        return orderedTieList;
    }
    // Count the vote to find winner or find a tie
    private void calculateWinner() {  // Can be improve with better sort algorithm(later)

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


        // Add everything to winner list based on seat avilable 
        int i;
        for (i = 0; i < election.numSeats; i++) {
            winnerList.add(sortCanList.get(finalSortedWinners.get(i))); 
        }
        for (; i < finalSortedWinners.size(); i++) {
            loserList.add(sortCanList.get(finalSortedWinners.get(i))); 
        }
    }
    // Print result to screen
    @Override
    protected void displayResults() {
        System.out.println("Plurality Results:");
        System.out.println("Election Type: " + election.algorithmType);
        System.out.println("Number of Seats: " + election.numSeats);
        System.out.println("Number of Candidates: " + election.candidates.size());
        System.out.println("Number of Ballots: " + election.ballots.size());
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
    
   
    
   // What is called when it is Plurality election 
    public void runAlgorithm(List<Ballot> ballots) {
        pluralityAlgorithmFunction(ballots); 
        calculateWinner(); 
        displayResults();
    }
}
   