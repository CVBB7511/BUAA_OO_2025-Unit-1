package calcuate;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class Poly {
    private HashMap<Integer,BigInteger> hashmap;
    private final BigInteger zero = new BigInteger("0");
    
    public Poly(){
        this.hashmap = new HashMap<>();
    }
    
    public void setHashmap(HashMap<Integer,BigInteger> hashmap) {
        this.hashmap = hashmap;
    }
    
    public HashMap<Integer,BigInteger> getHashmap() {
        return this.hashmap;
    }
    
    public void check() {
        for (Map.Entry<Integer, BigInteger> entry : hashmap.entrySet()) {
            int exp = entry.getKey();
            BigInteger a = entry.getValue();
            //System.out.println("hashmap:"+a+"* x^"+exp);
        }
    }
    public void addMono(Mono mono) {
        int exp = mono.getExp();
        BigInteger a = mono.geta();
        if (hashmap.containsKey(exp)) {
            a = a.add(hashmap.get(exp));
        }
        if (a.compareTo(zero) == 0) {
            hashmap.remove(exp);
        }
        else{
            hashmap.put(exp,a);
        }
    }
    
    public void addMono(BigInteger a,int exp) {
        if (hashmap.containsKey(exp)) {
            a = a.add(hashmap.get(exp));
        }
        if (a.compareTo(zero) == 0) {
            hashmap.remove(exp);
        }
        else{
            hashmap.put(exp,a);
            //System.out.println("a in addMono="+a);
        }
    }
    
    public void addPoly(Poly poly) {
        HashMap<Integer,BigInteger> polyMap = poly.getHashmap();
        for (Map.Entry<Integer, BigInteger> entry : polyMap.entrySet()) {
            int exp = entry.getKey();
            BigInteger a = entry.getValue();
            this.addMono(a,exp);
        }
    }
    
    public Poly mulMono(Mono mono) {
        BigInteger a1 = mono.geta();
        int exp1 = mono.getExp();
        HashMap<Integer,BigInteger> temp = new HashMap<>();
        for (Map.Entry<Integer, BigInteger> entry : hashmap.entrySet()) {
            int exp2 = entry.getKey();
            BigInteger a2 = entry.getValue();
            int exp = exp1 + exp2;
            BigInteger a = a1.multiply(a2);
            if (temp.containsKey(exp)) {
                a = a.add(hashmap.get(exp));
            }
            if (a.compareTo(zero) == 0) {
                temp.remove(exp);
            }
            else{
                temp.put(exp,a);
               // System.out.println("a="+a);
            }
        }
        Poly result = new Poly();
        result.setHashmap(temp);
        return result;
    }
    
    public Poly mulPoly(Poly poly) {
        HashMap<Integer,BigInteger> polyMap = poly.getHashmap();
        Poly result = new Poly();
        for (Map.Entry<Integer, BigInteger> entry : polyMap.entrySet()) {
            int exp = entry.getKey();
            BigInteger a = entry.getValue();
            Mono mono = new Mono(a,exp);
            result.addPoly(mulMono(mono));
        }
        return result;
    }
    
    public Poly powPoly(int exp) {
        Poly result = new Poly();
        HashMap<Integer,BigInteger> temp = new HashMap<>();
        //System.out.println("exp in pow:"+exp);
        if (exp == 0) {
            result.addMono(BigInteger.ONE,0);
            return result;
        }
        for (int i = 2; i <= exp; i++) {
            return this.mulPoly(this.powPoly(exp-1));
        }
        return this;
    }
    
    public String PolytoString() {
        StringBuilder strb = new StringBuilder();
        boolean mark = false; //是否有正项
        boolean start = true;
        for (Map.Entry<Integer, BigInteger> entry : hashmap.entrySet()) {
            int exp = entry.getKey();
            BigInteger a = entry.getValue();
            if (a.compareTo(BigInteger.ZERO) > 0){
                Mono mono = new Mono(a,exp);
                strb.append(mono.monotoString(true));
                start = false;
                break;
            }
        }
        for (Map.Entry<Integer, BigInteger> entry : hashmap.entrySet()) {
            int exp = entry.getKey();
            BigInteger a = entry.getValue();
            if (a.compareTo(BigInteger.ZERO) > 0){
                if(!mark) {
                    mark = true;
                    continue;
                }
            }
            Mono mono = new Mono(a,exp);
            String s = mono.monotoString(start);
            if (s != null) {
                strb.append(s);
                start = false;//不是首项
            }
        }
        String str = strb.toString();
        if (str.isEmpty()) {
            return "0";
        }
        return str;
    }
}
