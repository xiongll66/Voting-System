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

    public void promptForInput() {
        Scanner scanner = new Scanner(System.in);

        char electionType;
        int numSeats = 0;
        String ballotFileName;

        // prompt for election type
        System.out.println("Select election type:");
        System.out.println("Type 'p' for plurality voting");
        System.out.println("Type 's' for single transferable voting (STV)");
        System.out.print("Your selection [p/s]: ");

        String electionTypeInput = scanner.nextLine();

        if (electionTypeInput.isEmpty() || electionTypeInput.charAt(0) != 'p' || electionTypeInput.charAt(0) != 's') {
            System.out.println("Error, you didn't select a valid election type.");
            System.exit(1);
        }

        electionType = electionTypeInput.charAt(0);
        
        // prompt for ballotFileName
        System.out.println("Enter ballot file's name: ");

        String ballotFileNameInput = scanner.nextLine();
        File f = new File(ballotFileNameInput);
        String extension = ballotFileNameInput.split(".")[1];

        if (!f.isFile() || !extension.equals("csv")) {
            System.out.println("Error, you didn't enter a valid ballot file name or it doesn't exist.");
            System.exit(1);
        }

        ballotFileName = ballotFileNameInput;

        // set numSeats and candidates from number of columns in ballot file
        File ballotFile = new File(ballotFileName);
        Scanner ballotFileReader;
        try {
            ballotFileReader = new Scanner(ballotFile);
            if (ballotFileReader.hasNextLine()) {
                candidates = ballotFileReader.nextLine().split(",");
                numSeats = candidates.length;
            }
            ballotFileReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error opening ballot file.");
            e.printStackTrace();
        }

        if (electionType == 'p') {
            input = new PluralityInput("plurality", numSeats, ballotFileName);
            votingAlgorithm = new PluralityAlgorithm(this);
        } else {
            String auditFileName;
            boolean shuffle;
            
            // prompt for auditFileName
            System.out.print("Enter a name for your audit file (without the extension): ");
            String auditFileNameInput = scanner.nextLine();
            if (auditFileNameInput.contains(".")) {
                System.out.println("Invalid audit file name.");
                System.exit(1);
            }
            auditFileName = auditFileNameInput;

            // prompt for shuffle
            System.out.println("Turn shuffle on or off:");
            System.out.print("Type '1' for on, or '0' for off: ");
            String shuffleInput = scanner.nextLine();
            if (shuffleInput.isEmpty() || shuffleInput.charAt(0) != '1' || shuffleInput.charAt(0) != '0') {
                System.out.println("Error, invalid shuffle input.");
                System.exit(1);
            }
            if (shuffleInput.charAt(0) == '1') {
                shuffle = false;
            } else {
                shuffle = true;
            }

            input = new STVInput("stv", numSeats, ballotFileName, auditFileName, shuffle);
            votingAlgorithm = new STVAlgorithm();
        }

        scanner.close();
    }

    public void processBallotFile() {
        String ballotFileName = input.getBallotFileName();
        File ballotFile = new File(ballotFileName);
        Scanner ballotFileReader;
        try {
            ballotFileReader = new Scanner(ballotFile);
            // skip first line of candidates
            ballotFileReader.nextLine();

            int id = 0;
            // iterate through all ballots
            while (ballotFileReader.hasNextLine()) {
                Ballot newBallot;
                String[] ballotLine = ballotFileReader.nextLine().split(",");
                int[] vote = new int[ballotLine.length];
                for (int i = 0; i < ballotLine.length; i++) {
                    vote[i] = Integer.parseInt(ballotLine[i]);
                }
                if (input.getAlgorithm() == "plurality") {
                    newBallot = new PluralityBallot(vote);
                } else {
                    newBallot = new STVBallot(id++, vote);
                }
                ballots.add(newBallot);
            }

            ballotFileReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error processing ballot file");
            e.printStackTrace();
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

    public static void main(String[] args) {
        Election election = new Election();
        election.promptForInput();
        election.processBallotFile();
        election.runElection();
    }

}