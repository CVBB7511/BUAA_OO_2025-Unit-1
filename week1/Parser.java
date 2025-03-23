import Level.*;

import java.math.BigInteger;

import static java.lang.Integer.parseInt;

public class Parser {
    private final Lexer lexer;
    
    public Parser (Lexer lexer) {
        this.lexer = lexer;
    }
    
    public boolean issum() {
        return lexer.getToken().charAt(0) == '+' ;
    }
    
    public boolean issub() {
        return lexer.getToken().charAt(0) == '-';
    }
    
    public boolean ismul() {
        return lexer.getToken().charAt(0) == '*';
    }
    
    protected boolean isnum() {
        char c = lexer.getToken().charAt(0);
        return ('0' <= c) && (c <= '9');
    }
    
    protected boolean isleft() {
        return lexer.getToken().charAt(0) == '(';
    }
    
    protected boolean ispow() {
        return lexer.getToken().charAt(0) == '^';
    }
    
    public Expr parseExpr() {
        Expr expr = new Expr();
        expr.addTerm(parseTerm(getsign()));
        while(issum() || issub()) {
            if (issum()){
                lexer.next();
                expr.addTerm(parseTerm(1));
            }
            else {
                lexer.next();
                expr.addTerm(parseTerm(-1));
            }
        }
        return expr;
    }
    
    public Term parseTerm(int sign) {
        Term term = new Term(sign);
        term.addFactor(parseFactor(getsign()));
        while (ismul()) {
            lexer.next();
            term.addFactor(parseFactor(getsign()));
        }
        return term;
    }
    
    public Factor parseFactor(int sign){
        //Type 1 数字
        if (isnum()) {
            String num = lexer.getToken();
            lexer.next();
            return new Num(num,sign);
        }
        //Type 2 (表达式)^int
        else if (isleft()){
            //一定整体为正
            lexer.next();//(
            Expr subexpr = parseExpr();
            lexer.next();//)
            if (ispow()) { // ^num
                int exp = parseInt(lexer.getToken().substring(1));
                subexpr.changeExp(exp);
                lexer.next();
            }
            return subexpr;
        }
        //Type 3 幂函数
        else { // x^8
            char var = lexer.getToken().charAt(0);
            lexer.next();//x
            int exp = 1;
            if (ispow()) {// ^ int
                exp = parseInt(lexer.getToken().substring(1));
                lexer.next();
            }
           // ^int
            return new Gene(var,exp,sign);
        }
    }
    
    protected int getsign() {
        if (lexer.getToken().charAt(0) == '-'){
            lexer.next();
            return -1;
        }
        else if (lexer.getToken().charAt(0) == '+') {
            lexer.next();
        }
        return 1;
    }
}
