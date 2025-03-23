package level;

import calcuate.Poly;
import java.math.BigInteger;
import java.util.ArrayList;

public class Expr implements Factor {
    private ArrayList<Term> terms;
    private BigInteger exp;
    private String finalStr = null;
    
    public Expr() {
        terms = new ArrayList<>();
        exp = BigInteger.ONE;
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
        Poly poly = new Poly();
        for (Term term : terms) {
            poly.addPoly(term.getPoly());
        }
        //System.out.println("exp in Expr"+exp);
        return poly.powPoly(exp);
    }
    
    @Override
    public String getString() {
        if (finalStr == null) {
            finalStr = this.getPoly().polyToString();
        }
        return finalStr;
    }
}
