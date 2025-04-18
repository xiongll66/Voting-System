package Project2.testing.municipal;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import main.BallotFileReader;
import main.Election;
import main.InitialInput;
import main.PluralityAlgorithm;

import main.BallotFileReader;

/**
 * Unit tests for the Municipal voting algorithm in the Election system.
 * 
 * This class contains multiple test cases that simulate different election
 * scenarios using the Municipal algorithm. Each test ensures the correct
 * behavior of the Municipal algorithm when processing various voting results,
 * including ties and winners.
 */
public class MunicipalVotingTest {
    private Election election;
    private BallotFileReader ballotFileReader;
    private PluralityAlgorithm PluralityAlgorithm; 

     /**
     * Sets up the test environment by initializing the Election object and
     * the BallotFileReader before each test.
     */
    @BeforeEach
    public void setup() {
        this.election = new Election();
        ballotFileReader = new BallotFileReader();
    }

}
