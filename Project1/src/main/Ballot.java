package main;

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
     * Abstract method to get vote.
     * 
     * @return an array of integers representing vote
     */
    abstract public int[] getVote();
}