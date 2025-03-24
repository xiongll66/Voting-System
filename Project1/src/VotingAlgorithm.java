import java.util.ArrayList;
import java.util.List;

abstract class VotingAlgorithm {
    protected Election election; 
    public List<String> winnerList;
    public List<String> loserList;
    public int numWinners; 


    public VotingAlgorithm() {
        this.winnerList = new ArrayList<>();
        this.loserList = new ArrayList<>();
    }

    public abstract void runAlgorithm(List<Ballot> ballots);
    protected abstract void displayResults();
    protected abstract List<Integer> breakTie(List<Integer> tieList);

}