package deal;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class FDefiner {
    private ArrayList<String> vars = new ArrayList<>();
    private HashMap<Integer,String> fn = new HashMap<>();
    private int pos = 0;
    private ArrayList<BigInteger> num = new ArrayList<>();
    private ArrayList<String> first = new ArrayList<>(); // no func_Factor
    private ArrayList<String> second = new ArrayList<>();
    private String expr = null;
    private BigInteger ten = new BigInteger("10");
    private char sign;
    
    public FDefiner(Scanner scanner) {
        int len = 0;
        for (int i = 1; i <= 3; i++) {
            if (i == 1) {
                scanner.nextLine();
            }
            String str = scanner.nextLine();
            //System.out.println("str1="+str);
            str = Prework.prework(str);
            //System.out.println("str2="+str);
            len = str.length();
            if ((str.charAt(2) == '0') || (str.charAt(2) == '1')) {
                if (vars.isEmpty()) {
                    this.getvar(str);
                }
                pos = str.indexOf("=");
                fn.put(str.charAt(2) - '0',simplify("(" + str.substring(pos + 1) + ")"));
            }
            else {
                pos = str.indexOf("=") + 1;
                //System.out.println("+++pos="+pos);
                //System.out.println("first_num="+str.charAt(pos));
                getnum(str);// pos @ *
                xingcan(str);
                sign = str.charAt(pos);
                pos++; // + -
                getnum(str);
                xingcan(str);
                if (pos < len) {
                    //System.out.println("pos="+pos);
                    //System.out.println("len="+len);
                    // +- start
                    //System.out.println("pos=" + pos+"len="+len);
                    expr = Prework.prework(str.substring(pos));
                }
            }
        }
        ditui();
        //check();
    }
    
    public void check() {
        //System.out.println(vars.get(0));
        //System.out.println(vars.get(1));
        //System.out.println(first.get(0));
        //System.out.println(first.get(1));
        //System.out.println(second.get(0));
        // System.out.println(second.get(1));
        for (int i = 0; i <= 5;i++) {
            System.out.println(fn.get(i));
        }
    }
    
    public String getfn(int index) {
        return fn.get(index);
    }
    
    public int getcnt() {
        return vars.size();
    }
    
    public String getvar(int index) {
        return vars.get(index);
    }
    
    public void getvar(String str) {
        pos = 5;
        while (str.charAt(pos) != ')') {
            StringBuilder strb = new StringBuilder();
            while ((str.charAt(pos) != ',') && (str.charAt(pos) != ')')) {
                strb.append(str.charAt(pos));
                pos++;
            }
            if (str.charAt(pos) == ',') {
                pos++;
            }
            vars.add(strb.toString());
        }
    }
    
    public void ditui() {
        String str1;
        String str2;
        StringBuilder strb = new StringBuilder();
        String strn;
        for (int i = 2;i <= 5;i++) {
            str1 = fn.get(i - 1);
            str2 = fn.get(i - 2);
            if (vars.size() > 1) {
                str1 = str1.replaceAll(vars.get(1),"t");
                str2 = str2.replaceAll(vars.get(1),"t");
            }
            strb = new StringBuilder();
            str1 = str1.replaceAll(vars.get(0),"(" + first.get(0) + ")");
            str2 = str2.replaceAll(vars.get(0),"(" + first.get(1) + ")");
            if (vars.size() > 1) {
                str1 = str1.replaceAll("t","(" + second.get(0) + ")");
                str2 = str2.replaceAll("t","(" + second.get(1) + ")");
            }
            strb.append("(");
            strb.append(num.get(0).toString() + "*" + str1);
            strb.append(sign);
            strb.append(num.get(1).toString() + "*" + str2);
            if (expr != null) {
                strb.append("+" + expr);
            }
            else {
                strb.append("+" + "0");
            }
            //System.out.println("expr="+expr);
            strb.append(")");
            strn = Prework.prework(strb.toString());
            fn.put(i,simplify(strn));
        }
    }
    
    public String simplify(String strn) {
        return strn;
        /*Lexer lexer = new Lexer(strn);
        Parser parser = new Parser(lexer, vars.size(),this);
        Expr expr = parser.parseExpr();
        calcuate.Poly poly = expr.getPoly();
        return poly.polyToString();*/
    }
    
    public boolean isdigit(char ch) {
        return ('0' <= ch) && (ch <= '9');
    }
    
    public void getnum(String str) {
        BigInteger res = BigInteger.ZERO;
        int mark = 1;
        if (str.charAt(pos) == '+') {
            mark = 1;
            pos++;
        }
        else if (str.charAt(pos) == '-') {
            mark = -1;
            pos++;
        }
        //System.out.println("--pos="+pos);
        while (isdigit(str.charAt(pos))) {
            //System.out.println("Yes");
            BigInteger val = new BigInteger(String.valueOf(str.charAt(pos)));
            //System.out.println("val="+val);
            res = res.multiply(ten);
            res = res.add(val);
            pos++;
        }
        //System.out.println("res="+res);
        if (mark == 1) {
            num.add(res);
        }
        else {
            num.add(res.negate());
        }
    }
    
    public void xingcan(String str) {
        
        int cnt = 0;
        while (str.charAt(pos) != '(') {
            pos++;
        }
        pos++;//(
        cnt++;
        StringBuilder strb = new StringBuilder();
        while ((str.charAt(pos) != ',') && (!(cnt == 1 && str.charAt(pos) == ')'))) {
            strb.append(str.charAt(pos));
            if ((str.charAt(pos) == '(')) {
                cnt++;
            }
            else if (str.charAt(pos) == ')') {
                cnt--;
            }
            pos++;
        }
        first.add(strb.toString());
        if (str.charAt(pos) == ')') {
            pos++;//)
            return;
        }
        pos++;//,
        strb = new StringBuilder();
        while (!(cnt == 1 && str.charAt(pos) == ')')) {
            strb.append(str.charAt(pos));
            if ((str.charAt(pos) == '(')) {
                cnt++;
            }
            else if (str.charAt(pos) == ')') {
                cnt--;
            }
            pos++;
        }
        second.add(strb.toString());
        pos++;//)
    }
}
