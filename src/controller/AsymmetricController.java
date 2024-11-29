package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import enums.AsymmetricAction;
import view.AsymmetricView;

public class AsymmetricController implements ActionListener {
    private AsymmetricView view;

    private AsymmetricAction action;

    static class KeyHolder {
        public static PublicKey publicKey;
        public static PrivateKey privateKey;
    }


    public AsymmetricController(AsymmetricView view, AsymmetricAction action) {
        this.view = view;
        this.action = action;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (this.action) {
            case GenerateKey:
                doGenerateKey();
                break;
            case Encrypt:
                doEncrypt();
                break;
            case Decrypt:
                doDecrypt();
                break;
            case SavePublicKey:
                doSavePublicKey();
                break;
            case SavePrivateKey:
                doSavePrivateKey();
                break;
            case LoadPublicKey:
                doLoadPublicKey();
                break;
            case LoadPrivateKey:
                doLoadPrivateKey();
                break;
            default:
                break;
        }
    }

    private void doGenerateKey() {
        try {
            int keyLength = Integer.parseInt((String) view.getRsaKeyLengthComboBox().getSelectedItem());
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(keyLength);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            KeyHolder.publicKey = keyPair.getPublic();
            KeyHolder.privateKey = keyPair.getPrivate();

            view.getPublicKeyArea().setText(bytesToHex(KeyHolder.publicKey.getEncoded()));
            view.getPrivateKeyArea().setText(bytesToHex(KeyHolder.privateKey.getEncoded()));
            JOptionPane.showMessageDialog(null, "Khóa RSA đã được tạo thành công!", "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Lỗi khi tạo khóa: " + ex.getMessage(), "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void doEncrypt() {
        try {
            if (KeyHolder.publicKey == null) {
                JOptionPane.showMessageDialog(null, "Chưa có khóa công khai để mã hóa.", "Thông báo",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            String content = view.getContentArea().getText();
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, KeyHolder.publicKey);
            byte[] encryptedBytes = cipher.doFinal(content.getBytes());
            view.getResultArea().setText(bytesToHex(encryptedBytes));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Lỗi mã hóa: " + ex.getMessage(), "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void doDecrypt() {
        try {
            if (KeyHolder.privateKey == null) {
                JOptionPane.showMessageDialog(null, "Chưa có private key để giải mã.", "Thông báo",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            String content = view.getContentArea().getText();
            byte[] encryptedBytes = hexToBytes(content);
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, KeyHolder.privateKey);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            view.getResultArea().setText(new String(decryptedBytes));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Lỗi giải mã: " + ex.getMessage(), "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void doSavePublicKey() {
        if (KeyHolder.publicKey == null) {
            JOptionPane.showMessageDialog(null, "Chưa có public key để lưu.", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Lưu Public Key");
        int userSelection = fileChooser.showSaveDialog(view);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            if (!fileToSave.getName().endsWith(".pub")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".pub");
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
                writer.write(bytesToHex(KeyHolder.publicKey.getEncoded()));
                JOptionPane.showMessageDialog(null, "Public key đã được lưu vào file: " + fileToSave.getAbsolutePath(),
                        "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Có lỗi xảy ra khi lưu public key: " + ex.getMessage(), "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void doSavePrivateKey() {
        if (KeyHolder.privateKey == null) {
            JOptionPane.showMessageDialog(null, "Chưa có private key để lưu.", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Lưu Private Key");
        int userSelection = fileChooser.showSaveDialog(view);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            if (!fileToSave.getName().endsWith(".pem")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".pem");
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
                writer.write("-----BEGIN RSA PRIVATE KEY-----\n");
                writer.write(bytesToHex(KeyHolder.privateKey.getEncoded()));
                writer.write("\n-----END RSA PRIVATE KEY-----\n");
                JOptionPane.showMessageDialog(null, "Private key đã được lưu vào file: " + fileToSave.getAbsolutePath(),
                        "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Có lỗi xảy ra khi lưu private key: " + ex.getMessage(), "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void doLoadPublicKey() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn Public Key để tải");

        int userSelection = fileChooser.showOpenDialog(view);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToOpen = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(fileToOpen))) {
                StringBuilder hexString = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    hexString.append(line.trim());
                }

                byte[] publicKeyBytes = hexToBytes(hexString.toString());
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
                KeyHolder.publicKey = keyFactory.generatePublic(keySpec);
                view.getPublicKeyArea().setText(bytesToHex(KeyHolder.publicKey.getEncoded()));

                JOptionPane.showMessageDialog(null, "Public key đã được tải thành công.", "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Lỗi khi tải public key: " + ex.getMessage(), "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void doLoadPrivateKey() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn Private Key để tải");

        int userSelection = fileChooser.showOpenDialog(view);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToOpen = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(fileToOpen))) {
                StringBuilder pemString = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    pemString.append(line.trim());
                }

                // Xử lý PEM format
                String privateKeyContent = pemString.toString();
                privateKeyContent = privateKeyContent.replace("-----BEGIN RSA PRIVATE KEY-----", "")
                        .replace("-----END RSA PRIVATE KEY-----", "")
                        .replace("\n", "").replace("\r", "");

                byte[] privateKeyBytes = hexToBytes(privateKeyContent);
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
                KeyHolder.privateKey = keyFactory.generatePrivate(keySpec);
                view.getPrivateKeyArea().setText(bytesToHex(KeyHolder.privateKey.getEncoded()));

                JOptionPane.showMessageDialog(null, "Private key đã được tải thành công.", "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Lỗi khi tải private key: " + ex.getMessage(), "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }

    private byte[] hexToBytes(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }

    public void setView(AsymmetricView view) {
        this.view = view;
    }

    public void setAction(AsymmetricAction action) {
        this.action = action;
    }

}

