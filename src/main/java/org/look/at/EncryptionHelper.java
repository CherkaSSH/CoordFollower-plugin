package org.look.at;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import static org.look.at.CoordFollowerModule.getCoordsDirectory;

public class EncryptionHelper {
    private static final String ALGORITHM = "Blowfish";
    private static final String KEY_GENERATION_ALGORITHM = "PBEWithMD5AndDES";

    public static void save(Object obj, String password, String filename) throws Exception {
        byte[] encrypted = encrypt(obj, password);
        Path path = getCoordsDirectory().resolve(filename);
        Files.createDirectories(path.getParent());
        FileOutputStream fileOut = new FileOutputStream(path.toFile());
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(encrypted);
        out.close();
        fileOut.close();
    }

    public static Object load(String password, String filename) throws Exception {
        Path path = getCoordsDirectory().resolve(filename);
        FileInputStream fileIn = new FileInputStream(path.toFile());
        ObjectInputStream in = new ObjectInputStream(fileIn);
        byte[] encrypted = (byte[]) in.readObject();
        in.close();
        fileIn.close();
        return decrypt(encrypted, password);
    }

    public static byte[] encrypt(Object obj, String password) throws Exception {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(obj);
        out.close();
        byte[] bytes = byteOut.toByteArray();

        SecretKey secretKey = getSecretKey(password);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return cipher.doFinal(bytes);
    }

    public static Object decrypt(byte[] encrypted, String password) throws Exception {
        SecretKey secretKey = getSecretKey(password);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return cipher.doFinal(encrypted);
    }

    private static SecretKey getSecretKey(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance(KEY_GENERATION_ALGORITHM);
        KeySpec spec = new PBEKeySpec(password.toCharArray());
        SecretKey tmp = factory.generateSecret(spec);
        return new SecretKeySpec(tmp.getEncoded(), ALGORITHM);
    }
}
