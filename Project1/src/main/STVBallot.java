package main;
public class STVBallot extends Ballot {
    private int id;
    private int preference;

    public STVBallot(int id, int[] vote) {
        super(vote);
        this.id = id;
        this.preference = 1;
    }

    public void incrementPreference() {
        this.preference++;
    }

    public int getId() {
        return id;
    }
    
    public int getPreference() {
        return preference;
    }
}