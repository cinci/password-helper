import configuration.Configuration;
import configuration.Mode;
import service.Processor;

import java.io.Console;

/**
 * Created by jcincera on 01/12/15.
 */
public class Application {

    public static void main(String[] args) {
        Application app = new Application();

        Mode mode = app.readMode();
        String serviceName = app.readServiceName();
        String serviceValue = null;
        if (mode.equals(Mode.ADD)) {
            serviceValue = app.readPassword("Service value: ");
        }
        String password = app.readPassword("Master password: ");

        Configuration cfg = new Configuration(mode, serviceName, serviceValue, password);
        new Processor(cfg).run();
    }

    private String readServiceName() {
        Console console = getConsole();
        console.printf("Service name: ");
        return console.readLine();
    }

    private Mode readMode() {
        Console console = getConsole();
        console.printf("Mode (read/add/delete/encrypt/decrypt): ");
        Mode mode = Mode.fromValue(console.readLine());

        if (mode == null) {
            System.out.println("Invalid mode!");
            System.exit(1);
        }

        return mode;
    }

    private String readPassword(String message) {
        Console console = getConsole();
        console.printf(message);
        char[] password = console.readPassword();
        return new String(password);
    }

    private Console getConsole() {
        final Console console = System.console();

        if (console == null) {
            System.out.println("Could not create console instance.");
            System.exit(1);
        }

        return console;
    }
}
