package main;

import main.Ballot;

/**
 * Class extending Ballot that represents an stv election ballot.
 */
public class STVBallot extends Ballot {
    private int id;
    private int preference;

    /**
     * Constructor for STVBallot.
     * 
     * @param id an integer identifier for the ballot
     * @param vote an integer array containing the vote
     */
    public STVBallot(int id, int[] vote) {
        super(vote);
        this.id = id;
        this.preference = 1;
    }

    /**
     * Increments the preference of the ballot for redistribution.
     */
    public void incrementPreference() {
        this.preference++;
    }

    /**
     * Gets the id of the ballot.
     * 
     * @return id of the ballot
     */
    public int getId() {
        return id;
    }
    
    /**
     * Gets the preference number of the ballot.
     * 
     * @return current preference number of the ballot
     */
    public int getPreference() {
        return preference;
    }
}