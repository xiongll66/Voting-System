package main;

import java.util.Arrays;

/**
 * Abstract class representing Ballot for election. 
 */
abstract public class Ballot {

    private int[] vote;

    /**
     * Constructor for Ballot.
     * 
     * @param vote Array of integers representing vote
     */
    public Ballot(int[] vote) {
        this.vote = vote;
    }

    /**
     * Method to get vote.
     * 
     * @return an array of integers representing vote
     */
    public int[] getVote() {
        return this.vote;
    };

    /**
     * Method to compare ballots.
     * 
     * @param o the ballot object that's compared against
     * @return true if ballot votes match, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ballot that = (Ballot) o;
        return Arrays.equals(this.vote, that.getVote());
    }
}