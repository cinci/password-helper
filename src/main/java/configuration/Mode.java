package configuration;

/**
 * Created by jcincera on 01/12/15.
 */
public enum Mode {

    ADD("add", true, true),
    DELETE("delete", true, false),
    READ("read", true, false),
    ENCRYPT("encrypt", false, false),
    DECRYPT("decrypt", false, false),
    LIST("list", false, false);

    private String value;
    private Boolean serviceNamePrompt;
    private Boolean serviceValuePrompt;

    Mode(String value, Boolean serviceNamePrompt, Boolean serviceValuePrompt) {
        this.value = value;
        this.serviceNamePrompt = serviceNamePrompt;
        this.serviceValuePrompt = serviceValuePrompt;
    }

    public String getValue() {
        return value;
    }

    public Boolean hasServiceNamePrompt() {
        return serviceNamePrompt;
    }

    public Boolean hasServiceValuePrompt() {
        return serviceValuePrompt;
    }

    public static Mode fromValue(String value) {
        for (Mode v : Mode.values()) {
            if (v.getValue().equals(value)) {
                return v;
            }
        }

        return null;
    }
}
