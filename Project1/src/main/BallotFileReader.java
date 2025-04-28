package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import main.Ballot;
import main.PluralityBallot;
import main.STVBallot;

/**
 * Class for reading and processing ballot data from ballot file.
 */
public class BallotFileReader {

    /**
     * Reads candidates from ballot file.
     * 
     * @param fileName The name of the ballot file to read candidates from
     * @return String array of candidate names
     * @throws FileNotFoundException If given file cannot be found
     */
    public String[] readCandidates(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        try (Scanner scanner = new Scanner(file)) {
            return scanner.nextLine().split(",");
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
            scanner.nextLine();
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
                if (algorithmType.equals("plurality")) {
                    ballots.add(new PluralityBallot(vote));
                } else {
                    ballots.add(new STVBallot(id++, vote));
                }
            }
        }
        return ballots;
    }
}
