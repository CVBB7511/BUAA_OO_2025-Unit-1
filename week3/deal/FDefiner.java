package deal;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class FDefiner {
    private HashMap<String,ArrayList<String>> vars = new HashMap<>();
    private HashMap<String,HashMap<Integer,String>> fexpr = new HashMap<>();
    private int pos = 0;
    private ArrayList<BigInteger> num = new ArrayList<>();//递推函数f中的常数因子
    private ArrayList<String> first = new ArrayList<>(); // 递推函数f中第一个形参对应的实参
    private ArrayList<String> second = new ArrayList<>();
    private String expr = null; //f递推式的第三部分
    private BigInteger ten = new BigInteger("10");
    private char sign;
    
    public FDefiner(Scanner scanner) {
        int n = scanner.nextInt();
        // 处理普通自定义函数
        ghDefiner(n,scanner);
        int m = scanner.nextInt();
        scanner.nextLine();
        if (m == 0) {
            return;
        }
        int len;
        for (int i = 1; i <= 3; i++) {
            String str = scanner.nextLine();
            //System.out.println("str1="+str);
            str = Prework.prework(str);
            //System.out.println("str2="+str);
            len = str.length();
            if ((str.charAt(2) == '0') || (str.charAt(2) == '1')) {
                if (!vars.containsKey("f")) {
                    this.getvar("f",str);
                }
                pos = str.indexOf("=");
                HashMap<Integer,String> fn = getfn();
                fn.put(str.charAt(2) - '0',"(" + str.substring(pos + 1) + ")");
                fexpr.put("f",fn);
            }
            else {
                pos = str.indexOf("=") + 1;
                getnum(str);// pos @ *
                xingcan(str);
                sign = str.charAt(pos);
                pos++; // + -
                getnum(str);
                xingcan(str);
                if (pos < len) {
                    expr = Prework.prework(str.substring(pos));
                }
            }
        }
        ditui();
        //check();
    }
    
    public void ghDefiner(int n, Scanner scanner) {
        scanner.nextLine();
        String str;
        for (int i = 1; i <= n; i++) {
            str = scanner.nextLine();
            str = Prework.prework(str);
            int len = str.length();
            String name = String.valueOf(str.charAt(0));
            //提取形参
            getvar(name,str);
            pos = str.indexOf('=') + 1;
            HashMap<Integer,String> gh = new HashMap<>();
            gh.put(0,'(' + str.substring(pos) + ')');
            fexpr.put(name,gh);
        }
    }
    
    public void check() {
        //System.out.println(vars.get(0));
        //System.out.println(vars.get(1));
        //System.out.println(first.get(0));
        //System.out.println(first.get(1));
        //System.out.println(second.get(0));
        // System.out.println(second.get(1));
        HashMap<Integer,String> fn = fexpr.get("f");
        for (int i = 0; i <= 4;i++) {
            System.out.println(fn.get(i));
        }
    }
    
    public HashMap<Integer,String> getfn() {
        HashMap<Integer,String> fn;
        if (fexpr.containsKey("f")) {
            fn = fexpr.get("f");
        }
        else {
            fn = new HashMap<>();
            fexpr.put("f",fn);
        }
        return fn;
    }
    
    public String getexpr(String name, int index) {
        if (name.equals("f")) {
            HashMap<Integer,String> fn = getfn();
            return fn.get(index);
        }
        else {
            HashMap<Integer,String> fn = fexpr.get(name);
            return fn.get(0);
        }
    }
    
    public int getvarcnt(String name) {
        ArrayList<String> funcVar = vars.get(name);
        return funcVar.size();
    }
    
    public String getvar(String name,int index) {
        //得到第i个形参
        ArrayList<String> func = vars.get(name);
        return func.get(index);
    }
    
    public void getvar(String name,String str) {
        //提取形参
        if (name.equals("f")) {
            pos = 5;
        }
        else {
            pos = 2;
        }
        if (!vars.containsKey(name)) {
            vars.put(name,new ArrayList<>());
        }
        ArrayList<String> func = vars.get(name);
        while (str.charAt(pos) != ')') {
            StringBuilder strb = new StringBuilder();
            while ((str.charAt(pos) != ',') && (str.charAt(pos) != ')')) {
                strb.append(str.charAt(pos));
                pos++;
            }
            if (str.charAt(pos) == ',') {
                pos++;
            }
            func.add(strb.toString());
        }
    }
    
    public void ditui() {
        String str1;
        String str2;
        StringBuilder strb;
        String strn;
        HashMap<Integer,String> fn = getfn();
        ArrayList<String> func = vars.get("f"); //某个函数的形参表
        for (int i = 2;i <= 5;i++) {
            str1 = fn.get(i - 1);
            str2 = fn.get(i - 2);
            if (func.size() > 1) {
                str1 = str1.replaceAll(func.get(1),"t");
                str2 = str2.replaceAll(func.get(1),"t");
            }
            strb = new StringBuilder();
            //System.out.println("str1="+str1);
            //System.out.println("str2="+str2);
            //ln("func.size()"+func.size());
            str1 = str1.replaceAll(func.get(0),"(" + first.get(0) + ")");
            str2 = str2.replaceAll(func.get(0),"(" + first.get(1) + ")");
            if (func.size() > 1) {
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
            fn.put(i,strn);
        }
    }
    
    public boolean isdigit(char ch) {
        return ('0' <= ch) && (ch <= '9');
    }
    
    public void getnum(String str) {
        BigInteger res = BigInteger.ZERO;
        int mark = 1;
        if (str.charAt(pos) == '+') {
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
        while ((!(str.charAt(pos) == ',' && cnt == 1)) && (!(cnt == 1 && str.charAt(pos) == ')'))) {
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
