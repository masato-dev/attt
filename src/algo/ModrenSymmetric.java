package algo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class ModrenSymmetric {

    // Hàm tạo key
    public static String generateKey(String algorithm, int keySize) {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance(algorithm);
            keyGen.init(keySize);
            SecretKey secretKey = keyGen.generateKey();
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
        } catch (Exception ex) {
            throw new RuntimeException("Error generating key", ex);
        }
    }

    // Hàm mã hóa văn bản
    public static String encryptText(String algorithm, String mode, String padding, String key, String plainText) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException {
        Cipher cipher = initializeCipher(algorithm, mode, padding, key, Cipher.ENCRYPT_MODE);
        byte[] iv = cipher.getIV();
        byte[] encrypted = cipher.doFinal(plainText.getBytes());
        String ivBase64 = (iv != null) ? Base64.getEncoder().encodeToString(iv) : "No IV";
        return Base64.getEncoder().encodeToString(encrypted) + "\nIV: " + ivBase64;
    }

    // Hàm giải mã văn bản
    public static String decryptText(String algorithm, String mode, String padding, String key, String cipherText) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException {
        String[] parts = cipherText.split("\nIV: ");
        byte[] encrypted = Base64.getDecoder().decode(parts[0]);
        byte[] iv = (parts.length > 1 && !parts[1].equals("No IV")) 
            ? Base64.getDecoder().decode(parts[1]) 
            : null;

        Cipher cipher = initializeCipher(algorithm, mode, padding, key, Cipher.DECRYPT_MODE, iv);
        byte[] original = cipher.doFinal(encrypted);
        return new String(original);
    }

    // Mã hóa file
    public static String encryptFile(String algorithm, String mode, String padding, String key, File file) {
        try {
            byte[] fileBytes = readFileBytes(file);
            Cipher cipher = initializeCipher(algorithm, mode, padding, key, Cipher.ENCRYPT_MODE);
            byte[] encryptedBytes = cipher.doFinal(fileBytes);
            String iv = (cipher.getIV() != null) 
                ? Base64.getEncoder().encodeToString(cipher.getIV()) 
                : "No IV";
            File outputFile = writeFileBytes("encrypted_" + file.getName(), encryptedBytes);
            return outputFile.getAbsolutePath() + "\nIV: " + iv;
        } catch (Exception ex) {
            throw new RuntimeException("Error encrypting file", ex);
        }
    }

    // Giải mã file
    public static String decryptFile(String algorithm, String mode, String padding, String key, File file, String ivBase64) {
        try {
            byte[] fileBytes = readFileBytes(file);
            byte[] iv = (!ivBase64.equals("No IV")) 
                ? Base64.getDecoder().decode(ivBase64) 
                : null;

            Cipher cipher = initializeCipher(algorithm, mode, padding, key, Cipher.DECRYPT_MODE, iv);
            byte[] decryptedBytes = cipher.doFinal(fileBytes);
            File outputFile = writeFileBytes("decrypted_" + file.getName(), decryptedBytes);
            return outputFile.getAbsolutePath();
        } catch (Exception ex) {
            throw new RuntimeException("Error decrypting file", ex);
        }
    }

    // Khởi tạo Cipher với chế độ và padding
    private static Cipher initializeCipher(String algorithm, String mode, String padding, String key, int cipherMode) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException {
        return initializeCipher(algorithm, mode, padding, key, cipherMode, null);
    }

    private static Cipher initializeCipher(String algorithm, String mode, String padding, String key, int cipherMode, byte[] iv) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
        byte[] keyBytes = Base64.getDecoder().decode(key);
        SecretKey secretKey = new SecretKeySpec(keyBytes, algorithm);
        String transformation = algorithm + 
                    (mode != null && !mode.isEmpty() ? "/" + mode : "/ECB") + 
                    (padding != null && !padding.isEmpty() ? "/" + padding : "/PKCS5Padding");

        Cipher cipher = Cipher.getInstance(transformation);

        if ("ECB".equalsIgnoreCase(mode) || mode == null || mode.isEmpty()) { // Nếu không nhập mode thì mặc định là ECB
            cipher.init(cipherMode, secretKey); // ECB không dùng IV
        } else {
            if (cipherMode == Cipher.ENCRYPT_MODE && iv == null) {
                iv = generateIV(cipher.getBlockSize());
            }
            cipher.init(cipherMode, secretKey, iv != null ? new IvParameterSpec(iv) : null);
        }
        return cipher;
    }

    // Sinh IV ngẫu nhiên
    private static byte[] generateIV(int blockSize) {
        byte[] iv = new byte[blockSize];
        new SecureRandom().nextBytes(iv);
        return iv;
    }

    // Đọc file thành byte array
    private static byte[] readFileBytes(File file) throws Exception {
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] fileBytes = new byte[(int) file.length()];
            fis.read(fileBytes);
            return fileBytes;
        }
    }

    // Ghi byte array thành file
    private static File writeFileBytes(String fileName, byte[] bytes) throws Exception {
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            fos.write(bytes);
            return new File(fileName);
        }
    }
}
