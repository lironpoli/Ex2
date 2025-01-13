import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;


public class CellEntryTest {

    @Test
    public void testConstructorValidInput() {
        CellEntry entry = new CellEntry("A1");
        assertEquals('A', entry.getX() + 65); // Check if getX() returns 0 (A=0)
        assertEquals(1, entry.getY()); // Check if getY() returns 1
    }

    @Test
    public void testConstructorInvalidInput() {
        Exception exception = assertThrows(StringIndexOutOfBoundsException.class, () -> {
            new CellEntry("A"); // Invalid input, should throw an exception
        });
        assertNotNull(exception);
    }

    @Test
    public void testToString() {
        CellEntry entry = new CellEntry("B2");
        assertEquals("B2", entry.toString()); // Check if toString() returns "B2"
    }

    @Test
    public void testIsValid() {
        CellEntry validEntry = new CellEntry("C3");
        assertTrue(validEntry.isValid()); // Check if isValid() returns true

        CellEntry invalidEntry1 = new CellEntry("C100"); // Invalid y-coordinate
        assertFalse(invalidEntry1.isValid());

        CellEntry invalidEntry2 = new CellEntry("Z-1"); // Invalid y-coordinate
        assertFalse(invalidEntry2.isValid());

        CellEntry invalidEntry3 = new CellEntry("1A"); // Invalid x-coordinate
        assertFalse(invalidEntry3.isValid());
    }

    @Test
    public void testGetX() {
        CellEntry entry = new CellEntry("D4");
        assertEquals(3, entry.getX()); // D corresponds to 3 (0-based index)
    }

    @Test
    public void testGetY() {
        CellEntry entry = new CellEntry("E5");
        assertEquals(5, entry.getY()); // Check if getY() returns 5
    }
}