import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;


    public class Ex2SheetTest {
        private Ex2Sheet sheet;

        @BeforeEach
        public void setUp() {
            sheet = new Ex2Sheet(5, 5); // Create a 5x5 spreadsheet for testing
        }

        @Test
        public void testInitialization() {
            assertEquals(5, sheet.width());
            assertEquals(5, sheet.height());
            for (int i = 0; i < sheet.width(); i++) {
                for (int j = 0; j < sheet.height(); j++) {
                    assertEquals(Ex2Utils.EMPTY_CELL, sheet.value(i, j));
                }
            }
        }

        @Test
        public void testSetAndGetCellValue() {
            sheet.set(1, 1, "Hello");
            assertEquals("Hello", sheet.value(1, 1));
        }

        @Test
        public void testSetAndGetEmptyCellValue() {
            assertEquals(Ex2Utils.EMPTY_CELL, sheet.value(0, 0));
        }

        @Test
        public void testLoadAndSave() throws IOException {
            sheet.set(0, 0, "A1");
            sheet.set(0, 1, "B1");
            sheet.save("test.csv");

            Ex2Sheet newSheet = new Ex2Sheet(5, 5);
            newSheet.load("test.csv");

            assertEquals("A1", newSheet.value(0, 0));
            assertEquals("B1", newSheet.value(0, 1));
        }

        @Test
        public void testEvaluateSimpleFormula() {
            sheet.set(0, 0, "5");
            sheet.set(0, 1, "3");
            sheet.set(1, 0, "=A1 + A2");
            assertEquals("8.0", sheet.value(1, 0)); // Assuming eval returns a string representation of the result
        }

        @Test
        public void testEvaluateComplexFormula() {
            sheet.set(0, 0, "10");
            sheet.set(0, 1, "5");
            sheet.set(1, 0, "=A1 - A2");
            sheet.set(1, 1, "=A1 * A2");
            assertEquals("5.0", sheet.value(1, 0));
            assertEquals("50.0", sheet.value(1, 1));
        }

        @Test
        public void testInvalidCellReference() {
            sheet.set(0, 0, "=A2"); // A2 is out of bounds
            assertEquals(Ex2Utils.ERR_FORM, sheet.value(0, 0));
        }

        @Test
        public void testEmptyCellHandling() {
            assertEquals(Ex2Utils.EMPTY_CELL, sheet.value(2, 2));
            sheet.set(2, 2, "");
            assertEquals(Ex2Utils.EMPTY_CELL, sheet.value(2, 2));
        }

        @Test
        public void testInvalidFormulaFormat() {
            sheet.set(0, 0, "=A1 +"); // Invalid formula
            assertEquals(Ex2Utils.ERR_FORM, sheet.value(0, 0));
        }

        @AfterEach
        public void tearDown() {
            // Clean up after each test if necessary
        }
    }

