package configuration;

/**
 * Created by jcincera on 01/12/15.
 */
public class Configuration {
    private Mode mode;
    private String serviceName;
    private String serviceValue;
    private String password;

    public Configuration(Mode mode, String serviceName, String serviceValue, String password) {
        this.mode = mode;
        this.serviceName = serviceName;
        this.serviceValue = serviceValue;
        this.password = password;
    }

    public Mode getMode() {
        return mode;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getServiceValue() {
        return serviceValue;
    }

    public String getPassword() {
        return password;
    }
}
