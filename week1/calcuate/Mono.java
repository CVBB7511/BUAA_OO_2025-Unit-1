package calcuate;

import java.math.BigInteger;

public class Mono {
    // ax^exp
    private BigInteger a;
    private int exp;
    BigInteger zero = new BigInteger("0");
    BigInteger one = new BigInteger("+1");
    BigInteger neone = new BigInteger("-1");
    
    public Mono(BigInteger a,int exp) {
        this.a = a;
        this.exp = exp;
    }
    
    public void addMono(Mono mono) {
        a = a.add(mono.a);
    }
    
    public void mulMono(Mono mono) {
        a = a.multiply(mono.a);
        exp = exp + mono.exp;
    }
    
    public int getExp() {
        return this.exp;
    }
    
    public BigInteger geta() {
        return this.a;
    }
    
    public String monotoString(boolean start) {
        StringBuilder now = new StringBuilder();
        
        if ((!start)&&a.compareTo(zero) > 0) {
            now.append("+");
        }
        
        if (exp != 0) {
            if (a.compareTo(one) == 0) { // + 1 * x
            }
            else if (a.compareTo(neone) == 0) { // - 1 * x
                now.append("-");
            }
            else {
                now.append(a.toString());// +/- a
                now.append("*");
            }
            now.append("x");
            if (exp != 1) {
                now.append("^");
                now.append(exp);//ax^b
            }
        }
        else {
            now.append(a.toString());// +/- a
        }
        return now.toString();
    }
}