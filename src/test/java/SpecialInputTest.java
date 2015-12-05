import configuration.Mode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import service.Processor;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by jcincera on 04/12/15.
 */
public class SpecialInputTest extends AbstractTest {

    @Before
    public void setUp() throws Exception {
        Files.copy(Paths.get(MASTER_FILE), Paths.get(TEST_MASTER_FILE));
    }

    @Test
    public void verifySpaceInServiceName() throws Exception {
        encrypt("123456");
        new Processor(cfg(Mode.ADD, "new service", "service-password", "123456")).run();
        decrypt("123456");

        List<String> lines = readMasterFileLines();
        assertEquals(3, lines.size());
        assertTrue(lines.get(2).startsWith("new service"));
        assertTrue(lines.get(2).contains("service-password"));
    }

    @Test
    public void verifySpaceInServiceValue() throws Exception {
        encrypt("123456");
        new Processor(cfg(Mode.ADD, "new-service", "service password", "123456")).run();
        decrypt("123456");

        List<String> lines = readMasterFileLines();
        assertEquals(3, lines.size());
        assertTrue(lines.get(2).startsWith("new-service"));
        assertTrue(lines.get(2).contains("service password"));
    }

    @Test
    public void verifySpaceInServiceNameAndValue() throws Exception {
        encrypt("123456");
        new Processor(cfg(Mode.ADD, "new service", "service password", "123456")).run();
        decrypt("123456");

        List<String> lines = readMasterFileLines();
        assertEquals(3, lines.size());
        assertTrue(lines.get(2).startsWith("new service"));
        assertTrue(lines.get(2).contains("service password"));
    }

    @Test
    public void verifySpecialCharactersInServiceName() throws Exception {
        encrypt("123456");
        new Processor(cfg(Mode.ADD, "ASDFGHJKLasdfghjkl1234567890-=[];',./!@#$%^&*()_+", "service-password", "123456")).run();
        decrypt("123456");

        List<String> lines = readMasterFileLines();
        assertEquals(3, lines.size());
        assertTrue(lines.get(2).startsWith("ASDFGHJKLasdfghjkl1234567890-=[];',./!@#$%^&*()_+"));
        assertTrue(lines.get(2).contains("service-password"));
    }

    @Test
    public void verifySpecialCharactersInServiceValue() throws Exception {
        encrypt("123456");
        new Processor(cfg(Mode.ADD, "new-service", "ASDFGHJKLasdfghjkl1234567890-=[];',./!@#$%^&*()_+", "123456")).run();
        decrypt("123456");

        List<String> lines = readMasterFileLines();
        assertEquals(3, lines.size());
        assertTrue(lines.get(2).startsWith("new-service"));
        assertTrue(lines.get(2).contains("ASDFGHJKLasdfghjkl1234567890-=[];',./!@#$%^&*()_+"));
    }

    @Test
    public void verifySpecialCharactersInServiceNameAndValue() throws Exception {
        encrypt("123456");
        new Processor(cfg(Mode.ADD, "ASDFGHJKLasdfghjkl1234567890-=[];',./!@#$%^&*()_+",
                "ASDFGHJKLasdfghjkl1234567890-=[];',./!@#$%^&*()_+", "123456")).run();
        decrypt("123456");

        List<String> lines = readMasterFileLines();
        assertEquals(3, lines.size());
        assertTrue(lines.get(2).startsWith("ASDFGHJKLasdfghjkl1234567890-=[];',./!@#$%^&*()_+"));
        assertTrue(lines.get(2).contains("ASDFGHJKLasdfghjkl1234567890-=[];',./!@#$%^&*()_+"));
    }

    @After
    public void tearDown() throws Exception {
        Files.delete(Paths.get(TEST_MASTER_FILE));
    }
}
