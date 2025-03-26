package main;
public class PluralityBallot extends Ballot {
    private int[] vote;

    public PluralityBallot(int[] vote) {
        super(vote);
    }
    
    public int[] getVote() {
        return this.vote;
    }
}
