//package assignments.ex2;
// Add your documentation below:


public class SCell implements Cell {
    private String line;
    private int type;
    private int order;

    public SCell(String s, int x, int y) {
        setData(s);
    }

    @Override
    public String getData() {
        return line;
    }

    @Override
    public void setData(String s) {
        line = s;
        setType(checkType(s));
    }

    public int checkType(String s){
        if (s.startsWith("=")) {
            return checkForm(s.substring(1));
        } else if (isNumber(s)) return Ex2Utils.NUMBER;
        else return Ex2Utils.TEXT;
    }

    private int checkForm(String substring) {

        if (substring == null || substring.isEmpty()) {
            return Ex2Utils.ERR_FORM_FORMAT;
        }
        for (int i = 0; i < substring.length()-1; i++) {
            char c1 = substring.charAt(i);
            char c2 = substring.charAt(i+1);
            if (isOperator(c1) && isOperator(c2)) return Ex2Utils.ERR_FORM_FORMAT;
        }
        if (!substring.matches("[A-Za-z0-9+\\-*/().]*")) {
            return Ex2Utils.ERR_FORM_FORMAT;
        }
        if (!areParenthesesBalanced(substring)) {
            return Ex2Utils.ERR_FORM_FORMAT;
        }
        if (!areCellReferencesValid(substring)) {
            return Ex2Utils.ERR_FORM_FORMAT;
        }
        return Ex2Utils.FORM;
    }

    private boolean areParenthesesBalanced(String substring) {
        int open = 0;
        int close = 0;
        for (int i = 0; i < substring.length(); i++) {
            if (close > open) return false;
            if (substring.charAt(i) == '(') open++;
            if (substring.charAt(i) == ')') close++;
        }
        return open == close;
    }
    private boolean areCellReferencesValid(String substring) {
        String[] tokens = substring.split("[+\\-*/()]");
        for (String token : tokens) {
            if (Character.isLetter(token.charAt(0))) {
                CellEntry entry = new CellEntry(token);
                if (!entry.isValid())
                    return false;
            }
        }
        return true;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public void setType(int t) {
        type = t;
    }

    @Override
    public int getOrder() {
        if (type == Ex2Utils.TEXT || type == Ex2Utils.NUMBER)
            order = 0;
        else if (type == Ex2Utils.FORM){

            order = 1;

        }
        return order;
    }

    @Override
    public void setOrder(int t) {
        this.type = t;
    }

    public static boolean isNumber(String text) {
        boolean ans = true;
        try {
            double d = Double.parseDouble(text);

        } catch (Exception e) {
            ans = false;
        }
        return ans;
    }
    public static boolean isOperator(char c){
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

}
