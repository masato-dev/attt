package view;
import javax.swing.*;

import algo.ClassicSymmetric;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class ClassicSymmetricView extends JPanel {

    // Main frame
    private JTextArea inputTextArea;
    private JTextArea outputTextArea;
    private JComboBox<String> algorithmComboBox;
    private JTextField keyField;

    public ClassicSymmetricView() {
        
        // Create panels
        JPanel inputPanel = new JPanel();
        JPanel outputPanel = new JPanel();
        JPanel controlPanel = new JPanel();
        
        // Input area
        inputTextArea = new JTextArea(5, 20);
        inputTextArea.setWrapStyleWord(true);
        inputTextArea.setLineWrap(true);
        JScrollPane inputScroll = new JScrollPane(inputTextArea);
        inputPanel.add(new JLabel("Input Text:"));
        inputPanel.add(inputScroll);

        // Output area
        outputTextArea = new JTextArea(5, 20);
        outputTextArea.setWrapStyleWord(true);
        outputTextArea.setLineWrap(true);
        outputTextArea.setEditable(false);
        JScrollPane outputScroll = new JScrollPane(outputTextArea);
        outputPanel.add(new JLabel("Output Text:"));
        outputPanel.add(outputScroll);

        // Control panel
        controlPanel.setLayout(new FlowLayout());
        controlPanel.add(new JLabel("Chọn thuật toán:"));
        String[] algorithms = {"Caesar", "Substitution", "Vigenere", "Affine", "Hill"};
        algorithmComboBox = new JComboBox<>(algorithms);
        controlPanel.add(algorithmComboBox);

        controlPanel.add(new JLabel("Key:"));
        keyField = new JTextField(10);
        controlPanel.add(keyField);
        
        JButton generateKeyButton = new JButton("Tạo key");
        JButton saveKeyButton = new JButton("Lưu key");
        JButton loadKeyButton = new JButton("Load key");
        JButton encryptButton = new JButton("Mã hoá");
        JButton decryptButton = new JButton("Giải mã");
        controlPanel.add(saveKeyButton);
        controlPanel.add(loadKeyButton);
        controlPanel.add(generateKeyButton);
        controlPanel.add(encryptButton);
        controlPanel.add(decryptButton);

        // Button actions
        encryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String algorithm = (String) algorithmComboBox.getSelectedItem();
                    String key = keyField.getText();
                    if (key.isEmpty()) {
                        JOptionPane.showMessageDialog(ClassicSymmetricView.this, "Chưa nhập key", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    String inputText = inputTextArea.getText();
                    if (inputText.isEmpty()) {
                        JOptionPane.showMessageDialog(ClassicSymmetricView.this, "Chưa nhập input", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    String encryptedText = encrypt(inputText, algorithm, key);
                    outputTextArea.setText(encryptedText);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(ClassicSymmetricView.this, "Lỗi: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        decryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String algorithm = (String) algorithmComboBox.getSelectedItem();
                    String key = keyField.getText();
                    if (key.isEmpty()) {
                        JOptionPane.showMessageDialog(ClassicSymmetricView.this, "Chưa nhập key", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    String inputText = inputTextArea.getText();
                    if (inputText.isEmpty()) {
                        JOptionPane.showMessageDialog(ClassicSymmetricView.this, "Chưa nhập input", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    String decryptedText = decrypt(inputText, algorithm, key);
                    outputTextArea.setText(decryptedText);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(ClassicSymmetricView.this, "Lỗi: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        generateKeyButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String algorithm = (String) algorithmComboBox.getSelectedItem();
                String key = generateKey(algorithm);
                keyField.setText(key);
            }
            
        });

        saveKeyButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String key = keyField.getText();
                if (key.isEmpty()) {
                    JOptionPane.showMessageDialog(ClassicSymmetricView.this, "Key field is empty", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Save Key to File");
                int userSelection = fileChooser.showSaveDialog(ClassicSymmetricView.this);

                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File fileToSave = fileChooser.getSelectedFile();
                    
                    if (!fileToSave.getName().endsWith(".txt")) {
                        fileToSave = new File(fileToSave.getAbsolutePath() + ".txt");
                    }

                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
                        writer.write(key);
                        JOptionPane.showMessageDialog(ClassicSymmetricView.this, "Key saved to file: " + fileToSave.getAbsolutePath());
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(ClassicSymmetricView.this, "Error saving key: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        loadKeyButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Load Key from File");
                int userSelection = fileChooser.showOpenDialog(ClassicSymmetricView.this);

                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File fileToOpen = fileChooser.getSelectedFile();
                    try (BufferedReader reader = new BufferedReader(new FileReader(fileToOpen))) {
                        StringBuilder keyBuilder = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            keyBuilder.append(line);
                        }
                        String loadedKey = keyBuilder.toString().trim();
                        if (!loadedKey.isEmpty()) {
                            keyField.setText(loadedKey);
                            JOptionPane.showMessageDialog(ClassicSymmetricView.this, "Đã load key từ file: " + fileToOpen.getAbsolutePath());
                        } else {
                            JOptionPane.showMessageDialog(ClassicSymmetricView.this, "File trống", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(ClassicSymmetricView.this, "Lỗi khi load file: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
            
        });

        // Add panels to the frame
        this.add(inputPanel, BorderLayout.NORTH);
        this.add(outputPanel, BorderLayout.CENTER);
        this.add(controlPanel, BorderLayout.SOUTH);
        
        this.setVisible(true);
    }

    private String generateKey(String algorithm) {
        switch (algorithm) {
            case "Caesar":
                return ClassicSymmetric.generateCaesarKey();
            case "Substitution":
                return ClassicSymmetric.generateSubstitutionKey();
            case "Vigenere":
                return ClassicSymmetric.generateVigenereKey(new Random().nextInt(120));
            case "Affine":
                return ClassicSymmetric.generateAffineKey();
            case "Hill":
                return ClassicSymmetric.generateHillKey();
            default:
                return "";
        }
    }

    // Method to encrypt text based on selected algorithm
    private String encrypt(String text, String algorithm, String key) {
        switch (algorithm) {
            case "Caesar":
                return ClassicSymmetric.caesarEncrypt(text, key);
            case "Substitution":
                return ClassicSymmetric.substitutionEncrypt(text, key);
            case "Vigenere":
                return ClassicSymmetric.vigenereEncrypt(text, key);
            case "Affine":
                return ClassicSymmetric.affineEncrypt(text, key.split(",")[0], key.split(",")[1]);
            case "Hill":
                return ClassicSymmetric.hillEncrypt(text, key);
            default:
                return text;
        }
    }

    // Method to decrypt text based on selected algorithm
    private String decrypt(String text, String algorithm, String key) {
        switch (algorithm) {
            case "Caesar":
                return ClassicSymmetric.caesarDecrypt(text, key);
            case "Substitution":
                return ClassicSymmetric.substitutionDecrypt(text, key);
            case "Vigenere":
                return ClassicSymmetric.vigenereDecrypt(text, key);
            case "Affine":
                return ClassicSymmetric.affineDecrypt(text, key.split(",")[0], key.split(",")[1]);
            case "Hill":
                return ClassicSymmetric.hillDecrypt(text, key);
            default:
                return text;
        }
    }   
}


