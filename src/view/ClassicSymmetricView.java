package view;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        
        JButton encryptButton = new JButton("Mã hoá");
        JButton decryptButton = new JButton("Giải mã");
        controlPanel.add(encryptButton);
        controlPanel.add(decryptButton);

        // Button actions
        encryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String algorithm = (String) algorithmComboBox.getSelectedItem();
                String key = keyField.getText();
                String inputText = inputTextArea.getText();
                String encryptedText = encrypt(inputText, algorithm, key);
                outputTextArea.setText(encryptedText);
            }
        });

        decryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String algorithm = (String) algorithmComboBox.getSelectedItem();
                String key = keyField.getText();
                String inputText = inputTextArea.getText();
                String decryptedText = decrypt(inputText, algorithm, key);
                outputTextArea.setText(decryptedText);
            }
        });

        // Add panels to the frame
        this.add(inputPanel, BorderLayout.NORTH);
        this.add(outputPanel, BorderLayout.CENTER);
        this.add(controlPanel, BorderLayout.SOUTH);
        
        this.setVisible(true);
    }

    // Method to encrypt text based on selected algorithm
    private String encrypt(String text, String algorithm, String key) {
        switch (algorithm) {
            case "Caesar":
                return caesarCipher(text, Integer.parseInt(key));
            case "Substitution":
                return substitutionCipher(text, key);
            case "Vigenere":
                return vigenereCipher(text, key, true);
            case "Affine":
                return affineCipher(text, Integer.parseInt(key.split(",")[0]), Integer.parseInt(key.split(",")[1]));
            case "Hill":
                return hillCipher(text, key);
            default:
                return text;
        }
    }

    // Method to decrypt text based on selected algorithm
    private String decrypt(String text, String algorithm, String key) {
        switch (algorithm) {
            case "Caesar":
                return caesarCipher(text, -Integer.parseInt(key));
            case "Substitution":
                return substitutionCipher(text, key); // You need a reverse mapping for decryption
            case "Vigenere":
                return vigenereCipher(text, key, false);
            case "Affine":
                return affineCipher(text, Integer.parseInt(key.split(",")[0]), Integer.parseInt(key.split(",")[1])); // You need inverse logic for decryption
            case "Hill":
                return hillCipher(text, key); // Reverse Hill cipher needs to be implemented
            default:
                return text;
        }
    }

    // Caesar Cipher
    private String caesarCipher(String text, int shift) {
        StringBuilder result = new StringBuilder();
        for (char character : text.toCharArray()) {
            if (Character.isLetter(character)) {
                char base = (Character.isLowerCase(character)) ? 'a' : 'A';
                result.append((char) ((character - base + shift) % 26 + base));
            } else {
                result.append(character);
            }
        }
        return result.toString();
    }

    // Substitution Cipher (basic example, assumes key is a string of alphabet letters)
    private String substitutionCipher(String text, String key) {
        String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder result = new StringBuilder();
        for (char character : text.toCharArray()) {
            int index = alphabet.indexOf(character);
            if (index != -1) {
                result.append(key.charAt(index));
            } else {
                result.append(character);
            }
        }
        return result.toString();
    }

    // Vigenere Cipher
    private String vigenereCipher(String text, String key, boolean encrypt) {
        StringBuilder result = new StringBuilder();
        key = key.toLowerCase();
        int keyIndex = 0;
        for (char character : text.toCharArray()) {
            if (Character.isLetter(character)) {
                char base = (Character.isLowerCase(character)) ? 'a' : 'A';
                int shift = key.charAt(keyIndex % key.length()) - 'a';
                if (!encrypt) {
                    shift = -shift;
                }
                result.append((char) ((character - base + shift + 26) % 26 + base));
                keyIndex++;
            } else {
                result.append(character);
            }
        }
        return result.toString();
    }

    // Affine Cipher
    private String affineCipher(String text, int a, int b) {
        StringBuilder result = new StringBuilder();
        for (char character : text.toCharArray()) {
            if (Character.isLetter(character)) {
                char base = (Character.isLowerCase(character)) ? 'a' : 'A';
                result.append((char) (((a * (character - base) + b) % 26) + base));
            } else {
                result.append(character);
            }
        }
        return result.toString();
    }

    // Hill Cipher
    private String hillCipher(String text, String key) {
        // Implement Hill cipher (needs a 2x2 or 3x3 matrix for encryption)
        return text; // Placeholder for actual Hill cipher implementation
    }
}
