
public class CellEntry  implements Index2D {

    private char x;
    private int y;

    public CellEntry(String s) {
        this.x = Character.toUpperCase(s.charAt(0));
        this.y = Integer.parseInt(s.substring(1));
    }

    @Override
    public String toString() {
        return Character.toUpperCase(x)+""+(char)(y);
    }

    @Override
    public boolean isValid() {
        return Character.isLetter(x) && y >= 0 && y < 100;
    }

    @Override
    public int getX() {
        return this.x - 65;
    }
    @Override
    public int getY() {return y;}


}
