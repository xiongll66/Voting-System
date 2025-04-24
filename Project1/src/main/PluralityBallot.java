package main;

import p2main.Ballot;

/**
 * Class that extends Ballot to represent a Plurality Ballot.
 */
public class PluralityBallot extends Ballot {

    /**
     * Constructor for PluralityBallot.
     * 
     * @param vote integer array containing the vote
     */
    public PluralityBallot(int[] vote) {
        super(vote);
    }
}
