package p2main;

/**
 * Header is a class that stores ballot file header information.
 */
public class Header {
    private String electionType;
    private int numSeats;
    private String[] candidates;

    /**
     * Constructor for Header
     * @param electionType the type of election algorithm that will be used
     * @param numSeats the number of seats
     * @param candidates the list of candidate names
     */
    public Header(String electionType, int numSeats, String[] candidates) {
        this.electionType = electionType;
        this.numSeats = numSeats;
        this.candidates = candidates;
    }

    /**
     * gets election type
     * @return String containing the election type
     */
    public String getElectionType() {
        return electionType;
    }

    /**
     * gets number of seats
     * @return int representing number of seats
     */
    public int getNumSeats() {
        return numSeats;
    }
    
    /**
     * gets candidate names
     * @return String array containing the candidates' names
     */
    public String[] getCandidates() {
        return candidates;
    }
}