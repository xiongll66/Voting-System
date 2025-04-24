package p2main;

public class Header {
    private String electionType;
    private int numSeats;
    private String[] candidates;

    public Header(String electionType, int numSeats, String[] candidates) {
        this.electionType = electionType;
        this.numSeats = numSeats;
        this.candidates = candidates;
    }

    public String getElectionType() {
        return electionType;
    }

    public int getNumSeats() {
        return numSeats;
    }
    
    public String[] getCandidates() {
        return candidates;
    }
}