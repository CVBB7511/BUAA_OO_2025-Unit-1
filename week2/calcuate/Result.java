package calcuate;

public class Result {
    private boolean canMerge;
    private Mono first;
    private Mono second;
    
    public Result(boolean canMerge, Mono first, Mono second) {
        this.canMerge = canMerge;
        this.first = first;
        this.second = second;
    }
    
    public boolean getmark() {
        return this.canMerge;
    }
    
    public Mono getfirst() {
        return this.first;
    }
    
    public Mono getsecond() {
        return this.second;
    }
}
