package main;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MvAlgorithm extends VotingAlgorithm {

    public Election election;



    public MvAlgorithm(Election election) {
        
        this.election = election; 
        
    }

    public void runAlgorithm(List<Ballot> ballots) {
        
        MvAlgorithmFunction(ballots);
        calculateWinner(); 
        displayResults();
    }

    private void MvAlgorithmFunction(List<Ballot> ballots) {

    }

    private void calculateWinner() {

    }

    protected List<Integer> breakTie(List<Integer> tieList) {

    }


    protected void displayResults() {

    }

}