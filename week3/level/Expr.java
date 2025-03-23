package level;

import calcuate.Mono;
import calcuate.Poly;
import java.math.BigInteger;
import java.util.ArrayList;

public class Expr implements Factor {
    private ArrayList<Term> terms;
    private BigInteger exp;
    private String finalStr = null;
    private Poly poly = null;
    private Poly dx = null;
    
    public Expr() {
        terms = new ArrayList<>();
        exp = BigInteger.ONE;
    }
    
    public Expr(ArrayList<Term> terms,BigInteger exp) {
        this.terms = terms;
        this.exp = exp;
    }
    
    public void addTerm(Term term) {
        terms.add(term);
        finalStr = null;
    }
    
    public void changeExp(BigInteger exp) {
        this.exp = exp;
        finalStr = null;
    }
    
    @Override
    public Poly getPoly() {
        if (poly != null) {
            return poly;
        }
        poly = new Poly();
        for (Term term : terms) {
            poly.addPoly(term.getPoly());
        }
        //System.out.println("exp in Expr"+exp);
        poly = poly.powPoly(exp);
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
        // dx((term1+term2)^exp) = exp * (term1+term2)^(exp-1) * dx (term1+term2)
        if (exp.compareTo(BigInteger.ZERO) == 0) {
            dx.addMono(new Mono(BigInteger.ZERO,new ArrayList<>(),BigInteger.ZERO,BigInteger.ZERO));
            return dx;
        }
        dx.addMono(new Mono(exp,new ArrayList<>(),BigInteger.ZERO,BigInteger.ZERO));
        Expr newexpr = new Expr(terms,exp.subtract(BigInteger.ONE));
        dx = dx.mulPoly(newexpr.getPoly());
        Poly dxterm = new Poly();
        for (Term term : terms) {
            //System.out.println("term:"+term.getPoly().polyToString());
            dxterm.addPoly(term.getdx());
        }
        dx = dx.mulPoly(dxterm);
        return dx;
    }
}
