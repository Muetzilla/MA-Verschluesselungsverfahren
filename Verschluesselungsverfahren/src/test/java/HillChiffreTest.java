import ch.maturaarbeit.ciphers.Hill;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HillChiffreTest {
    private static Hill hill;

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        hill = new Hill();
    }

    @Test
    void testFourCharWord() {
        assertEquals("LZXR",hill.encrypt("TONY", "HILL", 2));
    }

    @Test
    void testLongWord() {
        assertEquals("XPDVSVDDQICIDDJB",hill.encrypt("VERSCHLUESSELUNG", "HILL", 2));
    }
    @Test
    void testWordOddLength() {
        assertEquals("XPDVSVDDKGMJKZSH",hill.encrypt("VERSCHLUSSELUNG", "HILL", 2));
    }
}
