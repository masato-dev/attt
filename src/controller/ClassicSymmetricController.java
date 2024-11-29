package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import algo.ClassicSymmetric;
import enums.ClassicSymmetricAction;
import view.ClassicSymmetricView;

public class ClassicSymmetricController implements ActionListener {
    private ClassicSymmetricView view;
    private ClassicSymmetricAction action;

    public ClassicSymmetricController(ClassicSymmetricView view, ClassicSymmetricAction action) {
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
            case SaveKey:
                doSaveKey();
                break;
            case LoadKey:
                doLoadKey();
                break;
            default:
                break;
        }
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

    private void doGenerateKey() {
        String algorithm = (String) view.getAlgorithmComboBox().getSelectedItem();
        String key = generateKey(algorithm);
        view.getKeyField().setText(key);
    }

    private void doEncrypt() {
        try {
            String algorithm = (String) view.getAlgorithmComboBox().getSelectedItem();
            String key = view.getKeyField().getText();
            if (key.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Chưa nhập key", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String inputText = view.getInputTextArea().getText();
            if (inputText.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Chưa nhập input", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String encryptedText = encrypt(inputText, algorithm, key);
            view.getOutputTextArea().setText(encryptedText);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Lỗi: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void doDecrypt() {
        try {
            String algorithm = (String) view.getAlgorithmComboBox().getSelectedItem();
            String key = view.getKeyField().getText();
            if (key.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Chưa nhập key", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            String inputText = view.getInputTextArea().getText();
            if (inputText.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Chưa nhập input", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            String decryptedText = decrypt(inputText, algorithm, key);
            view.getOutputTextArea().setText(decryptedText);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Lỗi: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void doSaveKey() {
        String key = view.getKeyField().getText();
        if (key.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Key field is empty", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Key to File");
        int userSelection = fileChooser.showSaveDialog(view);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            if (!fileToSave.getName().endsWith(".txt")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".txt");
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
                writer.write(key);
                JOptionPane.showMessageDialog(view,
                        "Key saved to file: " + fileToSave.getAbsolutePath());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(view, "Error saving key: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void doLoadKey() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Load Key from File");
        int userSelection = fileChooser.showOpenDialog(view);

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
                    view.getKeyField().setText(loadedKey);
                    JOptionPane.showMessageDialog(view,
                            "Đã load key từ file: " + fileToOpen.getAbsolutePath());
                } else {
                    JOptionPane.showMessageDialog(view, "File trống", "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(view, "Lỗi khi load file: " + ex.getMessage(), "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}
