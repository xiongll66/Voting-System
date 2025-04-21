package Project2.testing;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import main.BallotFileReader;
import main.Header;
import main.PluralityBallot;
import main.STVBallot;
import main.Ballot;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BallotFileReaderTest {
    private BallotFileReader ballotFileReader;

    @BeforeEach
    public void setup() {
        this.ballotFileReader = new BallotFileReader();
    }

    @Test
    public void readBallotsPluralityTest() {
        List<Ballot> expectedBallots = new ArrayList<>();
        int[] vote = {1, 0, 0};
        int[] vote2 = {0, 1, 0};
        Ballot b1 = new PluralityBallot(vote);
        Ballot b2 = new PluralityBallot(vote);
        Ballot b3 = new PluralityBallot(vote2);
        Ballot b4 = new PluralityBallot(vote2);

        expectedBallots.add(b1);
        expectedBallots.add(b2);
        expectedBallots.add(b3);
        expectedBallots.add(b4);

        try {
            List<Ballot> actualBallots = ballotFileReader.readBallots("Project1/testing/plurality/2tie1seat.csv", "plurality");
            assertIterableEquals(expectedBallots, actualBallots);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void readBallotsSTVTest() {
        List<Ballot> expectedBallots = new ArrayList<>();
        int[] vote = {1, 2, 3};
        int[] vote2 = {3, 1, 2};
        int[] vote3 = {2, 3, 1};
        Ballot b1 = new STVBallot(0, vote);
        Ballot b2 = new STVBallot(1, vote);
        Ballot b3 = new STVBallot(2, vote2);
        Ballot b4 = new STVBallot(3, vote2);
        Ballot b5 = new STVBallot(4, vote3);

        expectedBallots.add(b1);
        expectedBallots.add(b2);
        expectedBallots.add(b3);
        expectedBallots.add(b4);
        expectedBallots.add(b5);

        try {
            List<Ballot> actualBallots = ballotFileReader.readBallots("Project1/testing/stv/elimination.csv", "stv");
            assertIterableEquals(expectedBallots, actualBallots);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void readHeaderTest() throws Exception{
        Path csvPath = Paths.get("Project2/testing/plurality/3tie2seat2.csv");
        assertTrue(Files.exists(csvPath), "File not found: " + csvPath);

        BallotFileReader reader = new BallotFileReader();
        Header header = reader.readHeader(csvPath.toString());
        
        assertEquals("PV", header.getElectionType());
        assertEquals(2, header.getNumSeats());
        assertArrayEquals(new String[]{"A", "B", "C", "D"}, header.getCandidates());
    }
}
