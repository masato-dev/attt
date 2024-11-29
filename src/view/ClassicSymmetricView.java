package view;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import controller.ClassicSymmetricController;
import enums.ClassicSymmetricAction;

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
        encryptButton.addActionListener(new ClassicSymmetricController(this, ClassicSymmetricAction.Encrypt));
        decryptButton.addActionListener(new ClassicSymmetricController(this, ClassicSymmetricAction.Decrypt));
        generateKeyButton.addActionListener(new ClassicSymmetricController(this, ClassicSymmetricAction.GenerateKey));
        saveKeyButton.addActionListener(new ClassicSymmetricController(this, ClassicSymmetricAction.SaveKey));
        loadKeyButton.addActionListener(new ClassicSymmetricController(this, ClassicSymmetricAction.LoadKey));

        // Add panels to the frame
        this.add(inputPanel, BorderLayout.NORTH);
        this.add(outputPanel, BorderLayout.CENTER);
        this.add(controlPanel, BorderLayout.SOUTH);
        
        this.setVisible(true);
    }

    public JTextArea getInputTextArea() {
        return inputTextArea;
    }

    public void setInputTextArea(JTextArea inputTextArea) {
        this.inputTextArea = inputTextArea;
    }

    public JTextArea getOutputTextArea() {
        return outputTextArea;
    }

    public void setOutputTextArea(JTextArea outputTextArea) {
        this.outputTextArea = outputTextArea;
    }

    public JComboBox<String> getAlgorithmComboBox() {
        return algorithmComboBox;
    }

    public void setAlgorithmComboBox(JComboBox<String> algorithmComboBox) {
        this.algorithmComboBox = algorithmComboBox;
    }

    public JTextField getKeyField() {
        return keyField;
    }

    public void setKeyField(JTextField keyField) {
        this.keyField = keyField;
    }   
}


