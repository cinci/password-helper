package service;

import configuration.Mode;

import java.io.Console;

/**
 * Created by jcincera on 27/02/2017.
 */
public class ConsoleService {

    public Mode readMode() {
        Console console = getConsole();
        console.printf("Mode (read/add/delete/encrypt/decrypt/list): ");
        Mode mode = Mode.fromValue(console.readLine());

        if (mode == null) {
            System.out.println("Invalid mode!");
            System.exit(1);
        }

        return mode;
    }

    public String readServiceName(Mode mode) {
        String serviceName = "";

        if (mode.hasServiceNamePrompt()) {
            Console console = getConsole();
            console.printf("Service name: ");
            serviceName = console.readLine();
        }

        return serviceName != null ? serviceName : "";
    }

    public String readServiceValue(Mode mode) {
        String serviceValue = "";

        if (mode.hasServiceValuePrompt()) {
            serviceValue = readPassword("Service value: ");
        }

        return serviceValue != null ? serviceValue : "";
    }

    public String readPassword(String message) {
        Console console = getConsole();
        console.printf(message);
        char[] password = console.readPassword();

        return password != null ? new String(password) : "";
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
