package level;

import calcuate.Mono;
import calcuate.Poly;

import java.math.BigInteger;
import java.util.ArrayList;

public class Num implements Factor {
    private BigInteger value;
    private String finalStr = null;
    
    public Num(String str,int sign) {
        this.value = new BigInteger(str);
        //System.out.println("Num_val_str:"+str);
        if (sign == -1) {
            this.value = this.value.negate();
        }
    }
    
    @Override
    public Poly getPoly() {
        Poly poly = new Poly();
        //System.out.println("Num_val:"+this.value);
        poly.addMono(new Mono(this.value,new ArrayList<>(), BigInteger.ZERO, BigInteger.ZERO));
        return poly;
    }
    
    @Override
    public String getString() {
        if (finalStr == null) {
            finalStr = this.getPoly().polyToString();
        }
        return finalStr;
    }
}
