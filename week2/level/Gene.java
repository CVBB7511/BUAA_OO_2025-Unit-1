package level;

import calcuate.Mono;
import calcuate.Poly;

import java.math.BigInteger;
import java.util.ArrayList;

public class Gene implements Factor {
    private char var;
    private BigInteger exp;
    private int sign;
    private String finalStr = null;
    
    public Gene(char var,BigInteger exp,int sign) {
        this.exp = exp;
        this.var = var;
        this.sign = sign;
    }
    
    @Override
    public Poly getPoly() {
        Poly poly = new Poly();
        BigInteger res = new BigInteger(String.valueOf(sign));
        Mono mono = new Mono(res, new ArrayList<>(),exp,BigInteger.ZERO);
        poly.addMono(mono);
        return poly;
    }
    
    @Override
    public String getString() {
        if (finalStr == null) {
            finalStr = this.getPoly().polyToString();
        }
        return finalStr;
    }
    
}
