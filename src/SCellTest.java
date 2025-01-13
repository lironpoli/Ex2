
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SCellTest {
    private SCell cell;

    @BeforeEach
    void setUp() {
        cell = new SCell(" ",0, 0);
    }

    @Test
    public void testGetData() {
        assertEquals("1", cell.getData(), "The data should be '1'");
    }

    @Test
    public void testSetData() {
        cell.setData("Hello");
        assertEquals("Hello", cell.getData(), "The data should be updated to 'Hello'");
        assertEquals(Ex2Utils.TEXT, cell.getType(), "The type should be TEXT after setting to 'Hello'");
    }

    //@Test
    //public void testDetermineTypeWithNumber() {
      //  cell.setData("3.14");
      //  assertEquals(Ex2Utils.NUMBER, SCell.getType(), "The type should be NUMBER for '3.14'");
    //}

    @Test
    public void testDetermineTypeWithNegativeNumber() {
        cell.setData("-5.67");
        assertEquals(Ex2Utils.NUMBER, cell.getType(), "The type should be NUMBER for '-5.67'");
    }

    @Test
    public void testDetermineTypeWithText() {
        cell.setData("Hello");
        assertEquals(Ex2Utils.TEXT, cell.getType(), "The type should be TEXT for 'Hello'");
    }

    @Test
    public void testDetermineTypeWithFormula() {
        cell.setData("=A1 + 2");
        assertEquals(Ex2Utils.FORM, cell.getType(), "The type should be FORM for '=A1 + 2'");
    }

    @Test
    public void testDetermineTypeWithEmptyString() {
        cell.setData("");
        assertEquals(Ex2Utils.EMPTY_CELL, cell.getType(), "The type should be EMPTY_CELL for an empty string");
    }

    @Test
    public void testDetermineTypeWithNull() {
        cell.setData(null);
        assertEquals(Ex2Utils.EMPTY_CELL, cell.getType(), "The type should be EMPTY_CELL for null data");
    }

    @Test
    public void testDetermineTypeWithInvalidText() {
        cell.setData("AB");
        assertEquals(Ex2Utils.ERR_FORM_FORMAT, cell.getType(), "The type should be ERR_FORM_FORMAT for invalid text 'AB'");
    }

    @Test
    public void testGetOrder() {
        assertEquals(0, cell.getOrder(), "The initial order should be 0");
    }

    @Test
    public void testSetOrder() {
        cell.setOrder(2);
        assertEquals(2, cell.getOrder(), "The order should be updated to 2");
    }

    @Test
    public void testToString() {
        assertEquals("1", cell.toString(), "The string representation should be '1'");
        cell.setData("Hello");
        assertEquals("Hello", cell.toString(), "The string representation should be 'Hello'");
    }

    @Test
    public void testSetDataWithFormula() {
        cell.setData("=1 + 2");
        assertEquals("=1 + 2", cell.getData(), "The data should be set to '=1 + 2'");
        assertEquals(Ex2Utils.FORM, cell.getType(), "The type should be FORM for '=1 + 2'");
    }

    @Test
    public void testSetDataWithWhitespace() {
        cell.setData("   ");
        assertEquals(Ex2Utils.ERR_FORM, cell.getType(), "The type should be ERR_FORM for whitespace");
    }

    @Test
    public void testEmptyCell() {
        Ex2Sheet sheet = new Ex2Sheet(5, 5);
        String value = sheet.value(0, 0); // Get the value of the first cell
        assertEquals(Ex2Utils.EMPTY_CELL, value); // Assert that it matches EMPTY_CELL
    }
}