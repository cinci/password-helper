import configuration.Configuration;
import configuration.Mode;
import service.Processor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by jcincera on 04/12/15.
 */
public class AbstractTest {
    protected static final String FILE_CONTENT_VALIDATOR = "===OK===";
    protected static final String MASTER_FILE = "master-file.txt";
    protected static final String TEST_MASTER_FILE = "test-master-file.txt";

    protected Configuration cfg(Mode mode, String serviceName, String serviceValue, String masterPassword) {
        return new Configuration(mode, serviceName, serviceValue, masterPassword, TEST_MASTER_FILE);
    }

    protected List<String> readMasterFileLines() throws IOException {
        return Files.readAllLines(Paths.get(TEST_MASTER_FILE));
    }

    protected byte[] readMasterFileBytes() throws IOException {
        return Files.readAllBytes(Paths.get(TEST_MASTER_FILE));
    }

    protected void encrypt(String masterPassword) {
        new Processor(cfg(Mode.ENCRYPT, "", "", masterPassword)).run();
    }

    protected void decrypt(String masterPassword) {
        new Processor(cfg(Mode.DECRYPT, "", "", masterPassword)).run();
    }

    protected void addServices(Integer number) {
        for (int i = 0; i < number; i++) {
            new Processor(cfg(Mode.ADD, "new-service-" + i, "service-password-" + i, "123456")).run();
        }
    }
}
