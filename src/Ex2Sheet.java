import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Ex2Sheet implements Sheet {
    private final Cell[][] table;
    private List<String> dependencies = new ArrayList<>();

    public Ex2Sheet(int x, int y) {
        table = new SCell[x][y];
        for(int i=0;i<x;i=i+1) {
            for(int j=0;j<y;j=j+1) {
                table[i][j] = new SCell(Ex2Utils.EMPTY_CELL, i, j);
            }
        }
        eval();
    }

    public Ex2Sheet() {
        this(Ex2Utils.WIDTH, Ex2Utils.HEIGHT);
    }

    @Override
    public boolean isIn(int x, int y) {
        return x >= 0 && x < width() && y >= 0 && y < height();
    }


    @Override
    public int width() {
        return table.length;
    }
    @Override
    public int height() {
        return table[0].length;
    }
    @Override
    public void set(int x, int y, String s) {
        Cell c = new SCell(s, x, y);
        table[x][y] = c;
    }

    @Override
    public Cell get(int x, int y) {
        if (!isIn(x, y)) {
            throw new IllegalArgumentException("Coordinates out of bounds: (" + x + ", " + y + ")");
        }
        return table[x][y];
    }

    @Override
    public Cell get(String cords) {
        CellEntry entry = new CellEntry(cords);
        if (entry.isValid())
            return get(entry.getX(), entry.getY());
        else return null;
    }

    @Override
    public String value(int x, int y) {
        Cell c = get(x,y);
        String ans;
        ans = c.getData();
        if(!Objects.equals(ans, Ex2Utils.EMPTY_CELL)) {
            if (isNumber(ans)) {
                c.setType(Ex2Utils.NUMBER);
                double num = Double.parseDouble(ans);
                ans = String.valueOf(num);
            }

            else if (ans.startsWith("=")) {
                if (checkForm(ans.substring(1)) == Ex2Utils.FORM){
                    c.setType(Ex2Utils.FORM);
                    ans = eval(x, y);
                }else {
                    c.setType(Ex2Utils.ERR_FORM_FORMAT);
                    ans = Ex2Utils.ERR_FORM;
                }
            }
            else c.setType(Ex2Utils.TEXT);
        }
        table[x][y] = c;
        return ans;
    }

    @Override
    public String eval(int x, int y) {
        String ans = get(x,y).getData();
        if(!ans.isEmpty()) {
            if (isNumber(ans)){
                return ans;
            }
            else if (ans.startsWith("=")){
                return calcForm(ans.substring(1).toUpperCase());
            }
            else return Ex2Utils.ERR_FORM;
        }
        return ans;
    }

    public boolean isNumber(String text) {
        return SCell.isNumber(text);
    }

    @Override
    public void eval() {
        int[][] dd = depth();
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                table[i][j].setOrder(dd[i][j]);
            }
        }
    }

    @Override
    public int[][] depth() {
        int[][] ans = new int[width()][height()];
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                ans[i][j] = -1;
            }
        }
        int depth = 0 , count = 0, max = width()*height();
        boolean flagC = true;
        while (count < max && flagC){
            flagC = false;
            for (int i = 0; i < width(); i++) {
                for (int j = 0; j < height(); j++) {
                    if (canBeComputedNow(i,j)){
                        ans[i][j] = depth;
                        count++;
                        flagC = true;
                    }
                }
            }
            depth++;
        }
        return ans;
    }

    private boolean canBeComputedNow(int i, int j) {
        Cell cell = get(i,j);
        String[] tokens = cell.getData().split("[+\\-*/()]");
        for (String token : tokens) {
            if (token.isEmpty() || Character.isLetter(token.charAt(0))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void load(String fileName) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            int row = 0;
            while ((line = reader.readLine()) != null && row < height()) {
                String[] values = line.split(","); // Split the line by commas
                for (int col = 0; col < values.length && col < width(); col++) {
                    set(col, row, values[col].trim()); // Set the cell value, trimming whitespace
                }
                row++;
            }
        }    }

    @Override
    public void save(String fileName) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (int i = 0; i < height(); i++) {
                StringBuilder line = new StringBuilder();
                for (int j = 0; j < width(); j++) {
                    Cell cell = table[j][i]; // Note: table[x][y] is accessed as table[column][row]
                    if (cell != null) {
                        line.append(cell.getData());
                    }
                    if (j < width() - 1) {
                        line.append(","); // Add a comma between cell values
                    }
                }
                writer.write(line.toString());
                writer.newLine(); // Write a new line after each row
            }
        }    }

    private String calcForm(String formula) {
        try {
            if (isNumber(formula)) {
                return formula;
            }

            if (isReferenceToCell(formula)) {
//                if (dependencies.contains(formula)) {return Ex2Utils.ERR_CYCLE;}
//                else {dependencies.add(formula);}
                CellEntry entry = new CellEntry(formula);
                if (!isIn(entry.getX(), entry.getY())) {
                    return Ex2Utils.ERR_FORM;
                }
                return eval(entry.getX(), entry.getY());
            }

            if (formula.startsWith("(") && formula.endsWith(")")) {
                return calcForm(formula.substring(1, formula.length() - 1));
            }

            int operatorIndex = findLastOperatorIndex(formula);

            if (operatorIndex == -1) {
                return Ex2Utils.ERR_FORM;
            }

            String leftPart = formula.substring(0, operatorIndex).trim();
            String rightPart = formula.substring(operatorIndex + 1).trim();
            char operator = formula.charAt(operatorIndex);

            if (leftPart.isEmpty() || rightPart.isEmpty()) {
                return Ex2Utils.ERR_FORM;
            }

            String leftValue = calcForm(leftPart);
            String rightValue = calcForm(rightPart);

            return compute(leftValue, rightValue, operator);
        }catch (Exception e){return Ex2Utils.ERR_FORM;}
    }



    private boolean isReferenceToCell(String reference) {
        try {
            CellEntry entry = new CellEntry(reference);
            return isIn(entry.getX(), entry.getY());
        } catch (Exception e) {
            return false;
        }
    }



    private static int findLastOperatorIndex(String formula) {
        int balance = 0;
        int lastHighPriorityOperator = -1;
        for (int i = formula.length() - 1; i >= 0; i--) {
            char c = formula.charAt(i);

            if (c == ')') balance++;
            else if (c == '(') balance--;
            if (balance == 0) {
                if (c == '*' || c == '/') {
                    return i;
                } else if (c == '+' || c == '-') {
                    if (lastHighPriorityOperator == -1) {
                        lastHighPriorityOperator = i;
                    }
                }
            }
        }
        return lastHighPriorityOperator;
    }


    private static String compute(String left, String right, char operator) {
        double L = Double.parseDouble(left);
        double R = Double.parseDouble(right);

        double result = switch (operator) {
            case '+' -> L + R;
            case '-' -> L - R;
            case '*' -> L * R;
            case '/' -> L / R;
            default -> throw new IllegalArgumentException("Unknown operator: " + operator);
        };
        return String.valueOf(result);

    }

    private int checkForm(String substring) {

        if (substring == null || substring.isEmpty()) {
            return Ex2Utils.ERR_FORM_FORMAT;
        }
        for (int i = 0; i < substring.length()-1; i++) {
            char c1 = substring.charAt(i);
            char c2 = substring.charAt(i+1);
            if (SCell.isOperator(c1) && SCell.isOperator(c2)) return Ex2Utils.ERR_FORM_FORMAT;
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

}