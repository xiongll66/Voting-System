package main;

/**
 * Class extending InitialInput representing a plurality election input.
 */
public class PluralityInput extends InitialInput{

    /**
     * Constructor for PluralityInput.
     * 
     * @param algorithm plurality or stv
     * @param numSeats number of seats
     * @param ballotFileName name of ballot file
     */
    public PluralityInput(String algorithm, int numSeats, String ballotFileName) {
        super(algorithm, numSeats, ballotFileName);
    }
}
