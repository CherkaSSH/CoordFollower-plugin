package org.look.at;

import net.minecraft.world.entity.player.Player;
import org.rusherhack.client.api.RusherHackAPI;

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


public class Utils {
    private static final String ALGORITHM = "Blowfish";
    private static final String KEY_GENERATION_ALGORITHM = "PBEWithMD5AndDES";

    public class fileUtils {
        public static void save(Object obj, String password, String filename) throws Exception {
            byte[] encrypted = encryption.encrypt(obj, password);
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
            return encryption.decrypt(encrypted, password);
        }
        public static Path getCoordsDirectory() {
            Path path = RusherHackAPI.getConfigPath().getParent().resolve("coords");
            if (!Files.exists(path)) {
                try {Files.createDirectories(path);}
                catch (IOException e)
                {throw new RuntimeException("Failed to create coords directory", e);}
            }
            return path;
        }
    }
    public static class encryption {
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
    public static class math {
        public double getXZDistanceToBlock(Player player, Coordinates coords) {
            double playerX = player.getX();
            double playerZ = player.getZ();
            double blockX = coords.getX();
            double blockZ = coords.getZ();

            double deltaX = blockX - playerX;
            double deltaZ = blockZ - playerZ;

            return Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
        }

        public float rotateX(Player player,Coordinates coord){
            double deltaX = coord.getX() - player.getX();
            double deltaZ = coord.getZ() - player.getZ();
            double yaw = Math.atan2(deltaZ, deltaX);
            yaw = Math.toDegrees(yaw);
            return (float) (yaw -= 90);

        }
    }
}
