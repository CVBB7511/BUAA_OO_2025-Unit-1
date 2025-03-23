package deal;

public class Lexer {
    private final String str;
    private final int len;
    private int idx;
    private String curtoken; //存储元
    private final String op1 = "+-*(){},";//{},
    
    public Lexer(String str) {
        this.str = str;
        this.len = str.length();
        this.idx = 0;
        this.next();
    }
    
    protected boolean isnum(char c) {
        return ('0' <= c) && (c <= '9');
    }
    
    protected void next() {
        if (idx == len) {
            return;
        }
        char now = str.charAt(idx);
        if (isnum(now)) {
            curtoken = this.getnum();
        }
        else if (op1.indexOf(now) != -1) {
            //运算符 Type 1 默认长度为1
            curtoken = String.valueOf(now);
            idx++;
        }
        else if (now == '^') {
            idx++;
            if (str.charAt(idx) == '+') {
                idx++;
            }
            curtoken = "^" + this.getnum();
        }
        else if (now == 'x' || now == 'y') {
            idx++;
            curtoken = String.valueOf(now);//this.getgene();
        }
        else if (now == 'f') { //f{num
            idx += 2; ////f{
            curtoken = "f{" + this.getnum();
        }
        else if (now == 's') {
            idx += 4;// sin(
            curtoken = "sin(";
        }
        else if (now == 'c') {
            idx += 4;
            curtoken = "cos(";
        }
        else {
            System.out.println("Deal.Lexer fail");
        }
    }
    
    protected String getnum() {
        StringBuilder strb = new StringBuilder();
        while (idx < len && isnum(str.charAt(idx))) {
            strb.append(str.charAt(idx));
            idx++;
        }
        return strb.toString();
    }
    
    public String getToken() {
        return this.curtoken;
    }
}
