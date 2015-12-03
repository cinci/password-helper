package configuration;

/**
 * Created by jcincera on 01/12/15.
 */
public enum Mode {

    ADD("add"),
    DELETE("delete"),
    READ("read"),
    ENCRYPT("encrypt"),
    DECRYPT("decrypt");

    private String value;

    Mode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
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
