package main;

import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;


public class Election {
    private List<Ballot> ballots;
    private String[] candidates;
    private InitialInput input;
    private VotingAlgorithm votingAlgorithm;

    private char electionType;
    private String ballotFileName;
    private String auditFileName;
    private boolean shuffle;

    public void promptForInput(Scanner scanner) {
        // prompt for election type
        System.out.println("Select election type:");
        System.out.println("Type 'p' for plurality voting");
        System.out.println("Type 's' for single transferable voting (STV)");
        System.out.print("Your selection [p/s]: ");

        String electionTypeInput = scanner.nextLine();

        if (electionTypeInput.isEmpty() || (electionTypeInput.charAt(0) != 'p' && electionTypeInput.charAt(0) != 's')) {
            System.out.println("Error: You didn't select a valid election type.");
            System.exit(1);
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
    }

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

    public void runElection() {
        votingAlgorithm.runAlgorithm(ballots);
    }

    public InitialInput getInput() {
        return input;
    }

    public String[] getCandidates() {
        return candidates;
    }

    public List<Ballot> getBallots() {
        return ballots;
    }

    private void validateBallotFile(String fileName) {
        File file = new File(fileName);
        if (!file.exists() || !fileName.endsWith(".csv")) {
            System.out.println("Error: invalid ballot file");
            System.exit(1);
        }
    }

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

    private void createInputObject() {
        int numSeats = candidates.length;
        if (electionType == 'p') {
            input = new PluralityInput("plurality", numSeats, ballotFileName);
            votingAlgorithm = new PluralityAlgorithm(this);
        } else {
            input = new STVInput("stv", numSeats, ballotFileName, auditFileName, shuffle);
            votingAlgorithm = new STVAlgorithm();
        }
    }

    public static void main(String[] args) {
        Election election = new Election();
        Scanner scanner = new Scanner(System.in);
        BallotFileReader ballotFileReader = new BallotFileReader();
        election.promptForInput(scanner);
        election.processBallotFile(ballotFileReader);
        election.runElection();
    }

}