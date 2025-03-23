package Level;

import calcuate.Poly;

import java.math.BigInteger;
import java.util.ArrayList;

public class Expr implements Factor{
    private ArrayList<Term> terms;
    private int exp;
    
    public Expr() {
        terms = new ArrayList<>();
        exp = 1;
    }
    
    public void addTerm(Term term){
        terms.add(term);
    }
    
    public void changeExp(int exp) {
        this.exp = exp;
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
}
