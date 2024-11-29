package view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import controller.AsymmetricController;
import enums.AsymmetricAction;

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
        generateKeyButton.addActionListener(new AsymmetricController(this, AsymmetricAction.GenerateKey));
        encryptButton.addActionListener(new AsymmetricController(this, AsymmetricAction.Encrypt));
        decryptButton.addActionListener(new AsymmetricController(this, AsymmetricAction.Decrypt));
        savePublicKeyBtn.addActionListener(new AsymmetricController(this, AsymmetricAction.SavePublicKey));
        savePrivateKeyBtn.addActionListener(new AsymmetricController(this, AsymmetricAction.SavePrivateKey));
        loadPublicKeyButton.addActionListener(new AsymmetricController(this, AsymmetricAction.LoadPublicKey));
        loadPrivateKeyButton.addActionListener(new AsymmetricController(this, AsymmetricAction.LoadPrivateKey));
    }

    public JComboBox<String> getRsaKeyLengthComboBox() {
        return rsaKeyLengthComboBox;
    }

    public void setRsaKeyLengthComboBox(JComboBox<String> rsaKeyLengthComboBox) {
        this.rsaKeyLengthComboBox = rsaKeyLengthComboBox;
    }

    public JTextArea getContentArea() {
        return contentArea;
    }

    public void setContentArea(JTextArea contentArea) {
        this.contentArea = contentArea;
    }

    public JButton getGenerateKeyButton() {
        return generateKeyButton;
    }

    public void setGenerateKeyButton(JButton generateKeyButton) {
        this.generateKeyButton = generateKeyButton;
    }

    public JButton getLoadPublicKeyButton() {
        return loadPublicKeyButton;
    }

    public void setLoadPublicKeyButton(JButton loadPublicKeyButton) {
        this.loadPublicKeyButton = loadPublicKeyButton;
    }

    public JButton getLoadPrivateKeyButton() {
        return loadPrivateKeyButton;
    }

    public void setLoadPrivateKeyButton(JButton loadPrivateKeyButton) {
        this.loadPrivateKeyButton = loadPrivateKeyButton;
    }

    public JButton getEncryptButton() {
        return encryptButton;
    }

    public void setEncryptButton(JButton encryptButton) {
        this.encryptButton = encryptButton;
    }

    public JButton getDecryptButton() {
        return decryptButton;
    }

    public void setDecryptButton(JButton decryptButton) {
        this.decryptButton = decryptButton;
    }

    public JButton getSavePublicKeyBtn() {
        return savePublicKeyBtn;
    }

    public void setSavePublicKeyBtn(JButton savePublicKeyBtn) {
        this.savePublicKeyBtn = savePublicKeyBtn;
    }

    public JButton getSavePrivateKeyBtn() {
        return savePrivateKeyBtn;
    }

    public void setSavePrivateKeyBtn(JButton savePrivateKeyBtn) {
        this.savePrivateKeyBtn = savePrivateKeyBtn;
    }

    public JTextArea getResultArea() {
        return resultArea;
    }

    public void setResultArea(JTextArea resultArea) {
        this.resultArea = resultArea;
    }

    public JTextArea getPublicKeyArea() {
        return publicKeyArea;
    }

    public void setPublicKeyArea(JTextArea publicKeyArea) {
        this.publicKeyArea = publicKeyArea;
    }

    public JTextArea getPrivateKeyArea() {
        return privateKeyArea;
    }

    public void setPrivateKeyArea(JTextArea privateKeyArea) {
        this.privateKeyArea = privateKeyArea;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

}