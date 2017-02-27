import configuration.Configuration;
import configuration.Mode;
import service.ConsoleService;
import service.Processor;

/**
 * Created by jcincera on 01/12/15.
 */
public class Application {

    private static final String MASTER_FILE = "master-file.txt";

    public static void main(String[] args) {
        ConsoleService console = new ConsoleService();

        Mode mode = console.readMode();
        String serviceName = console.readServiceName(mode);
        String serviceValue = console.readServiceValue(mode);

        String password = console.readPassword("Master password: ");

        Configuration cfg = new Configuration(mode, serviceName, serviceValue, password, MASTER_FILE);
        new Processor(cfg).run();
    }
}
