package Level;
import java.math.BigInteger;
import java.util.ArrayList;

import calcuate.Mono;
import calcuate.Poly;
public class Term {
    private ArrayList<Factor> factors;
    private int sign;
    private BigInteger one = new BigInteger("1");
    private BigInteger zero = new BigInteger("0");
    private BigInteger neone = new BigInteger("-1");
    public Term(int sign) {
        this.factors = new ArrayList<>();
        this.sign = sign;
    }
    
    public void addFactor(Factor factor) {
        factors.add(factor);
    }
    
    public Poly getPoly() {
        Poly poly = new Poly();
        poly.check();
        if (sign == 1) {
            poly.addMono(new Mono(one,0));
        }
        else {
            poly.addMono(new Mono(neone,0));
        }
        poly.check();
        for (Factor factor : factors) {
            factor.getPoly().check();
            poly = poly.mulPoly(factor.getPoly());//多项式相乘
        }
        poly.check();
        return poly;
    }
}
