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
     * method to get vote.
     * 
     * @return an array of integers representing vote
     */
    public int[] getVote() {
        return this.vote;
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ballot that = (Ballot) o;
        return Arrays.equals(this.vote, that.getVote());
    }
}