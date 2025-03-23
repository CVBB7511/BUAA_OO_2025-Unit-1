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
    private Poly poly = null;
    private Poly dx = null;
    
    public Gene(char var,BigInteger exp,int sign) {
        this.exp = exp;
        this.var = var;
        this.sign = sign;
    }
    
    @Override
    public Poly getPoly() {
        if (poly != null) {
            return poly;
        }
        poly = new Poly();
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
    
    public Poly getdx() {
        if (dx != null) {
            return dx;
        }
        dx = new Poly();
        // dx(sign * x^exp) = exp*sign * x^(exp-1)
        if (exp.compareTo(BigInteger.ZERO) == 0) {
            Num num = new Num("0",1);
            dx = num.getdx();
            return dx;
        }
        if (sign == 1) {
            dx.addMono(new Mono(exp,new ArrayList<>(),BigInteger.ZERO,BigInteger.ZERO));
        }
        else {
            dx.addMono(new Mono(exp.negate(),new ArrayList<>(),BigInteger.ZERO,BigInteger.ZERO));
        }
        Gene newGene = new Gene(this.var,exp.subtract(BigInteger.ONE),1);
        dx = dx.mulPoly(newGene.getPoly());
        return dx;
    }
}
