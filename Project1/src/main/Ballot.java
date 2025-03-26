package main;
abstract public class Ballot {
    private int[] vote;

    public Ballot(int[] vote) {
        this.vote = vote;
    }

    abstract public int[] getVote();
}