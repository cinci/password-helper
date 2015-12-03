package service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jcincera on 01/12/15.
 */
public final class CryptService {
    private static final String CIPHER_ALGORITHM = "AES";

    private final String fileContentValidator;

    public CryptService(String fileContentValidator) {
        this.fileContentValidator = fileContentValidator;
    }

    public byte[] encryptText(byte[] text, String key) throws Exception {
        Key aesKey = new SecretKeySpec(key.getBytes(), CIPHER_ALGORITHM);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        return cipher.doFinal(text);
    }

    public byte[] decryptText(byte[] text, String key) throws Exception {
        Key aesKey = new SecretKeySpec(key.getBytes(), CIPHER_ALGORITHM);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, aesKey);
        return cipher.doFinal(text);
    }

    public byte[] convertToBytes(List<String> lines) throws IOException {
        StringBuffer b = new StringBuffer();
        lines.stream().forEach(l -> b.append(l).append("\n"));

        return b.toString().getBytes();
    }

    public List<String> getXLines(String data) {
        if (!data.startsWith(fileContentValidator)) {
            throw new RuntimeException("Invalid file content!");
        }

        String[] lines = data.split("\n");
        return new ArrayList<>(Arrays.asList(lines));
    }

    public String formatKey(String key) {
        String newKey = key + key + key;
        if (newKey.length() < 16) {
            throw new RuntimeException("Too short password!");
        }
        newKey = newKey.substring(0, 16);
        return newKey;
    }
}
