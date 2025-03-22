import java.util.ArrayList;
import java.util.List;

abstract class VotingAlgorithm {
    public List<String> winners;
    public List<String> losers;

    public VotingAlgorithm() {
        this.winners = new ArrayList<>();
        this.losers = new ArrayList<>();
    }

    public abstract void runSTVAlgorithm(List<Ballot> ballots);
    public abstract void displaySTVResults();
    public abstract void breakTieSTV();
}