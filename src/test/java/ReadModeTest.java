import configuration.Mode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import service.Processor;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by jcincera on 05/12/15.
 */
public class ReadModeTest extends AbstractTest {

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
    public void verifyCorrectRead() throws Exception {
        encrypt("123456");
        new Processor(cfg(Mode.READ, "service-name", "", "123456")).run();
        switchToStandardOutput();

        String output = tmpOut.toString();
        assertEquals("service-name" + SPLIT_SEQUENCE + "some-username-and-password\n", output);
    }

    @Test
    public void verifyCorrectReadWithSpecialCharacters() throws Exception {
        encrypt("123456");
        new Processor(cfg(Mode.ADD, "ASDFGHJKLasdfghjkl1234567890-=[];',./!@#$%^&*()_+",
                "ASDFGHJKLasdfghjkl1234567890-=[];',./!@#$%^&*()_+", "123456")).run();
        new Processor(cfg(Mode.READ, "ASDFGHJKLasdfghjkl1234567890-=[];',./!@#$%^&*()_+", "", "123456")).run();

        switchToStandardOutput();
        String output = tmpOut.toString();
        assertEquals("ASDFGHJKLasdfghjkl1234567890-=[];',./!@#$%^&*()_+" + SPLIT_SEQUENCE + "ASDFGHJKLasdfghjkl1234567890-=[];',./!@#$%^&*()_+\n", output);
    }

    @Test
    public void verifyCorrectReadWithSpace() throws Exception {
        encrypt("123456");
        new Processor(cfg(Mode.ADD, "abc 123", "abc 123", "123456")).run();
        new Processor(cfg(Mode.READ, "abc 123", "", "123456")).run();

        switchToStandardOutput();
        String output = tmpOut.toString();
        assertEquals("abc 123" + SPLIT_SEQUENCE + "abc 123\n", output);
    }

    @Test
    public void verifyCorrectMultipleAddAndRead1() throws Exception {
        encrypt("123456");
        new Processor(cfg(Mode.ADD, "service-1", "abc 123", "123456")).run();
        new Processor(cfg(Mode.ADD, "service 2", "abc 123", "123456")).run();
        new Processor(cfg(Mode.ADD, "service 3 !@#$%^&*()_", "abc 123", "123456")).run();
        new Processor(cfg(Mode.ADD, "service 4 =", "abc 123", "123456")).run();
        new Processor(cfg(Mode.ADD, "service 5 xxx", "abc 123", "123456")).run();

        new Processor(cfg(Mode.READ, "service-1", "", "123456")).run();
        new Processor(cfg(Mode.READ, "service 2", "", "123456")).run();
        new Processor(cfg(Mode.READ, "service 3 !@#$%^&*()_", "", "123456")).run();
        new Processor(cfg(Mode.READ, "service 4 =", "", "123456")).run();
        new Processor(cfg(Mode.READ, "service 5 xxx", "", "123456")).run();

        switchToStandardOutput();
        String output = tmpOut.toString();

        assertTrue(output.contains("service-1 ### abc 123"));
        assertTrue(output.contains("service 2 ### abc 123"));
        assertTrue(output.contains("service 3 !@#$%^&*()_ ### abc 123"));
        assertTrue(output.contains("service 4 = ### abc 123"));
        assertTrue(output.contains("service 5 xxx ### abc 123"));
    }

    @Test
    public void verifyCorrectMultipleAddAndRead2() throws Exception {
        encrypt("123456");
        new Processor(cfg(Mode.ADD, "service 4 =", "abc 123", "123456")).run();
        new Processor(cfg(Mode.ADD, "service-1", "abc 123", "123456")).run();
        new Processor(cfg(Mode.ADD, "service 2", "abc 123", "123456")).run();
        new Processor(cfg(Mode.ADD, "service 5 xxx", "abc 123", "123456")).run();
        new Processor(cfg(Mode.ADD, "service 3 !@#$%^&*()_", "abc 123", "123456")).run();

        new Processor(cfg(Mode.READ, "service-1", "", "123456")).run();
        new Processor(cfg(Mode.READ, "service 3 !@#$%^&*()_", "", "123456")).run();
        new Processor(cfg(Mode.READ, "service 5 xxx", "", "123456")).run();
        new Processor(cfg(Mode.READ, "service 2", "", "123456")).run();
        new Processor(cfg(Mode.READ, "service 4 =", "", "123456")).run();

        switchToStandardOutput();
        String output = tmpOut.toString();

        assertTrue(output.contains("service-1 ### abc 123"));
        assertTrue(output.contains("service 2 ### abc 123"));
        assertTrue(output.contains("service 3 !@#$%^&*()_ ### abc 123"));
        assertTrue(output.contains("service 4 = ### abc 123"));
        assertTrue(output.contains("service 5 xxx ### abc 123"));
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
