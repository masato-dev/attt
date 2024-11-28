package view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class AsymmetricView extends JPanel {
    private JComboBox<String> rsaKeyLengthComboBox;
    private JTextArea contentArea;
    private JButton generateKeyButton, loadPublicKeyButton, loadPrivateKeyButton, encryptButton, decryptButton, savePublicKeyBtn, savePrivateKeyBtn;
    private JTextArea resultArea, publicKeyArea, privateKeyArea;
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public AsymmetricView() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Thêm khoảng cách giữa các component

        // Initialize components
        rsaKeyLengthComboBox = new JComboBox<>(new String[] { "1024", "2048", "4096" });

        publicKeyArea = new JTextArea(5, 30);
        publicKeyArea.setLineWrap(true);
        publicKeyArea.setWrapStyleWord(true);
        publicKeyArea.setCaretPosition(0);

        privateKeyArea = new JTextArea(5, 30);
        privateKeyArea.setLineWrap(true);
        privateKeyArea.setWrapStyleWord(true);
        privateKeyArea.setCaretPosition(0);

        contentArea = new JTextArea(5, 30);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setCaretPosition(0);

        generateKeyButton = new JButton("Tạo Khóa");
        loadPublicKeyButton = new JButton("Load Public Key");
        loadPrivateKeyButton = new JButton("Load private key");
        encryptButton = new JButton("Mã Hóa");
        decryptButton = new JButton("Giải Mã");
        savePublicKeyBtn = new JButton("Lưu public key");
        savePrivateKeyBtn = new JButton("Lưu private key");
        resultArea = new JTextArea(5, 30);
        resultArea.setEditable(false);
        resultArea.setLineWrap(true); // Cho phép kết quả hiển thị xuống hàng
        JScrollPane scrollPane = new JScrollPane(resultArea);

        // Chọn chiều dài RSA
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Chọn Chiều Dài RSA:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Cả ComboBox và Label đều chiếm 2 cột
        gbc.fill = GridBagConstraints.HORIZONTAL; // Dàn trải toàn bộ chiều ngang
        add(rsaKeyLengthComboBox, gbc);

        // Nhập Key và Button
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(new JLabel("Nhập Public Key:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL; // Dàn trải toàn bộ chiều ngang
        add(new JScrollPane(publicKeyArea), gbc); // Thêm JScrollPane để có thanh cuộn nếu khóa quá dài

        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(savePublicKeyBtn, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        add(new JLabel("Nhập Private Key:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL; // Dàn trải toàn bộ chiều ngang
        add(new JScrollPane(privateKeyArea), gbc); // Thêm JScrollPane để có thanh cuộn nếu khóa quá dài

        gbc.gridx = 3;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        add(savePrivateKeyBtn, gbc);

        // Xem Khóa và Load private key
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        add(generateKeyButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        add(loadPublicKeyButton, gbc);

        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(loadPrivateKeyButton, gbc);

        // Nhập Nội Dung
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        add(new JLabel("Nhập Nội Dung:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(contentArea, gbc);

        // Mã Hóa và Giải Mã
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        add(encryptButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        add(decryptButton, gbc);

        // Kết quả Giải Mã
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(scrollPane, gbc);

        // Add action listeners
        generateKeyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int keyLength = Integer.parseInt((String) rsaKeyLengthComboBox.getSelectedItem());
                    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
                    keyPairGenerator.initialize(keyLength);
                    KeyPair keyPair = keyPairGenerator.generateKeyPair();

                    publicKey = keyPair.getPublic();
                    privateKey = keyPair.getPrivate();

                    publicKeyArea.setText(bytesToHex(publicKey.getEncoded()));
                    privateKeyArea.setText(bytesToHex(privateKey.getEncoded()));
                    JOptionPane.showMessageDialog(null, "Khóa RSA đã được tạo thành công!", "Thông báo",
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Lỗi khi tạo khóa: " + ex.getMessage(), "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        encryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (publicKey == null) {
                        JOptionPane.showMessageDialog(null, "Chưa có khóa công khai để mã hóa.", "Thông báo",
                                JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    String content = contentArea.getText();
                    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                    cipher.init(Cipher.ENCRYPT_MODE, publicKey);
                    byte[] encryptedBytes = cipher.doFinal(content.getBytes());
                    resultArea.setText(bytesToHex(encryptedBytes));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Lỗi mã hóa: " + ex.getMessage(), "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        decryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (privateKey == null) {
                        JOptionPane.showMessageDialog(null, "Chưa có private key để giải mã.", "Thông báo",
                                JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    String content = contentArea.getText();
                    byte[] encryptedBytes = hexToBytes(content);
                    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                    cipher.init(Cipher.DECRYPT_MODE, privateKey);
                    byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
                    resultArea.setText(new String(decryptedBytes));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Lỗi giải mã: " + ex.getMessage(), "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        savePublicKeyBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (publicKey == null) {
                    JOptionPane.showMessageDialog(null, "Chưa có public key để lưu.", "Thông báo",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Lưu Public Key");
                int userSelection = fileChooser.showSaveDialog(AsymmetricView.this);

                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File fileToSave = fileChooser.getSelectedFile();
                    if (!fileToSave.getName().endsWith(".pub")) {
                        fileToSave = new File(fileToSave.getAbsolutePath() + ".pub");
                    }

                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
                        writer.write(bytesToHex(publicKey.getEncoded()));
                        JOptionPane.showMessageDialog(null, "Public key đã được lưu vào file: " + fileToSave.getAbsolutePath(), "Thông báo",
                                JOptionPane.INFORMATION_MESSAGE);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Có lỗi xảy ra khi lưu public key: " + ex.getMessage(), "Lỗi",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
            
        });

        savePrivateKeyBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (privateKey == null) {
                    JOptionPane.showMessageDialog(null, "Chưa có private key để lưu.", "Thông báo",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Lưu Private Key");
                int userSelection = fileChooser.showSaveDialog(AsymmetricView.this);

                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File fileToSave = fileChooser.getSelectedFile();
                    if (!fileToSave.getName().endsWith(".pem")) {
                        fileToSave = new File(fileToSave.getAbsolutePath() + ".pem");
                    }

                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
                        writer.write("-----BEGIN RSA PRIVATE KEY-----\n");
                        writer.write(bytesToHex(privateKey.getEncoded()));
                        writer.write("\n-----END RSA PRIVATE KEY-----\n");
                        JOptionPane.showMessageDialog(null, "Private key đã được lưu vào file: " + fileToSave.getAbsolutePath(), "Thông báo",
                                JOptionPane.INFORMATION_MESSAGE);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Có lỗi xảy ra khi lưu private key: " + ex.getMessage(), "Lỗi",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }            
        });

        loadPublicKeyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Chọn Public Key để tải");
        
                int userSelection = fileChooser.showOpenDialog(AsymmetricView.this);
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
                        publicKey = keyFactory.generatePublic(keySpec);
                        publicKeyArea.setText(bytesToHex(publicKey.getEncoded()));
        
                        JOptionPane.showMessageDialog(null, "Public key đã được tải thành công.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Lỗi khi tải public key: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        
        loadPrivateKeyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Chọn Private Key để tải");
        
                int userSelection = fileChooser.showOpenDialog(AsymmetricView.this);
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
                        privateKey = keyFactory.generatePrivate(keySpec);
                        privateKeyArea.setText(bytesToHex(privateKey.getEncoded()));
        
                        JOptionPane.showMessageDialog(null, "Private key đã được tải thành công.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Lỗi khi tải private key: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
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

}