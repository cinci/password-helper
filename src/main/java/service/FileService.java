package service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by jcincera on 01/12/15.
 */
public final class FileService {

    private final String masterFile;

    public FileService(String masterFile) {
        this.masterFile = masterFile;
    }

    public void storeNewMasterFile(byte[] data) throws IOException, InterruptedException {
        Thread.sleep(100);
        Files.move(Paths.get(masterFile), Paths.get(masterFile + "_" + System.currentTimeMillis()));
        Files.write(Paths.get(masterFile), data);
    }

    public void backupMasterFile() throws IOException, InterruptedException {
        Thread.sleep(100);
        Files.copy(Paths.get(masterFile), Paths.get(masterFile + "_" + System.currentTimeMillis()));
    }

    public byte[] readFileContent() {
        byte[] result;

        try {
            result = Files.readAllBytes(Paths.get(masterFile));
        }
        catch (IOException e) {
            throw new RuntimeException("Unable to read file: " + masterFile);
        }

        if (result == null) {
            throw new RuntimeException("Unable to read file: " + masterFile);
        }

        return result;
    }
}
