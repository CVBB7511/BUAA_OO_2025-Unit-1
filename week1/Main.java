import Level.Expr;
import calcuate.Poly;

import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        String str = Prework.prework(input);
        //System.out.println("str="+str);
        Lexer lexer = new Lexer(str);
        Parser parser = new Parser(lexer);
        Expr expr = parser.parseExpr();
        Poly poly = expr.getPoly();
        System.out.printf("%s",poly.PolytoString());
    }
}