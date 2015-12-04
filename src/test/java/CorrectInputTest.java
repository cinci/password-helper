import configuration.Mode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import service.Processor;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by jcincera on 04/12/15.
 */
public class CorrectInputTest extends AbstractTest {

    @Before
    public void setUp() throws Exception {
        Files.copy(Paths.get(MASTER_FILE), Paths.get(TEST_MASTER_FILE));
    }

    @Test
    public void verifyEncryptMode() throws Exception {
        List<String> lines = readMasterFileLines();
        assertEquals(FILE_CONTENT_VALIDATOR, lines.get(0));

        encrypt("123456");

        byte[] content = readMasterFileBytes();
        assertFalse(new String(content).startsWith(FILE_CONTENT_VALIDATOR));
    }

    @Test
    public void verifyAddMode() throws Exception {
        encrypt("123456");
        new Processor(cfg(Mode.ADD, "new-service", "service-password", "123456")).run();
        decrypt("123456");

        List<String> lines = readMasterFileLines();
        assertEquals(3, lines.size());
        assertTrue(lines.get(2).startsWith("new-service"));
        assertTrue(lines.get(2).contains("service-password"));
    }

    @Test
    public void verifyMultipleAddMode() throws Exception {
        encrypt("123456");
        addServices(5);
        decrypt("123456");

        List<String> lines = readMasterFileLines();
        assertEquals(7, lines.size());

        for (int i = 0; i < 5; i++) {
            assertTrue(lines.get(i + 2).startsWith("new-service-" + i));
            assertTrue(lines.get(i + 2).contains("service-password-" + i));
        }
    }

    @Test
    public void verifyDeleteMode() throws Exception {
        encrypt("123456");
        addServices(5);
        new Processor(cfg(Mode.DELETE, "new-service-2", "", "123456")).run();
        decrypt("123456");

        List<String> lines = readMasterFileLines();
        assertEquals(6, lines.size());
        lines.stream().forEach(l -> {
            assertFalse(l.startsWith("new-service-2"));
            assertFalse(l.contains("new-service-2"));
        });
    }

    @Test
    public void verifyClearFileAndAddWithDecryption() throws Exception {
        encrypt("123456");
        new Processor(cfg(Mode.DELETE, "service-name", "", "123456")).run();
        decrypt("123456");

        List<String> lines = readMasterFileLines();
        assertEquals(1, lines.size());
        assertTrue(lines.get(0).equals(FILE_CONTENT_VALIDATOR));

        encrypt("123456");
        addServices(5);
        decrypt("123456");

        lines = readMasterFileLines();
        assertEquals(6, lines.size());
        for (int i = 0; i < 5; i++) {
            assertTrue(lines.get(i + 1).startsWith("new-service-" + i));
            assertTrue(lines.get(i + 1).contains("service-password-" + i));
        }
    }

    @Test
    public void verifyClearFileAndAddWithoutDecryption() throws Exception {
        encrypt("123456");
        new Processor(cfg(Mode.DELETE, "service-name", "", "123456")).run();
        addServices(5);
        decrypt("123456");

        List<String> lines = readMasterFileLines();
        assertEquals(6, lines.size());
        for (int i = 0; i < 5; i++) {
            assertTrue(lines.get(i + 1).startsWith("new-service-" + i));
            assertTrue(lines.get(i + 1).contains("service-password-" + i));
        }
    }

    @After
    public void tearDown() throws Exception {
        Files.delete(Paths.get(TEST_MASTER_FILE));
    }
}
