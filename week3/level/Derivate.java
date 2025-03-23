package level;

import calcuate.Poly;
import deal.FDefiner;
import deal.Lexer;

public class Derivate implements Factor {
    private Expr expr;
    private String finalStr = null;
    private FDefiner definer;
    private Poly dx;
    
    public Derivate(Expr expr, FDefiner definer) {
        this.expr = expr;
        this.definer = definer;
    }
    
    public Poly getPoly() {
        String str = expr.getString();
        //System.out.println("--dx"+str);
        deal.Lexer lexer = new deal.Lexer(str);
        deal.Parser parser = new deal.Parser(lexer, definer);
        Expr newexpr = parser.parseExpr();
        return newexpr.getdx();
    }
    
    public String getString() {
        if (finalStr != null) {
            return finalStr;
        }
        finalStr = this.getPoly().polyToString();
        return finalStr;
    }
    
    public Poly getdx() {
        if (dx != null) {
            return dx;
        }
        String str = getString();
        str = deal.Prework.prework(str);
        Lexer lexer = new Lexer(str);
        deal.Parser parser = new deal.Parser(lexer, definer);
        expr = parser.parseExpr();
        dx = expr.getPoly();
        //System.out.println("String="+poly.polyToString());
        return dx;
    }
}
