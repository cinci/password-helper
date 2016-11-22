import configuration.Mode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import service.Processor;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertTrue;

/**
 * Created by jcincera on 12/19/16.
 */
public class ListModeTest extends AbstractTest {

    private ByteArrayOutputStream tmpOut;
    private PrintStream old;

    @Before
    public void setUp() throws Exception {
        Files.copy(Paths.get(MASTER_FILE), Paths.get(TEST_MASTER_FILE));

        tmpOut = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(tmpOut);
        old = System.out;
        System.setOut(ps);
    }

    @Test
    public void verifySingleItem() throws Exception {
        encrypt("123456");
        new Processor(cfg(Mode.LIST, "", "", "123456")).run();
        switchToStandardOutput();

        String output = tmpOut.toString();

        assertTrue(output.contains("service-name"));
        assertTrue(!output.contains("some-username-and-password"));
    }

    @Test
    public void verifyMultipleItems() throws Exception {
        encrypt("123456");
        new Processor(cfg(Mode.ADD, "service 4 =", "abc 123", "123456")).run();
        new Processor(cfg(Mode.ADD, "service 2", "www", "123456")).run();
        new Processor(cfg(Mode.ADD, "service 5 xxx", "ooo", "123456")).run();
        new Processor(cfg(Mode.ADD, "service 3 !@#$%^&*()_", "abc 123", "123456")).run();

        new Processor(cfg(Mode.LIST, "", "", "123456")).run();

        switchToStandardOutput();
        String output = tmpOut.toString();

        assertTrue(output.contains("service 4"));
        assertTrue(output.contains("service 2"));
        assertTrue(output.contains("service 5 xxx"));
        assertTrue(output.contains("service 3 !@#$%^&*()_"));

        assertTrue(!output.contains("abc 123"));
        assertTrue(!output.contains("www"));
        assertTrue(!output.contains("ooo"));
    }

    @After
    public void tearDown() throws Exception {
        Files.delete(Paths.get(TEST_MASTER_FILE));
    }

    private void switchToStandardOutput() {
        System.out.flush();
        System.setOut(old);
    }
}
