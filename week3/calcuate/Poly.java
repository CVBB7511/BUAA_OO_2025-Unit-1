package calcuate;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

public class Poly {
    private ArrayList<Mono> monoList;
    
    public Poly() {
        monoList = new ArrayList<>();
    }
    
    public void removeZero() {
        Iterator<Mono> iterator = monoList.iterator();
        while (iterator.hasNext()) {
            Mono mono = iterator.next();
            if (mono.getCoef().compareTo(BigInteger.ZERO) == 0) {
                iterator.remove();
            }
        }
    }
    
    public void merge() {
        //对monoList中的元素进行合并
        boolean mark = true;
        HashSet<Mono> todeal = new HashSet<>();
        todeal.addAll(monoList);
        HashSet<Mono> dealed = new HashSet<>();
        while (mark) {
            mark = false; //本轮未产生新的合并
            for (Mono todo :todeal) {
                boolean mergemark = false;
                for (Mono done:dealed) {
                    
                    Result result = todo.merge(done);
                    if (result.getmark()) {
                        mark = true;
                        mergemark = true;
                        dealed.remove(done);
                        Mono fir = result.getfirst();
                        Mono sec = result.getsecond();
                        if (fir.getCoef().compareTo(BigInteger.ZERO) != 0) {
                            dealed.add(fir);
                        }
                        if (sec.getCoef().compareTo(BigInteger.ZERO) != 0) {
                            dealed.add(sec);
                        }
                        break;
                    }
                }
                if (!mergemark) {
                    // sin 2倍角合并
                    //System.out.println("sin 二倍角合并");
                    todo.sin2();
                    dealed.add(todo);
                }
            }
            todeal.clear();
            todeal.addAll(dealed);
            dealed.clear();
        }
        monoList.clear();
        monoList.addAll(todeal);
    }
    
    public void addMono(Mono adder) {
        //尝试蹭gcd
        if (adder.getCoef().compareTo(BigInteger.ZERO) != 0) {
            monoList.add(adder);
        }
        this.merge();
        //removeZero();
    }
    
    public void addPoly(Poly poly) {
        for (Mono mono : poly.monoList) {
            addMono(mono);
        }
        //removeZero();
    }
    
    public Poly mulMono(Mono multiplier) {
        Poly result = new Poly();
        for (Mono mono : monoList) {
            Mono newmono = mono.mulMono(multiplier);
            if (newmono.getCoef().compareTo(BigInteger.ZERO) != 0) {
                result.addMono(newmono);
            }
        }
        return result;
    }
    
    public Poly mulPoly(Poly multiplier) {
        Poly result = new Poly();
        for (Mono multiplicand : monoList) {
            result.addPoly(multiplier.mulMono(multiplicand));
        }
        return result;
    }
    
    public Poly powPoly(BigInteger exponent) {
        Poly result = new Poly();
        if (exponent.compareTo(BigInteger.ZERO) == 0) {
            Mono mono = new Mono(BigInteger.ONE,new ArrayList<>(), BigInteger.ZERO,BigInteger.ZERO);
            result.addMono(mono);
            return result;
        }
        for (BigInteger i = BigInteger.ZERO;
            i.compareTo(exponent) != 0; i = i.add(BigInteger.ONE)) {
            return this.mulPoly(this.powPoly(exponent.subtract(BigInteger.ONE)));
        }
        return this;
    }
    
    public String polyToString() {
        String mark = null;
        ArrayList<String> smonos = new ArrayList<>();
        //System.out.println("monolist.size="+monoList.size());
        for (Mono mono : monoList) {
            smonos.add(mono.monoToString(false));//默认第一个字符是‘+’ ‘-’
        }
        Collections.sort(smonos);
        for (String str : smonos) {
            if (str.charAt(0) == '+') {
                mark = str;
                break;
            }
        }
        //System.out.println("mark="+mark);
        StringBuilder sb = new StringBuilder();
        if (mark != null) { //一个小小的优化
            sb.append(mark.substring(1));
        }
        for (String str : smonos) {
            if (str != mark) {
                sb.append(str);
            }
        }
        if (sb.toString().isEmpty()) {
            return "0";
        }
        //System.out.println("str="+sb.toString());
        return sb.toString();
    }
    
    public boolean isone() {
        removeZero();
        //System.out.println("monoList.size()="+monoList.size());
        if (monoList.size() == 0) {
            return true;
        }
        if (monoList.size() != 1) {
            return false;
        }
        Mono mono = monoList.get(0);
        return mono.isone();
    }
}
