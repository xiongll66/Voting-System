package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BallotFileReader {
    public String[] readCandidates(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        try (Scanner scanner = new Scanner(file)) {
            return scanner.nextLine().split(",");
        }
    }

    public List<Ballot> readBallots(String fileName, String algorithmType) throws FileNotFoundException {
        List<Ballot> ballots = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(fileName))) {
            scanner.nextLine();
            int id = 0;
            while (scanner.hasNextLine()) {
                String[] ballotLine = scanner.nextLine().split(",");
                int[] vote = new int[ballotLine.length];
                for (int i = 0; i < ballotLine.length; i++) {
                    vote[i] = Integer.parseInt(ballotLine[i]);
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
