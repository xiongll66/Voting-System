package p2main;

import p2main.Ballot;

/**
 * Class that extends Ballot to represent a MV Ballot.
 */
public class MVBallot extends Ballot{

    /**
     * Constructor for MVBallot.
     * 
     * @param vote integer array containing the vote
     */
    public MVBallot(int[] vote) {
        super(vote);
    }
}
