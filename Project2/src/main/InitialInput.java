package main;

/**
 * Abstract class representing initial input for election.
 */
public abstract class InitialInput {

    private String algorithm;
    private int numSeats;
    private String ballotFileName;

    /**
     * Constructor for InitialInput.
     * 
     * @param algorithm either plurality or stv
     * @param numSeats the number of seats
     * @param ballotFileName the name of the ballot file
     */

    public InitialInput(String algorithm, int numSeats, String ballotFileName) {
        this.algorithm = algorithm;
        this.numSeats = numSeats;
        this.ballotFileName = ballotFileName;
    }

    /**
     * Gets election algorithm selected.
     * 
     * @return plurality or stv
     */
    public String getAlgorithm() {
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
}