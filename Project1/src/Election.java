

import java.util.List;

class Election{
    private List<Ballots> ballots;
    private String[] candidates;
    private InitialInput input;
    private VotingAlgorithm votingAlgorithm;

    public Election(List<Ballots> ballots, String[] candidates,InitialInput input, VotingAlgorithm votingAlgorithm){
        this.ballots = ballots;
        this.candidates = candidates;
        this.input = input;
        this.votingAlgorithm = votingAlgorithm;
    }

    public void promptForInput(){

    }

    public void processBallotFile(){

    }

    public void runElection(){

    }

    public InitialInput getInput(){

    }

    public String[] getCandidates(){

    }

    publis List<Ballots> getBallots(){

    }
}