package view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class AsymmetricView extends JPanel {
    private JComboBox<String> rsaKeyLengthComboBox;
    private JTextField contentField;
    private JButton generateKeyButton, viewKeyButton, loadPrivateKeyButton, encryptButton, decryptButton;
    private JTextArea resultArea, keyArea;
    private JFileChooser fileChooser;
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public AsymmetricView() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Thêm khoảng cách giữa các component

        // Initialize components
        rsaKeyLengthComboBox = new JComboBox<>(new String[] { "1024", "2048", "4096" });
        keyArea = new JTextArea(5, 30);
        keyArea.setLineWrap(true);
        contentField = new JTextField();
        generateKeyButton = new JButton("Tạo Khóa");
        viewKeyButton = new JButton("Xem Khóa");
        loadPrivateKeyButton = new JButton("Tải private key");
        encryptButton = new JButton("Mã Hóa");
        decryptButton = new JButton("Giải Mã");
        resultArea = new JTextArea(5, 30);
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        fileChooser = new JFileChooser();

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
        add(new JLabel("Nhập Khóa:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL; // Dàn trải toàn bộ chiều ngang
        add(keyArea, gbc);

        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(generateKeyButton, gbc);

        // Xem Khóa và Load private key
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        add(viewKeyButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(loadPrivateKeyButton, gbc);

        // Nhập Nội Dung
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        add(new JLabel("Nhập Nội Dung:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(contentField, gbc);

        // Mã Hóa và Giải Mã
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        add(encryptButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        add(decryptButton, gbc);

        // Kết quả Giải Mã
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(scrollPane, gbc);

        // Add action listeners
        generateKeyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateKey();
            }
        });

        viewKeyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewKey();
            }
        });

        loadPrivateKeyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadPrivateKey();
            }
        });

        encryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                encryptContent();
            }
        });

        decryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                decryptContent();
            }
        });
    }

    private void generateKey() {
        try {
            int keyLength = Integer.parseInt((String) rsaKeyLengthComboBox.getSelectedItem());
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(keyLength);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            publicKey = keyPair.getPublic();
            privateKey = keyPair.getPrivate();
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Lưu Private Key");
            int userSelection = fileChooser.showSaveDialog(this);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileToSave))) {
                    oos.writeObject(privateKey);
                    resultArea.setText("Private key đã được lưu thành công.");
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, "Lỗi lưu private key: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
            keyArea.setText(publicKey.toString());
            resultArea.setText("Khóa RSA đã được tạo.");
        } catch (NoSuchAlgorithmException e) {
            JOptionPane.showMessageDialog(this, "Lỗi tạo khóa: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewKey() {
        if (publicKey != null) {
            keyArea.setText(publicKey.toString());
        } else {
            JOptionPane.showMessageDialog(this, "Chưa có khóa công khai.", "Thông báo", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void loadPrivateKey() {
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(selectedFile))) {
                privateKey = (PrivateKey) ois.readObject();
                resultArea.setText("private key đã được tải.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi tải khóa: " + e.getMessage(), "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void encryptContent() {
        try {
            if (publicKey == null) {
                JOptionPane.showMessageDialog(this, "Chưa có khóa công khai để mã hóa.", "Thông báo",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            String content = contentField.getText();
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encryptedBytes = cipher.doFinal(content.getBytes());
            resultArea.setText("Kết quả Mã Hóa: \n" + bytesToHex(encryptedBytes));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi mã hóa: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void decryptContent() {
        try {
            if (privateKey == null) {
                JOptionPane.showMessageDialog(this, "Chưa có private key để giải mã.", "Thông báo",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            String content = contentField.getText();
            byte[] encryptedBytes = hexToBytes(content);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            resultArea.setText("Kết quả Giải Mã: \n" + new String(decryptedBytes));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi giải mã: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(Integer.toHexString(0xFF & b));
        }
        return hexString.toString();
    }

    private byte[] hexToBytes(String hex) {
        int length = hex.length();
        byte[] bytes = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            bytes[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return bytes;
    }
}