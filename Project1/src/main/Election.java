package main;

import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * Election is the main driver class of the voting system. It prompts the user for election information,
 * processes the ballot file, and invokes the appropriate voting algorithm.
 */
public class Election {
    private List<Ballot> ballots;
    private String[] candidates;
    private InitialInput input;
    private VotingAlgorithm votingAlgorithm;
    private int numSeats;

    private char electionType;
    private String ballotFileName;
    private String auditFileName;
    private boolean shuffle;

/**
 * Prompts user for input on election information.
 * 
 * @param scanner scanner object used to read user input to store election type, ballot file name, and number of seats
 * @throws Exception 
 */
    public void promptForInput(Scanner scanner) throws Exception {
        // prompt for election type
        System.out.println("Select election type:");
        System.out.println("Type 'p' for plurality voting");
        System.out.println("Type 's' for single transferable voting (STV)");
        System.out.print("Your selection [p/s]: ");

        String electionTypeInput = scanner.nextLine().trim();

        if (electionTypeInput.isEmpty() || (electionTypeInput.charAt(0) != 'p' && electionTypeInput.charAt(0) != 's')) {
            throw new Exception("Invalid election type.");
        }

        electionType = electionTypeInput.charAt(0);
        
        // prompt for ballotFileName
        System.out.print("Enter ballot file's name: ");
        ballotFileName = scanner.nextLine().trim();
        validateBallotFile(ballotFileName);

        if (electionType == 's') {
            
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

        // prompt for number of seats
        System.out.print("Enter number of seats: ");
        numSeats = Integer.parseInt(scanner.nextLine().trim());
    }

    /**
     * Process the ballot file and calls function to create the input object.
     * 
     * @param ballotFileReader Object responsible for reading ballot file
     */
    public void processBallotFile(BallotFileReader ballotFileReader) {
        try {
            String fileName = this.ballotFileName;
            candidates = ballotFileReader.readCandidates(fileName);
            createInputObject();
            ballots = ballotFileReader.readBallots(fileName, input.getAlgorithm());
        } catch (FileNotFoundException e) {
            System.out.println("Error processing ballot file: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Runs election based off of chosen algorithm.
     */
    public void runElection() {
        votingAlgorithm.runAlgorithm(ballots);
    }

    /**
     * Gets input for the elction.
     * 
     * @return the initial input object that contains election setup information.
     */
    public InitialInput getInput() {
        return input;
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
     * Creates input object for election based off of selected election type.
     */
    private void createInputObject() {
        if (electionType == 'p') {
            input = new PluralityInput("plurality", numSeats, ballotFileName);
            votingAlgorithm = new PluralityAlgorithm(this);
        } else {
            input = new STVInput("stv", numSeats, ballotFileName, auditFileName, shuffle);
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
            election.promptForInput(scanner);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }
        election.processBallotFile(ballotFileReader);
        election.runElection();
    }

}