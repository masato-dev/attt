package view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class HashView extends JPanel {
    private JRadioButton md5RadioButton, sha256RadioButton;
    private JTextField contentField;
    private JButton hashButton, chooseFileButton, hashFileButton;
    private JTextArea hashResultArea;
    private JFileChooser fileChooser;
    private File selectedFile;

    public HashView() {
        // Set layout
        setLayout(new GridBagLayout());

        // Initialize components
        md5RadioButton = new JRadioButton("MD5");
        sha256RadioButton = new JRadioButton("SHA-256");
        ButtonGroup algorithmGroup = new ButtonGroup();
        algorithmGroup.add(md5RadioButton);
        algorithmGroup.add(sha256RadioButton);

        contentField = new JTextField();
        hashButton = new JButton("Hash");
        hashFileButton = new JButton("Hash File");
        chooseFileButton = new JButton("Chọn File");

        hashResultArea = new JTextArea(10, 30);
        hashResultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(hashResultArea);

        fileChooser = new JFileChooser();

        // Add components to panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Chọn Thuật Toán:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(md5RadioButton, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel(""), gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(sha256RadioButton, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Nhập Nội Dung:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(contentField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(hashButton, gbc);
        gbc.gridx = 1;
        gbc.gridy = 3;
        add(chooseFileButton, gbc);
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(hashFileButton, gbc);

        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        add(scrollPane, gbc);

        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        // Set default selections
        md5RadioButton.setSelected(true);

        // Add action listeners
        hashButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateHash();
            }
        });

        chooseFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseFile();
            }
        });

        hashFileButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                generateHashFile();
            }

        });
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
        String content = contentField.getText();

        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.update(content.getBytes());
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

    private void generateHashFile() {
        if (selectedFile == null) {
            JOptionPane.showMessageDialog(this, "Chưa chọn file", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String algorithm = md5RadioButton.isSelected() ? "MD5" : "SHA-256";
        String content;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(selectedFile))) {
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line).append("\n");
            }
            content = builder.toString();
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi đọc file: " + ex.getMessage(), "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.update(content.getBytes());
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

