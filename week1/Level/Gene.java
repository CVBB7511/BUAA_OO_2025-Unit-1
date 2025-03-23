package Level;

import calcuate.Poly;

import java.math.BigInteger;

public class Gene implements Factor{
    private char var;
    private int exp;
    private int sign;
    
    public Gene (char var,int exp,int sign) {
        this.exp = exp;
        this.var = var;
        this.sign = sign;
    }
    
    @Override
    public Poly getPoly() {
        Poly poly = new Poly();
        poly.addMono(new BigInteger(String.valueOf(sign)),exp);
        return poly;
    }
}
