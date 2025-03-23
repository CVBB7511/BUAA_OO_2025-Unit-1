package level;

import java.math.BigInteger;
import java.util.ArrayList;
import calcuate.Poly;

import calcuate.Mono;

public class Term {
    private ArrayList<Factor> factors;
    private int sign;
    private final BigInteger one = new BigInteger("1");
    private final BigInteger zero = new BigInteger("0");
    private final BigInteger neone = new BigInteger("-1");
    
    public Term(int sign) {
        this.factors = new ArrayList<>();
        this.sign = sign;
    }
    
    public void addFactor(Factor factor) {
        factors.add(factor);
    }
    
    public Poly getPoly() {
        Poly poly = new Poly();
        //poly.check();
        if (sign == 1) {
            poly.addMono(new Mono(one,new ArrayList<>(),BigInteger.ZERO,BigInteger.ZERO));
        }
        else {
            poly.addMono(new Mono(neone,new ArrayList<>(),BigInteger.ZERO,BigInteger.ZERO));
        }
        //poly.check();
        for (Factor factor : factors) {
            //factor.getPoly().check();
            poly = poly.mulPoly(factor.getPoly());//多项式相乘
        }
        //poly.check();
        return poly;
    }
}
