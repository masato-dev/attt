package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class HashView extends JPanel {
    private JRadioButton md5RadioButton, sha256RadioButton;
    private JTextField keyField, contentField;
    private JButton generateKeyButton, hashButton, chooseFileButton;
    private JTextArea hashResultArea;
    private JFileChooser fileChooser;
    private File selectedFile;

    public HashView() {
        // Set layout
        setLayout(new GridLayout(6, 2, 10, 10));

        // Initialize components
        md5RadioButton = new JRadioButton("MD5");
        sha256RadioButton = new JRadioButton("SHA-256");
        ButtonGroup algorithmGroup = new ButtonGroup();
        algorithmGroup.add(md5RadioButton);
        algorithmGroup.add(sha256RadioButton);

        keyField = new JTextField();
        contentField = new JTextField();
        generateKeyButton = new JButton("Tạo Key");
        hashButton = new JButton("Hash");
        chooseFileButton = new JButton("Chọn File");

        hashResultArea = new JTextArea(5, 30);
        hashResultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(hashResultArea);

        fileChooser = new JFileChooser();

        // Add components to panel
        add(new JLabel("Chọn Thuật Toán:"));
        add(md5RadioButton);
        add(new JLabel(""));
        add(sha256RadioButton);
        add(new JLabel("Nhập Khóa:"));
        add(keyField);
        add(new JLabel("Nhập Nội Dung:"));
        add(contentField);
        add(generateKeyButton);
        add(chooseFileButton);
        add(hashButton);
        add(scrollPane);

        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        // Set default selections
        md5RadioButton.setSelected(true);

        // Add action listeners
        generateKeyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateKey();
            }
        });

        chooseFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseFile();
            }
        });

        hashButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateHash();
            }
        });
    }

    private void generateKey() {
        // Tạo khóa ngẫu nhiên sử dụng ký tự trong bảng mã ASCII (từ 32 đến 126)
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder keyBuilder = new StringBuilder();
        int keyLength = 16;

        for (int i = 0; i < keyLength; i++) {
            // Sinh ngẫu nhiên một giá trị trong phạm vi từ 32 đến 126
            int randomInt = secureRandom.nextInt(95) + 32; // 95 là số lượng ký tự hợp lệ trong phạm vi ASCII (126 - 32
                                                           // + 1)
            keyBuilder.append((char) randomInt);
        }

        // Đặt giá trị khóa vào JTextField
        keyField.setText(keyBuilder.toString());
    }

    private void chooseFile() {
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            JOptionPane.showMessageDialog(this, "File đã chọn: " + selectedFile.getAbsolutePath());
        }
    }

    private void generateHash() {
        String algorithm = md5RadioButton.isSelected() ? "MD5" : "SHA-256";
        String key = keyField.getText();
        String content = contentField.getText();

        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.update((key + content).getBytes());
            byte[] hashedBytes = messageDigest.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashedBytes) {
                hexString.append(Integer.toHexString(0xFF & b));
            }
            hashResultArea.setText("Kết quả Hash (" + algorithm + "):\n" + hexString.toString());
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi hash: " + ex.getMessage(), "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
