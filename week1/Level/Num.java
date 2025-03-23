package Level;

import calcuate.Mono;
import calcuate.Poly;

import java.math.BigInteger;

public class Num implements Factor{
    BigInteger value;
    public Num (String str,int sign) {
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
        poly.addMono(new Mono(this.value,0));
        return poly;
    }
}
