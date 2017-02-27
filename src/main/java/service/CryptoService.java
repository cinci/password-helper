package service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jcincera on 01/12/15.
 */
public final class CryptoService {
    private static final String CIPHER_TYPE = "AES/CBC/PKCS5PADDING";
    private static final String CHARSET = "UTF-8";
    private static final String SECRET_KEY_SPEC = "AES";
    private static final String INIT_VECTOR = "AajSHFE343fjfeaf";

    private final String fileContentValidator;

    public CryptoService(String fileContentValidator) {
        this.fileContentValidator = fileContentValidator;
    }

    public byte[] encryptText(byte[] text, String key) throws Exception {
        IvParameterSpec iv = new IvParameterSpec(INIT_VECTOR.getBytes(CHARSET));
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(CHARSET), SECRET_KEY_SPEC);

        Cipher cipher = Cipher.getInstance(CIPHER_TYPE);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);

        return cipher.doFinal(text);
    }

    public byte[] decryptText(byte[] text, String key) throws Exception {
        IvParameterSpec iv = new IvParameterSpec(INIT_VECTOR.getBytes(CHARSET));
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(CHARSET), SECRET_KEY_SPEC);

        Cipher cipher = Cipher.getInstance(CIPHER_TYPE);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);

        return cipher.doFinal(text);
    }

    public byte[] convertToBytes(List<String> lines) throws IOException {
        StringBuffer b = new StringBuffer();
        lines.forEach(l -> b.append(l).append("\n"));

        return b.toString().getBytes();
    }

    public List<String> getContent(String data) {
        if (!data.startsWith(fileContentValidator)) {
            throw new RuntimeException("Invalid file content!");
        }

        String[] lines = data.split("\n");
        return new ArrayList<>(Arrays.asList(lines));
    }

    public String formatKey(String key) {
        String newKey = key + key + key;
        if (newKey.length() < 16) {
            throw new RuntimeException("Password is too short!");
        }
        newKey = newKey.substring(0, 16);
        return newKey;
    }
}
