public class Lexer {
    private final String str;
    private final int len;
    private int idx;
    private String curtoken; //存储元
    private final String op1= "+-*()";
    
    public Lexer(String str) {
        this.str = str;
        this.len = str.length();
        this.idx = 0;
        this.next();
    }
    
    protected boolean isnum(char c) {
        return ('0'<=c)&&(c<='9');
    }
    
    protected void next() {
        if (idx == len) {
            return ;
        }
        char now = str.charAt(idx);
        if (isnum(now)) {
            curtoken = this.getnum();
        }
        else if(op1.indexOf(now) != -1) {
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
        else if (now == 'x') {
            idx ++ ;
            curtoken = "x";//this.getgene();
        }
        else {
            System.out.println("Lexer fail");
        }
    }
    
    protected String getnum() {
        StringBuilder strb = new StringBuilder();
        while(idx < len && isnum(str.charAt(idx))) {
            strb.append(str.charAt(idx));
            idx++;
        }
        return strb.toString();
    }
    
    /*protected String getgene() {
        StringBuilder strx = new StringBuilder();
        //读取完整的变量名
        strx.append("x");
        if (str.charAt(idx) == '^') {
            strx.append("^");
            idx++;
            strx.append(this.getnum());
        }
        return strx.toString();
    }*/
    
    public String getToken() {
        return this.curtoken;
    }
}
