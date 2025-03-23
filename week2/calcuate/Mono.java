package calcuate;

import deal.Lexer;
import deal.Parser;
import level.Expr;
import level.Factor;
import level.Trig;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import static java.lang.Integer.parseInt;

public class Mono {
    // ax^exp
    private BigInteger coef;
    private BigInteger exp;
    private BigInteger cnt;
    private ArrayList<Trig> trigs;
    private final BigInteger zero = new BigInteger("0");
    private final BigInteger one = new BigInteger("+1");
    private final BigInteger neone = new BigInteger("-1");
    private final BigInteger two = new BigInteger("2");
    private boolean mulsign;
    
    public Mono(BigInteger coef,ArrayList<Trig> trigs,BigInteger exp,BigInteger cnt) {
        this.coef = coef;
        this.trigs = trigs;
        this.exp = exp;
        this.cnt = cnt;
    }
    
    public ArrayList<Trig> getrigs() {
        return this.trigs;
    }
    
    public BigInteger getCoef() {
        return this.coef;
    }
    
    public void removeZero() {
        Iterator<Trig> iterator = trigs.iterator();
        while (iterator.hasNext()) {
            Trig trig = iterator.next();
            if (trig.getexp().compareTo(BigInteger.ZERO) == 0) {
                iterator.remove();
            }
        }
    }
    
    //判断同类项
    public boolean same(Mono mono) {
        ArrayList<Trig> trigs2 = mono.getrigs();
        removeZero();
        mono.removeZero();
        if (trigs.size() != trigs2.size()) {
            return false;
        }
        for (Trig trig: trigs) {
            boolean mark = false;
            for (Trig trig2: trigs2) {
                if (trig.sameto(trig2)) {
                    mark = true;
                    break;
                }
            }
            if (!mark) {
                return false;
            }
        }
        return true;
    }
    
    public BigInteger getmin(BigInteger x, BigInteger y) {
        if (x.compareTo(y) < 0) {
            return x;
        }
        return y;
    }
    
    public BigInteger getabsmin(BigInteger x, BigInteger y) {
        if (x.abs().compareTo(y.abs()) < 0) {
            return x;
        }
        return y;
    }
    
    public Factor getFactor2(Factor factor) {
        String newstr = "2*(" + factor.getString() + ")";
        Lexer lexer = new Lexer(newstr);
        Parser parser = new Parser(lexer,0,null);
        Expr expr = parser.parseExpr();
        Factor newfactor = expr;
        return newfactor;
    }
    
    public boolean contain(Factor fac) {
        for (Trig trig:trigs) {
            Factor factor = trig.getfactor();
            if (factor.getString().equals(fac.getString())) {
                return true;
            }
        }
        return false;
    }
    
    public void sin2() {
        String str1;
        String str2;
        boolean mark = true;
        while (mark) {
            if (coef.mod(two).compareTo(one) == 0) { return; }
            mark = false;
            for (Trig trig: trigs) {
                for (Trig trig2:trigs) {
                    str1 = trig.getfactor().getString();
                    str2 = trig2.getfactor().getString();
                    if (trig.getType().equals(trig2.getType())) {
                        continue;
                    }
                    if (!str1.equals(str2)) {
                        continue;
                    }
                    int maxn = 1;
                    while (coef.mod(two.pow(maxn)).compareTo(zero) == 0) {
                        maxn++;
                    }
                    if (maxn - 1 == 0) {
                        continue;
                    }
                    Factor newfactor = getFactor2(trig.getfactor());
                    if ((trig.getexp().compareTo(trig2.getexp()) != 0) && (!contain(newfactor))) {
                        continue;
                    }
                    BigInteger maxb = BigInteger.valueOf(maxn - 1);
                    maxb = maxb.min(trig.getexp());
                    maxb = maxb.min(trig2.getexp());
                    coef = coef.divide(two.pow(parseInt(maxb.toString())));
                    BigInteger newexp;
                    newexp = trig.getexp().subtract(maxb);
                    if (newexp.compareTo(zero) == 0) {
                        trigs.remove(trig);
                    }
                    else {
                        trig.setexp(newexp);
                    }
                    newexp = trig2.getexp().subtract(maxb);
                    if (newexp.compareTo(zero) == 0) {
                        trigs.remove(trig2);
                    }
                    else {
                        trig2.setexp(newexp);
                    }
                    
                    trigs.add(new Trig(Trig.Type.sin,newfactor,maxb));
                    mark = true;
                    break;
                }
                if (mark) {
                    break;
                }
            }
        }
    }
    
    public Result merge(Mono mono) {
        //判断两个单项式 this 和 mono 能不能合并
        if (this.exp.compareTo(mono.exp) != 0) { //次数不相等
            //System.out.println("exp ne");
            return new Result(false,this,mono);
        }
        if (this.cnt.compareTo(mono.cnt) != 0) { //次数不相等
            //System.out.println("cnt ne");
            return new Result(false,this,mono);
        }
        ArrayList<Trig> trigs2 = mono.getrigs();
        // get gcd && diff
        ArrayList<Trig> gcd = getgcd(mono);
        ArrayList<Trig> diff1 = subgcd(trigs,gcd);
        ArrayList<Trig> diff2 = subgcd(trigs2,gcd);
        int size1 = diff1.size();
        int size2 = diff2.size();
        //System.out.println("here");
        if (size1 > 1 || size2 > 1) {
            return new Result(false,this,mono);
        }
        if ((size1 == 1) && (size2 == 1)) {
            // cos^2 +sin^2 cos^2-sin^2
            return merge1(diff1,diff2,gcd,mono);
        }
        
        if ((size1 == 0) && (size2 == 0)) {
            //同类项
            //System.out.println("同类项");
            return new Result(true,new Mono(this.coef.add(mono.getCoef()),trigs,exp,cnt),
                new Mono(zero,null,zero,zero));
        }
        //System.out.println("here");
        return new Result(false,this,mono);
    }
    
    public Result merge1(ArrayList<Trig> dif1, ArrayList<Trig> dif2,
        ArrayList<Trig> gcd, Mono mono) {
        
        Factor fac1 = dif1.get(0).getfactor();
        BigInteger exp1 = dif1.get(0).getexp();
        Factor fac2 = dif2.get(0).getfactor();
        BigInteger exp2 = dif2.get(0).getexp();
        if (fac1.getString().compareTo(fac2.getString()) != 0) { //因子不同
            return new Result(false,this,mono);
        }
        if ((exp1.compareTo(new BigInteger("2")) != 0)
            || (exp2.compareTo(new BigInteger("2")) != 0)) { //不是二次
            return new Result(false,this,mono);
        }
        //不是一个sin,一个cos
        if (dif1.get(0).getType().equals(dif2.get(0).getType())) {
            return new Result(false,this,mono);
        }
        // gcd*(a*cos(fac1)^2 + b*sin(fac2)^2) -> gcd*absmin + gcd * (absmax - absmin)
        BigInteger coef1 = this.coef;
        BigInteger coef2 = mono.getCoef();
        //Type1: a*cos^2 -a*sin^2
        //System.out.println("try type1");
        if (coef1.compareTo(coef2.negate()) == 0) {
            // gcd *(coef1*diff1 +coef2*diff2) ->
            BigInteger newcoef;
            if (dif1.get(0).getType().equals("cos")) {
                newcoef = coef1;
            }
            else {
                newcoef = coef2;
            }
            ArrayList newtrig = new ArrayList<>();
            String newstr = "2*(" + fac1.getString() + ")";
            Lexer lexer = new Lexer(newstr);
            Parser parser = new Parser(lexer,0,null);
            Expr expr = parser.parseExpr();
            Factor newfactor = expr;
            deepcopy(newtrig,gcd);
            newtrig.add(new Trig(Trig.Type.cos,expr,BigInteger.ONE));
            return new Result(true,
                new Mono(newcoef,newtrig,exp,cnt.subtract(one)),new Mono(zero,null,zero,zero));
        }
        BigInteger absmin = getabsmin(coef1,coef2);
        //Type2:gcd*(a*trig(fac1)^2 + b*trig(fac2)^2) -> gcd*a + (b-a)*gcd*[diff2|trig(fac2)]
        //System.out.println("try type2");
        if (absmin.compareTo(coef1) == 0) {
            return new Result(true, new Mono(coef1,gcd,this.exp,this.cnt.subtract(two)),
                new Mono(coef2.subtract(coef1),mono.getrigs(),this.exp,this.cnt));
        }
        // gcd*(a*trig(fac1)^2 + b*trig(fac2)^2) -> gcd*b + (a-b)*gcd*[diff1|trig(fac1)]
        else {
            return new Result(true, new Mono(coef2,gcd,this.exp,this.cnt.subtract(two)),
                new Mono(coef1.subtract(coef2),this.trigs,this.exp,this.cnt));
        }
    }
    
    public ArrayList<Trig> getgcd(Mono mono) {
        ArrayList<Trig> gcd = new ArrayList<>();
        ArrayList<Trig> trigs2 = mono.getrigs();
        for (Trig trig:trigs) {
            for (Trig trig2:trigs2) {
                if (!trig.getType().equals(trig2.getType())) { // sin cos
                    continue;
                }
                Factor fac1 = trig.getfactor();
                Factor fac2 = trig2.getfactor();
                String str1 = fac1.getString();
                String str2 = fac2.getString();
                if (str1.equals(str2)) {
                    //System.out.println("--gcd");
                    BigInteger minn = getmin(trig.getexp(),trig2.getexp());
                    Trig newtrig = new Trig(trig.gettype(),fac1,minn);
                    gcd.add(newtrig);
                    break;
                }
            }
        }
        return gcd;
    }
    
    public ArrayList<Trig> subgcd(ArrayList<Trig> trigset,ArrayList<Trig> gcd) {
        ArrayList<Trig> diff = new ArrayList<>();
        for (Trig trig:trigset) {
            boolean mark = false;
            for (Trig trig2:gcd) {
                if (!trig.getType().equals(trig2.getType())) {
                    continue;
                }
                Factor fac1 = trig.getfactor();
                Factor fac2 = trig2.getfactor();
                String str1 = fac1.getString();
                String str2 = fac2.getString();
                if (str1.equals(str2)) {
                    //System.out.println("subgcd str equal");
                    BigInteger newexp = trig.getexp().subtract(trig2.getexp());
                    if (newexp.compareTo(zero) != 0) {
                        diff.add(new Trig(trig.gettype(),fac1,newexp));
                    }
                    mark = true;
                    break;
                }
            }
            if (!mark) {
                diff.add(new Trig(trig.gettype(),trig.getfactor(),trig.getexp()));
            }
        }
        return diff;
    }
    
    public boolean addMono(Mono mono) {
        if (mono.exp.compareTo(this.exp) != 0) { //无法合并
            return false;
        }
        else if (mono.cnt.compareTo(this.cnt) != 0) {
            return false;
        }
        else if (this.same(mono)) {
            coef = coef.add(mono.coef);
            return true;
        }
        return false;
    }
    
    public void deepcopy(ArrayList<Trig> trigs1,ArrayList<Trig> trigs2) {
        for (Trig trig: trigs2) {
            trigs1.add(trig.clone());
        }
    }
    
    public Mono mulMono(Mono mono) {
        BigInteger resultcoef = coef.multiply(mono.coef);
        BigInteger resultexp = exp.add(mono.exp);
        BigInteger resultcnt = cnt.add(mono.cnt);
        
        ArrayList<Trig> trigs2 = mono.getrigs();
        ArrayList<Trig> resultList = new ArrayList<>();
        /*bug*/
        deepcopy(resultList,trigs);
        for (Trig trig2: trigs2) {
            boolean mark = false;
            for (Trig trig : resultList) {
                if (trig2.similarto(trig).compareTo(neone) != 0) {
                    //sin (factor)^exp1 factor相同
                    mark = true;
                    trig.setexp(trig2.getexp().add(trig.getexp()));
                    break;
                }
            }
            if (!mark) {
                //未能合并
                resultList.add(trig2);
            }
        }
        return new Mono(resultcoef,resultList,resultexp,resultcnt);
    }
    
    public String monoToString(boolean start) {
        StringBuilder now = new StringBuilder();
        //符号
        mulsign = false;
        if ((!start) && coef.compareTo(zero) > 0) {
            now.append("+");
        }
        else if (coef.compareTo(zero) < 0) {
            now.append("-");
        }
        // +-1
        if ((coef.compareTo(one) == 0) || (coef.compareTo(neone) == 0)) {
            if ((trigs.size() == 0) && (exp.compareTo(zero) == 0)) {
                now.append("1");
                mulsign = true;
            }
        }
        // 设0不存在 其他值
        else {
            if (coef.compareTo(zero) < 0) {
                now.append(coef.negate().toString());
                mulsign = true;
            }
            else {
                now.append(coef.toString());
                mulsign = true;
            }
        }
        //∏
        trigsToString(now);
        //x^exp
        if (exp.compareTo(zero) != 0) {
            if (mulsign) {
                now.append("*");
            }
            else {
                mulsign = false;
            }
            now.append("x");
            if (exp.compareTo(one) != 0) {
                now.append("^" + exp.toString());
            }
        }
        return now.toString();
    }
    
    public void trigsToString(StringBuilder now) {
        ArrayList<String> strigs = new ArrayList<>();
        removeZero();
        for (Trig trig:trigs) {
            strigs.add(trig.toString());
        }
        //System.out.println("strigs.size="+strigs.size());
        Collections.sort(strigs);
        for (String str: strigs) {
            if (mulsign) {
                now.append("*" + str);
            }
            else {
                mulsign = true;
                now.append(str);
            }
        }
    }
    
    public boolean isone() {
        if ((exp.compareTo(BigInteger.ZERO) == 0) && (cnt.compareTo(BigInteger.ZERO) == 0)) {
            return true;
        }
        if ((coef.compareTo(BigInteger.ONE) == 0) && (cnt.compareTo(BigInteger.ZERO) == 0)) {
            return true;
        }
        if ((trigs.size() == 1) && (exp.compareTo(BigInteger.ZERO) == 0)
            && (coef.compareTo(BigInteger.ONE) == 0)) {
            return true;
        }
        return false;
    }
    
}