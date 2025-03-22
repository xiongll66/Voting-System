
/**
 * Abstract class representing initial input for election.
 */
public abstract class InitialInput {

    /**
     * Represents 2 types of elections: Plurality and STV.
     */
    public enum Algorithm {
        PLURALITY, STV
    }

    public Algorithm algorithm;
    public int numSeats;
    public String ballotFileName;

    /**
     * Gets election algorithm selected.
     * 
     * @return Plurality or STV
     */
    public Algorithm getAlgorithm() {
        return algorithm;
    }

    /**
     * Gets number of seats in election.
     * 
     * @return number of seats
     */
    public int getNumSeats() {
        return numSeats;
    }

    /**
     * Gets name of ballot file.
     * 
     * @return name of ballot file
     */
    public String getBallotFileName() {
        return ballotFileName;
    }

    /**
     * Sets election algorithm.
     * 
     * @param algorithm the election algorithm to be set (Plurality of STV)
     */
    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    /**
     * Sets the number of seats in election.
     * 
     * @param numSeats the number of seats to set
     */
    public void setNumSeats(int numSeats) {
        this.numSeats = numSeats;
    }

    /**
     * Sets the name of the ballot file.
     * 
     * @param ballotFileName the name of the ballot file to set
     */
    public void setBallotFileName(String ballotFileName) {
        this.ballotFileName = ballotFileName;
    }
}