package level;

import deal.Lexer;
import calcuate.Poly;
import java.util.ArrayList;
import deal.FDefiner;

public class Func implements Factor {
    private int index;
    private ArrayList<Factor> vars = new ArrayList<>(); //实参
    private FDefiner definer = null;
    private String finalStr = null;
    
    public Func(int index, ArrayList<Factor> vars, FDefiner definer) {
        this.index = index;
        this.vars = vars;//浅拷贝
        this.definer = definer;
    }
    
    public Poly getPoly() {
        //字符串替换 形参->实参
        //System.out.println("index="+index);
        String str = definer.getfn(index);
        int n = vars.size();
        //System.out.println("1str="+str);
        if (n > 1) {
            str = str.replaceAll(definer.getvar(1),"t");
        }
        //System.out.println("2str="+str);
        Factor factor = this.vars.get(0);//实参
        String para = "(" + factor.getString() + ")";
        str = str.replaceAll(definer.getvar(0),para);
        //System.out.println("3str="+str);
        if (n > 1) {
            factor = this.vars.get(1);//实参
            para = "(" + factor.getString() + ")";
            str = str.replaceAll("t",para);
        }
        //System.out.println("4str="+str);
        // Expr 解析
        //System.out.println("fstr="+str);
        str = deal.Prework.prework(str);
        // System.out.println("Str="+str);
        // .out.println("str="+str);
        Lexer lexer = new Lexer(str);
        deal.Parser parser = new deal.Parser(lexer, vars.size(), definer);
        Expr expr = parser.parseExpr();
        Poly poly = expr.getPoly();
        //System.out.println("String="+poly.polyToString());
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
