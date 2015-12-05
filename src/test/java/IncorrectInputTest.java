import configuration.Mode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import service.Processor;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by jcincera on 04/12/15.
 */
public class IncorrectInputTest extends AbstractTest {

    @Before
    public void setUp() throws Exception {
        Files.copy(Paths.get(MASTER_FILE), Paths.get(TEST_MASTER_FILE));
    }

    @Test
    public void verifyInvalidMasterPasswordForDelete() throws Exception {
        encrypt("123456");
        byte[] original = readMasterFileBytes();
        new Processor(cfg(Mode.DELETE, "service-name", "", "1234567890")).run();

        assertEqualsFiles(original, readMasterFileBytes());
    }

    @Test
    public void verifyInvalidMasterPasswordForAdd() throws Exception {
        encrypt("123456");
        byte[] original = readMasterFileBytes();
        new Processor(cfg(Mode.ADD, "service-name", "", "1234567890")).run();

        assertEqualsFiles(original, readMasterFileBytes());
    }

    @Test
    public void verifyInvalidMasterPasswordForDecrypt1() throws Exception {
        encrypt("123456");
        byte[] original = readMasterFileBytes();
        new Processor(cfg(Mode.DECRYPT, "service-name", "", "1234567890")).run();

        assertEqualsFiles(original, readMasterFileBytes());
    }

    @Test
    public void verifyInvalidMasterPasswordForDecrypt2() throws Exception {
        encrypt("123456");
        byte[] original = readMasterFileBytes();
        new Processor(cfg(Mode.DECRYPT, "service-name", "", "")).run();

        assertEqualsFiles(original, readMasterFileBytes());
    }

    @Test
    public void verifyDeleteNonExistingService() throws Exception {
        encrypt("123456");
        byte[] original = readMasterFileBytes();
        new Processor(cfg(Mode.DELETE, "non-existing-service", "", "123456")).run();

        assertEqualsFiles(original, readMasterFileBytes());
    }

    @Test
    public void verifyReadNonExistingService() throws Exception {
        encrypt("123456");
        byte[] original = readMasterFileBytes();
        new Processor(cfg(Mode.READ, "non-existing-service", "", "123456")).run();

        assertEqualsFiles(original, readMasterFileBytes());
    }

    @Test
    public void verifyAddUnsupportedServiceName() throws Exception {
        encrypt("123456");
        byte[] original = readMasterFileBytes();
        new Processor(cfg(Mode.ADD, "service" + SPLIT_SEQUENCE + "ooo", "123", "123456")).run();

        assertEqualsFiles(original, readMasterFileBytes());
    }

    @Test
    public void verifyAddUnsupportedServiceValue() throws Exception {
        encrypt("123456");
        byte[] original = readMasterFileBytes();
        new Processor(cfg(Mode.ADD, "123", "value" + SPLIT_SEQUENCE + "ooo", "123456")).run();

        assertEqualsFiles(original, readMasterFileBytes());
    }

    @Test
    public void verifyAddUnsupportedServiceNameAndValue() throws Exception {
        encrypt("123456");
        byte[] original = readMasterFileBytes();
        new Processor(cfg(Mode.ADD, "service" + SPLIT_SEQUENCE + "ppp", "value" + SPLIT_SEQUENCE + "ooo", "123456")).run();

        assertEqualsFiles(original, readMasterFileBytes());
    }

    @After
    public void tearDown() throws Exception {
        Files.delete(Paths.get(TEST_MASTER_FILE));
    }
}
