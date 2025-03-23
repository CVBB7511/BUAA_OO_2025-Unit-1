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
    private Poly poly = null;
    private Poly dx = null;
    
    public Term(int sign) {
        this.factors = new ArrayList<>();
        this.sign = sign;
    }
    
    public void addFactor(Factor factor) {
        factors.add(factor);
    }
    
    public Poly getPoly() {
        if (poly != null) {
            return poly;
        }
        poly = new Poly();
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
    
    public Poly getdx() {
        if (dx != null) {
            return dx;
        }
        dx = new Poly();
        for (Factor factor : factors) {
            Poly now = new Poly();
            now.addPoly(factor.getdx());
            for (Factor other : factors) {
                if (other == factor) {
                    continue;
                }
                now = now.mulPoly(other.getPoly());
            }
            //factor.getPoly().check();
            dx.addPoly(now);
        }
        if (sign == 1) {
            dx = dx.mulMono(new Mono(one,new ArrayList<>(),BigInteger.ZERO,BigInteger.ZERO));
        }
        else {
            dx = dx.mulMono(new Mono(neone,new ArrayList<>(),BigInteger.ZERO,BigInteger.ZERO));
        }
        //poly.check();
        return dx;
    }
}
