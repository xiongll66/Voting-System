package main;

/**
 * Class for election input using STV algorithm. 
 */
public class STVInput extends InitialInput{
    private String auditFileName;
    private boolean shuffle;
    
    /**
     * Constructor for STVInput.
     * 
     * @param alg stv
     * @param numSeats number of seats
     * @param ballotFileName name of ballot file
     * @param auditFileName name of audit file
     * @param shuffle true for on, false for off
     */
    public STVInput(String alg, int numSeats, String ballotFileName, String auditFileName, boolean shuffle) {
        super(alg, numSeats, ballotFileName);
        this.auditFileName = auditFileName;
        this.shuffle = shuffle;
    }

    /**
     * Gets name of audit file.
     * 
     * @return name of audit file
     */
    public String getAuditFileName() {
        return auditFileName;
    }

    /**
     * Gets whether ballot should be shuffled or not
     * 
     * @return true if the ballot is to be shuffled, false if not
     */
    public boolean getShuffle() {
        return shuffle;
    }

    /**
     * Sets name of audit file.
     * 
     * @param auditFileName name of audit file to be set
     */
    public void setAuditFileName(String auditFileName) {
        this.auditFileName = auditFileName;
    }

    /**
     * Sets shuffle flag.
     * 
     * @param shuffle true if ballot should be shuffled, false if not
     */
    public void setShuffle(boolean shuffle) {
        this.shuffle = shuffle;
    }
}
