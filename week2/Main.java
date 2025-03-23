import deal.FDefiner;
import level.Expr;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int multi = 0;
        FDefiner definer = null;
        if (n != 0) {
            definer = new FDefiner(scanner);
            multi = definer.getcnt();
            //definer.check();
        }
        else {
            scanner.nextLine();
        }
        String input = scanner.nextLine();
        String str = deal.Prework.prework(input);
        //System.out.println("str="+str);
        deal.Lexer lexer = new deal.Lexer(str);
        deal.Parser parser = new deal.Parser(lexer, multi, definer);
        Expr expr = parser.parseExpr();
        calcuate.Poly poly = expr.getPoly();
        System.out.printf("%s",poly.polyToString());
    }
}