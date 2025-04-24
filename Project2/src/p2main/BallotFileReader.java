package p2main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import p2main.Ballot;
import p2main.PluralityBallot;
import p2main.STVBallot;

/**
 * Class for reading and processing ballot data from ballot file.
 */
public class BallotFileReader {

    /**
     * Reads the header of the ballot file
     * @param fileName the name of the ballot file
     * @return Header object containing election information
     * @throws FileNotFoundException
     */
    public Header readHeader(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        String electionType;
        int numSeats;
        String[] candidates;
        try (Scanner scanner = new Scanner(file)) {
            electionType = scanner.nextLine();
            numSeats = Integer.parseInt(scanner.nextLine());
            scanner.nextLine(); // skip number of candidates line in header (not useful)
            scanner.nextLine(); // skip number of ballots line in header (not useful)
            candidates = scanner.nextLine().split(",");

            Header header = new Header(electionType, numSeats, candidates);
            return header;
        }
    }

    /**
     * Reads ballot from given ballot file. 
     * 
     * @param fileName The name of the ballot file to read ballots from
     * @param algorithmType The algorithm type for the election (plurality or stv)
     * @return A list of Ballot objects
     * @throws FileNotFoundException If given file cannot be found
     */
    public List<Ballot> readBallots(String fileName, String algorithmType) throws FileNotFoundException {
        List<Ballot> ballots = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(fileName))) {
            // skip header lines (5 lines)
            for (int i = 0; i < 5; i++) {
                scanner.nextLine();
            }
            int id = 0;
            while (scanner.hasNextLine()) {
                String[] ballotLine = scanner.nextLine().split(",", -1);
                int[] vote = new int[ballotLine.length];
                for (int i = 0; i < ballotLine.length; i++) {
                    try {
                        vote[i] = Integer.parseInt(ballotLine[i]);
                    } catch (NumberFormatException e) {
                        vote[i] = 0;
                    }
                }
                if (algorithmType.equals("PV") || algorithmType.equals("MV")) {
                    ballots.add(new PluralityBallot(vote));
                } else if (algorithmType.equals("STV")) {
                    ballots.add(new STVBallot(id++, vote));
                }
            }
        }
        return ballots;
    }

}
