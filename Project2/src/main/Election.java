package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

/**
 * Election is the main driver class of the voting system. It prompts the user for election information,
 * processes the ballot file, and invokes the appropriate voting algorithm.
 */
public class Election {
    private List<Ballot> ballots;
    private String[] candidates;
    private VotingAlgorithm votingAlgorithm;
    private int numSeats;

    private String electionType;
    private String ballotFileName;
    private String auditFileName;
    private boolean shuffle;

/**
 * Prompts user for a ballot file name and parses the file's header to initialize election information variables
 * 
 * @param scanner scanner object used to read user input to store election type, ballot file name, and number of seats
 * @throws Exception throws an exception if there are invalid inputs
 */
    public void promptForInput(Scanner scanner, BallotFileReader ballotFileReader) throws Exception {
        
        // prompt for ballotFileName
        System.out.print("Enter ballot file's name: ");
        ballotFileName = scanner.nextLine().trim();
        validateBallotFile(ballotFileName);

        // parse header to set election info variables
        Header header = ballotFileReader.readHeader(ballotFileName);
        setStateVariablesFromHeader(header);

        if (electionType.equals("STV")) {
            
            // prompt for auditFileName
            System.out.print("Enter a name for your audit file (without the extension): ");
            auditFileName = scanner.nextLine().trim();
            if (auditFileName.isEmpty()) {
                System.out.println("Error: Audit file name can't be empty.");
                System.exit(1);
            }

            // prompt for shuffle
            System.out.println("Turn shuffle on or off:");
            System.out.print("Type '1' for on, or '0' for off: ");
            String shuffleInput = scanner.nextLine();
            shuffle = parseShuffle(shuffleInput);

        }

        
    }

    /**
     * Process the ballots from the ballot file.
     * 
     * @param ballotFileReader Object responsible for reading ballots from the ballot file
     */
    public void processBallotFile(BallotFileReader ballotFileReader) {
        try {
            String fileName = this.ballotFileName;
            ballots = ballotFileReader.readBallots(fileName, electionType);
        } catch (FileNotFoundException e) {
            System.out.println("Error processing ballot file: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Runs election based off of chosen algorithm.
     */
    private void runElection() {
        votingAlgorithm.runAlgorithm(ballots);
    }

    /**
     * Gets the election type string
     * @return PV or STV
     */
    public String getElectionType() {
        return electionType;
    }

    /**
     * Gets string array of candidates for election.
     * 
     * @return String array of candidates
     */
    public String[] getCandidates() {
        return candidates;
    }

    /**
     * Sets the candidates for the election.
     *
     * @param candidates An array of strings representing the candidates' names.
     */
    public void setCandidates(String[] candidates) {
        this.candidates = candidates;
    }

    /**
     * Gets list of ballots cast in election.
     * 
     * @return list of ballot objects
     */
    public List<Ballot> getBallots() {
        return ballots;
    }

    /**
     * Gets number of seats.
     * 
     * @return number of seats
     */
    public int getNumSeats() {
        return numSeats;
    }

    /**
     * Gets VotingAlgorithm.
     * 
     * @return the VotingAlgorithm class that is picked.
     */
    public VotingAlgorithm getVotingAlgorithm() {
        return votingAlgorithm;
    }

    /**
     * Gets audit file name
     * @return String containing audit file's name
     */
    public String getAuditFileName() {
        return auditFileName;
    }

    /**
     * Gets shuffle value
     * @return true for on, false for off
     */
    public boolean getShuffle() {
        return shuffle;
    }

    /**
     * Checks ballot file to make sure it exists and ends with '.csv'.
     * 
     * @param fileName the name of the ballot file to check
     * @throws Exception 
     */
    private void validateBallotFile(String fileName) throws Exception {
        File file = new File(fileName);
        if (!file.exists() || !fileName.endsWith(".csv")) {
            throw new Exception("Invalid ballot file.");
        }
    }

    /**
     * Checks whether shuffle is on or off.
     * 
     * @param shuffleInput the input for the shuffle
     * @return True if shuffle is on, False otherwise
     */
    private boolean parseShuffle(String shuffleInput) {
        if (shuffleInput.isEmpty() || (shuffleInput.charAt(0) != '1' && shuffleInput.charAt(0) != '0')) {
            System.out.println("Error: invalid shuffle input.");
            System.exit(1);
        }
        if (shuffleInput.charAt(0) == '1') {
            return true;
        } else {
            return false;
        }
    }

    /**
     * sets the Election class's state variables
     * @param header contains election information from the ballot file's header
     */
    private void setStateVariablesFromHeader(Header header) {
        electionType = header.getElectionType();
        numSeats = header.getNumSeats();
        candidates = header.getCandidates();

        if (electionType.equals("PV")) {
            votingAlgorithm = new PluralityAlgorithm(this);
        } else if (electionType.equals("STV")) {
            votingAlgorithm = new STVAlgorithm(this);
        }
    }

    /**
     * Main method to run election program. 
     * Initializes the election, prompts the user for input, processes the ballot file, and runs the election.
     * 
     * @param args command line arguments
     */
    public static void main(String[] args) {
        Election election = new Election();
        Scanner scanner = new Scanner(System.in);
        BallotFileReader ballotFileReader = new BallotFileReader();
        try {
            election.promptForInput(scanner, ballotFileReader);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }
        election.processBallotFile(ballotFileReader);
        election.runElection();
    }

}