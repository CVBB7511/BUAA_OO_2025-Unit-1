import deal.FDefiner;
import level.Expr;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        FDefiner definer = new FDefiner(scanner);
        String input = scanner.nextLine();
        String str = deal.Prework.prework(input);
        //System.out.println("str="+str);
        deal.Lexer lexer = new deal.Lexer(str);
        deal.Parser parser = new deal.Parser(lexer, definer);
        Expr expr = parser.parseExpr();
        calcuate.Poly poly = expr.getPoly();
        System.out.printf("%s",poly.polyToString());
    }
}