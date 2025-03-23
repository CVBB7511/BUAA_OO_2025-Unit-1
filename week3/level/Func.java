package level;

import deal.Lexer;
import calcuate.Poly;
import java.util.ArrayList;
import deal.FDefiner;

public class Func implements Factor {
    private int index;
    private String name;
    private ArrayList<Factor> vars = new ArrayList<>(); //实参
    private FDefiner definer = null;
    private String finalStr = null;
    private Poly poly = null;
    private Poly dx = null;
    private Expr expr = null;
    
    public Func(int index, ArrayList<Factor> vars, FDefiner definer,String name) {
        this.index = index;
        this.vars = vars;//实参的数量
        this.definer = definer;
        this.name = name;
    }
    
    public Poly getPoly() {
        //字符串替换 形参->实参
        //System.out.println("index="+index);
        if (poly != null) {
            return poly;
        }
        String str = definer.getexpr(name,index);
        
        int n = vars.size();//实参数量
        //System.out.println("name="+name+"  1str="+str);
        if (n > 1) {
            str = str.replaceAll(definer.getvar(name,1),"t");
        }
        //System.out.println("2str="+str);
        Factor factor = this.vars.get(0);//实参
        String para = "(" + factor.getString() + ")";
        //System.out.println(str);
        str = str.replaceAll(definer.getvar(name,0),para);
        //System.out.println("3str="+str);
        if (n > 1) {
            factor = this.vars.get(1);//实参
            para = "(" + factor.getString() + ")";
            str = str.replaceAll("t",para);
        }
        //System.out.println("4str="+str);
        // Expr
        str = deal.Prework.prework(str);
        //System.out.println("fstr="+str);
        Lexer lexer = new Lexer(str);
        deal.Parser parser = new deal.Parser(lexer, definer);
        expr = parser.parseExpr();
        poly = expr.getPoly();
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
    
    public Poly getdx() {
        //System.out.println("fuck");
        if (dx != null) {
            return dx;
        }
        dx = new Poly();
        //获取expr
        if (poly == null) {
            this.getPoly();
        }
        //System.out.println("函数因子求导结果" + expr.getString());
        dx = expr.getdx();
        return dx;
    }
}
