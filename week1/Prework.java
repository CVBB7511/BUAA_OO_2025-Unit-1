public class Prework {
    public static String prework(String input) {
        String str1;
        String str2;
        StringBuilder builder = new StringBuilder();
        str1 = input.replaceAll(" ","").replaceAll("\t","");
        //System.out.println(str1);
        // 处理连续加减号
        int len = str1.length();
        int i = 0;
        while (i < len) {
            int mark = 1;
            while (i < len && (str1.charAt(i) != '+') && ((str1.charAt(i) != '-'))) {
                builder.append(str1.charAt(i));
                i++;
            }
            if (i >= len) {
                break;
            }
            while (i < len && ((str1.charAt(i) == '+') || ((str1.charAt(i) == '-')))) {
                if (str1.charAt(i) == '-') {
                    mark *= -1;
                }
                i++;
            }
            builder.append((mark == 1) ? '+' : '-');
            // +(0)^0+x* +7*x^+2*(x-x)^0-(x+1)^8-(x^0+x^2+x^+03)^+0+-1145+(x^2)^8-(x^4)^4+1+1+-1-1
        }
        str2 = builder.toString();
        return str2;
    }
}
