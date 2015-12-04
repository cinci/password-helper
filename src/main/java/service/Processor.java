package service;

import configuration.Configuration;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jcincera on 01/12/15.
 */
public class Processor {

    private static final String FILE_CONTENT_VALIDATOR = "===OK===";
    private static final String DIVIDER = " ... ";
    private static final String NEW_LINE = "%s" + DIVIDER + "%s";

    private Configuration cfg;
    private CryptService cryptService;
    private FileService fileService;

    public Processor(Configuration cfg) {
        this.cfg = cfg;
        cryptService = new CryptService(FILE_CONTENT_VALIDATOR);
        fileService = new FileService(cfg.getMasterFile());
    }

    public void run() {
        try {
            byte[] masterFileContent = fileService.readFileContent();
            String masterKey = cryptService.formatKey(cfg.getPassword());

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
            }
        }
        catch (Exception e) {
            //System.out.println(e.getClass().getSimpleName() + " - " + e.getMessage());
            System.out.println("Invalid!");
            System.exit(1);
        }
    }

    private void addService(byte[] text, String key) throws Exception {
        fileService.backupMasterFile();

        String data = new String(cryptService.decryptText(text, key));
        List<String> lines = cryptService.getContent(data);
        if (lines.size() > 0) {
            lines.add(String.format(NEW_LINE, cfg.getServiceName(), cfg.getServiceValue()));
        }

        byte[] newContent = cryptService.convertToBytes(lines);
        byte[] encrypted = cryptService.encryptText(newContent, key);

        fileService.storeNewMasterFile(encrypted);
    }

    private void deleteService(byte[] text, String key) throws Exception {
        fileService.backupMasterFile();

        String data = new String(cryptService.decryptText(text, key));
        List<String> lines = cryptService.getContent(data);
        if (lines.size() > 1) {
            lines = lines.stream().filter(l -> !l.split(DIVIDER)[0].equals(cfg.getServiceName())).collect(Collectors.toList());
        }

        byte[] newContent = cryptService.convertToBytes(lines);
        byte[] encrypted = cryptService.encryptText(newContent, key);

        fileService.storeNewMasterFile(encrypted);
    }

    private void readService(byte[] text, String key) throws Exception {
        String data = new String(cryptService.decryptText(text, key));
        cryptService.getContent(data).stream().filter(l -> l.split(DIVIDER)[0].equals(cfg.getServiceName())).forEach(System.out::println);
    }

    private void decryptFile(byte[] text, String key) throws Exception {
        fileService.backupMasterFile();
        byte[] decrypted = cryptService.decryptText(text, key);
        fileService.storeNewMasterFile(decrypted);
    }

    private void encryptFile(byte[] text, String key) throws Exception {
        fileService.backupMasterFile();
        byte[] encrypted = cryptService.encryptText(text, key);
        fileService.storeNewMasterFile(encrypted);
    }
}
