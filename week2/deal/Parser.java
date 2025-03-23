package deal;

import level.Expr;
import level.Factor;
import level.Num;
import level.Term;
import level.Trig;
import level.Func;
import level.Gene;
import java.math.BigInteger;
import java.util.ArrayList;

import static java.lang.Integer.parseInt;

public class Parser {
    private final Lexer lexer;
    private int mult; // 函数变量数
    private FDefiner definer;
    
    public Parser(Lexer lexer, int mult, FDefiner definer) {
        this.lexer = lexer;
        this.mult = mult;
        this.definer = definer;
    }
    
    public boolean issum() {
        return lexer.getToken().charAt(0) == '+';
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
    
    protected boolean isfunc() {
        return lexer.getToken().charAt(0) == 'f';
    }
    
    protected boolean issin() {
        return lexer.getToken().charAt(0) == 's';
    }
    
    protected boolean iscos() {
        return lexer.getToken().charAt(0) == 'c';
    }
    
    public Expr parseExpr() {
        Expr expr = new Expr();
        expr.addTerm(parseTerm(getsign()));
        //System.out.println("char = " + lexer.getToken());
        while (issum() || issub()) {
            if (issum()) {
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
    
    public Factor parseTrig(int sign) {
        final boolean isSin = issin();
        lexer.next();//sin( cos(
        Factor factor = parseFactor(getsign());
        //lexer.next();//factor自己会跳过
        lexer.next();//)
        BigInteger exp = BigInteger.ONE;
        if (ispow()) { // ^num
            exp = new BigInteger(lexer.getToken().substring(1));
            //System.out.println("exp="+exp);
            lexer.next();
            if (exp.compareTo(BigInteger.ZERO) == 0) {
                return new Num("1",sign);
            }
        }
        if (factor.getString().equals("0")) {
            if (isSin) {
                return new Num("0",sign);
            }
            else {
                return new Num("1",sign);
            }
        }
        Trig trig = null;
        if (isSin) {
            trig = new Trig(Trig.Type.sin,factor,exp);
        }
        else {
            trig = new Trig(Trig.Type.cos,factor,exp);
        }
        return trig;
    }
    
    public Factor parseFactor(int sign) {
        //Type 1 数字
        if (isnum()) {
            String num = lexer.getToken();
            lexer.next();
            return new Num(num,sign);
        }
        //Type 2 (表达式)^int
        else if (isleft()) {
            //一定整体为正
            lexer.next();//(
            Expr subexpr = parseExpr();
            lexer.next();//)
            if (ispow()) { // ^num
                BigInteger exp = new BigInteger(lexer.getToken().substring(1));
                subexpr.changeExp(exp);
                lexer.next();
            }
            return subexpr;
        }
        //Type 3 幂函数
        else if (isfunc()) {
            final int idx = parseInt(lexer.getToken().substring(2));
            lexer.next();//}
            lexer.next();//(
            lexer.next();// 第一个因子的左括号
            ArrayList<Factor> factors = new ArrayList<>();
            for (int i = 1; i <= mult; i++) {
                Factor factor = parseFactor(getsign());
                factors.add(factor);
                lexer.next();//, )
            }
            return new Func(idx,factors,definer);
        }
        // Type 4 sin( cos(
        else if (issin() || iscos()) {
            return parseTrig(sign);
        }
        //Type 5 幂函数
        else { // x^8 y^8
            char var = lexer.getToken().charAt(0);
            lexer.next();//x
            BigInteger exp = BigInteger.ONE;
            if (ispow()) { // ^ int
                exp = new BigInteger(lexer.getToken().substring(1));
                lexer.next();
            }
            // ^int
            return new Gene(var,exp,sign);
        }
    }
    
    protected int getsign() {
        if (lexer.getToken().charAt(0) == '-') {
            lexer.next();
            return -1;
        }
        else if (lexer.getToken().charAt(0) == '+') {
            lexer.next();
        }
        return 1;
    }
}
