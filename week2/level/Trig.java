package level;

import calcuate.Mono;
import calcuate.Poly;
import java.math.BigInteger;
import java.util.ArrayList;

public class Trig implements Factor {
    public enum Type {
        sin,cos
    }
    
    private Factor factor;
    private BigInteger exp = BigInteger.ONE;
    private Type type;
    private BigInteger neone = new BigInteger("-1");
    private String finalStr = null;
    
    public Trig(Type type, Factor factor, BigInteger exp) {
        this.type = type;
        this.factor = factor;
        this.exp = exp;
    }
    
    public Factor getfactor() {
        return this.factor;
    }
    
    public BigInteger getexp() {
        return this.exp;
    }
    
    public String getType() {
        if (this.type == Type.sin) {
            return "sin";
        }
        return "cos";
    }
    
    public Type gettype() {
        return this.type;
    }
    
    //忽略常数 判断同类项
    public boolean sameto(Trig trig) {
        if (this.type != trig.type) {
            return false;
        }
        if (this.exp.compareTo(this.exp) != 0) {
            return false;
        }
        String str1 = this.factor.getString();
        String str2 = trig.getfactor().getString();
        return str1.equals(str2);
    }
    
    public BigInteger min(BigInteger x, BigInteger y) {
        if (x.compareTo(y) < 0) {
            return x;
        }
        return y;
    }
    
    //提取公因式，返回公共次数
    public BigInteger similarto(Trig trig) {
        if (this.type != trig.type) {
            return neone;
        }
        String str1 = this.factor.getString();
        String str2 = trig.getfactor().getString();
        if (!str1.equals(str2)) {
            return neone;
        }
        return min(this.exp,trig.getexp());
    }
    
    public void setexp(BigInteger exp) {
        this.exp = exp;
        finalStr = null;
    }
    
    public Trig clone() {
        //仅能修改exp，而不产生其它影响
        Trig res = new Trig(this.type,this.factor,this.exp);
        return res;
    }
    
    @Override
    public Poly getPoly() {
        ArrayList<Trig> list = new ArrayList<>();
        list.add(this);
        Mono mono = new Mono(BigInteger.ONE,list,BigInteger.ZERO,exp);
        Poly poly = new Poly();
        poly.addMono(mono);
        return poly;
    }
    
    public String toString() {
        StringBuilder strb = new StringBuilder();
        Factor factor = this.getfactor();
        Poly poly = factor.getPoly();
        if (factor.getString().equals("0")) {
            if (this.getType().equals("sin")) {
                //sin(0) = 0
                return "0";
            }
            else {
                //cos(0) = 1
                return "1";
            }
        }
        if (!poly.isone()) {
            strb.append(this.getType() +
                "((" + poly.polyToString() + "))");
        }
        else {
            strb.append(this.getType() +
                "(" + poly.polyToString() + ")");
        }
        if (this.getexp().compareTo(BigInteger.ONE) != 0) {
            strb.append("^" + this.getexp().toString());
        }
        return strb.toString();
    }
    
    @Override
    public String getString() {
        if (finalStr == null) {
            finalStr = this.getPoly().polyToString();
        }
        return finalStr;
    }
}
