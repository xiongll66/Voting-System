import java.util.List;


class Election {
    public List<String> candidates;
    public int numSeats;
    public String algorithmType;
    public List<Ballot> ballots;


    public Election(List<String> candidates, int numSeats, String algorithmType, List<Ballot> ballots) {
        this.candidates = candidates;
        this.numSeats = numSeats;
        this.algorithmType = algorithmType;
        this.ballots = ballots;
    }

}