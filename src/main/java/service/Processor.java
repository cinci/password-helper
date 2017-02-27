package service;

import configuration.Configuration;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jcincera on 01/12/15.
 */
public class Processor {

    private static final String FILE_CONTENT_VALIDATOR = "===OK===";
    private static final String SPLIT_SEQUENCE = " ### ";
    private static final String NEW_LINE = "%s" + SPLIT_SEQUENCE + "%s";

    private Configuration cfg;
    private CryptoService cryptoService;
    private FileService fileService;

    public Processor(Configuration cfg) {
        this.cfg = cfg;
        cryptoService = new CryptoService(FILE_CONTENT_VALIDATOR);
        fileService = new FileService(cfg.getMasterFile());
    }

    public void run() {
        try {
            if (cfg.getServiceName().contains(SPLIT_SEQUENCE) || cfg.getServiceValue().contains(SPLIT_SEQUENCE)) {
                System.out.println("Service name/value is not supported");
                throw new RuntimeException("Invalid service name/value");
            }

            byte[] masterFileContent = fileService.readFileContent();
            String masterKey = cryptoService.formatKey(cfg.getPassword());

            switch (cfg.getMode()) {
                case ENCRYPT:
                    encryptFile(masterFileContent, masterKey);
                    break;
                case DECRYPT:
                    decryptFile(masterFileContent, masterKey);
                    break;
                case READ:
                    readService(masterFileContent, masterKey);
                    break;
                case ADD:
                    addService(masterFileContent, masterKey);
                    break;
                case DELETE:
                    deleteService(masterFileContent, masterKey);
                    break;
                case LIST:
                    listServices(masterFileContent, masterKey);
                    break;
            }
        }
        catch (Exception e) {
            //System.out.println(e.getClass().getSimpleName() + " - " + e.getMessage());
            System.out.println("Invalid input!");
        }
    }

    private void addService(byte[] text, String key) throws Exception {
        String data = new String(cryptoService.decryptText(text, key));
        List<String> lines = cryptoService.getContent(data);
        if (lines.size() > 0) {
            lines.add(String.format(NEW_LINE, cfg.getServiceName(), cfg.getServiceValue()));
        }

        byte[] newContent = cryptoService.convertToBytes(lines);
        byte[] encrypted = cryptoService.encryptText(newContent, key);

        fileService.storeNewMasterFile(encrypted);
    }

    private void deleteService(byte[] text, String key) throws Exception {
        String data = new String(cryptoService.decryptText(text, key));
        List<String> lines = cryptoService.getContent(data);
        if (lines.size() > 1) {
            lines = lines.stream().filter(l -> !l.split(SPLIT_SEQUENCE)[0].equals(cfg.getServiceName())).collect(Collectors.toList());
        }

        byte[] newContent = cryptoService.convertToBytes(lines);
        byte[] encrypted = cryptoService.encryptText(newContent, key);

        fileService.storeNewMasterFile(encrypted);
    }

    private void readService(byte[] text, String key) throws Exception {
        String data = new String(cryptoService.decryptText(text, key));
        cryptoService.getContent(data).stream().filter(l -> l.split(SPLIT_SEQUENCE)[0].equals(cfg.getServiceName())).forEach(System.out::println);
    }

    private void decryptFile(byte[] text, String key) throws Exception {
        byte[] decrypted = cryptoService.decryptText(text, key);
        fileService.storeNewMasterFile(decrypted);
    }

    private void encryptFile(byte[] text, String key) throws Exception {
        byte[] encrypted = cryptoService.encryptText(text, key);
        fileService.storeNewMasterFile(encrypted);
    }

    private void listServices(byte[] text, String key) throws Exception {
        String data = new String(cryptoService.decryptText(text, key));
        cryptoService.getContent(data).forEach(l -> System.out.println(l.split(SPLIT_SEQUENCE)[0]));
    }
}
