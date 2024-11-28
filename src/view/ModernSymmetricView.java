package view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import controller.ModernSymmetricController;
import enums.Action;

public class ModernSymmetricView extends JPanel {
    private JTextArea textArea;
    private JTextField keyField;
    private JComboBox<String> algorithmComboBox, keySizeComboBox, modeCombobox, paddingCombobox;
    private JTextField keySizeField;
    private JButton encryptButton, decryptButton, generateKeyButton, encryptFileButton, decryptFileButton,
            saveKeyButton, loadKeyButton;
    private JPanel panel;
    private SecretKey secretKey;

    // AES key sizes
    private String[] aesKeySize = { "128", "192", "256" };

    // DES key sizes
    private String[] desKeySize = { "56" }; // Thực tế chỉ 56-bit được sử dụng.

    // Triple DES (DESede) key sizes
    private String[] desedeKeySize = { "112", "168" }; // 2 hoặc 3 khóa DES.

    // Blowfish key sizes
    private String[] blowfishKeySize = { "32", "448" }; // Min và Max key sizes.

    // RC2 key sizes
    private String[] rc2KeySize = { "40", "128" }; // Min và Max key sizes.

    // RC4 key sizes
    private String[] rc4KeySize = { "40", "256" }; // Min và Max key sizes.

    // RC5 key sizes
    private String[] rc5KeySize = { "0", "2040" }; // Min và Max key sizes.

    private String[] algorithms = { "AES", "DES", "DESede", "Blowfish", "RC2", "RC4", "RC5", "CAST5", "IDEA", "Camellia"};
    private String[] modes = { "ECB", "CBC", "CFB", "OFB", "CTR", "GCM", "CTS" };
    private String[] paddings = {
        "PKCS5Padding",
        "PKCS7Padding",
        "ISO10126Padding",
        "ANSI_X.923Padding",
        "ZeroPadding",
        "NoPadding",
    };

    private int keySelectIndex = 3;

    public ModernSymmetricView() {
        setLayout(new BorderLayout());

        textArea = new JTextArea();
        add(new JScrollPane(textArea), BorderLayout.CENTER);

        panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2)); // Mỗi hàng chứa 2 cột

        // Giải thuật
        panel.add(new JLabel("Giải thuật:"));
        algorithmComboBox = new JComboBox<>(algorithms);
        panel.add(algorithmComboBox);

        // Kích thước Key
        panel.add(new JLabel("Kích thước Key:"));
        keySizeComboBox = new JComboBox<>(aesKeySize);
        panel.add(keySizeComboBox);
        keySizeField = new JTextField();

        // Key
        panel.add(new JLabel("Key:"));

        JPanel keyPanel = new JPanel();
        keyField = new JTextField();
        loadKeyButton = new JButton("Tải key");
        loadKeyButton.addActionListener(new ModernSymmetricController(this, Action.LoadKey));
        keyPanel.setLayout(new GridLayout(1, 2));
        keyPanel.add(keyField);
        keyPanel.add(loadKeyButton);
        panel.add(keyPanel);

        // Mode
        panel.add(new JLabel("Mode:"));
        modeCombobox = new JComboBox<>(modes);
        panel.add(modeCombobox);

        // Padding
        panel.add(new JLabel("Padding:"));
        paddingCombobox = new JComboBox<>(paddings);
        panel.add(paddingCombobox);

        // Nút tạo key
        generateKeyButton = new JButton("Tạo Key");
        panel.add(generateKeyButton);

        saveKeyButton = new JButton("Lưu Key");
        panel.add(saveKeyButton);
        saveKeyButton.addActionListener(new ModernSymmetricController(this, Action.SaveKey));

        // Nút mã hóa
        encryptButton = new JButton("Mã hóa");
        panel.add(encryptButton);

        // Nút giải mã
        decryptButton = new JButton("Giải mã");
        panel.add(decryptButton);

        // Nút mã hoá file
        encryptFileButton = new JButton("Mã hoá File");
        panel.add(encryptFileButton);

        // Nút giải mã file
        decryptFileButton = new JButton("Giải mã File");
        panel.add(decryptFileButton);

        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        add(panel, BorderLayout.SOUTH);

        // Thao tác khi nút "Tạo Key" được nhấn
        generateKeyButton.addActionListener(new ModernSymmetricController(this, Action.GenerateKey));

        // Thao tác khi nút "Mã hóa" được nhấn
        encryptButton.addActionListener(new ModernSymmetricController(this, Action.Encrypt));

        // Thao tác khi nút "Giải mã" được nhấn
        decryptButton.addActionListener(new ModernSymmetricController(this, Action.Decrypt));

        algorithmComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String algorithm = (String) algorithmComboBox.getSelectedItem();
                String[] keySizes;

                // Cập nhật danh sách key size dựa trên thuật toán được chọn
                boolean isShowCombobox = true;
                switch (algorithm) {
                    case "AES":
                        keySizes = aesKeySize;
                        break;
                    case "DES":
                        keySizes = desKeySize;
                        break;
                    case "DESede":
                        keySizes = desedeKeySize;
                        break;
                    case "Blowfish":
                        isShowCombobox = false;
                        keySizes = blowfishKeySize;
                        break;
                    case "RC2":
                        isShowCombobox = false;
                        keySizes = rc2KeySize;
                        break;
                    case "RC4":
                        isShowCombobox = false;
                        keySizes = rc4KeySize;
                        break;
                    case "RC5":
                        isShowCombobox = false;
                        keySizes = rc5KeySize;
                        break;
                    default:
                        keySizes = new String[0]; // Trường hợp không có thuật toán nào phù hợp
                }
                // Cập nhật keySizeComboBox
                if (isShowCombobox) {
                    keySizeComboBox.setModel(new DefaultComboBoxModel<>(keySizes));
                    showKeySizeCombobox();
                } else {
                    String min = keySizes[0];
                    String max = keySizes[1];
                    keySizeField.setText(min + " - " + max);
                    showKeySizeTextField();
                }
            }
        });

        encryptFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                encryptFile(); // Mã hóa văn bản
            }
        });

        decryptFileButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                decryptFile();
            }

        });
    }

    public void showMessageDialog(String text) {
        JOptionPane.showMessageDialog(this, text);
    }

    private void showKeySizeCombobox() {
        panel.remove(keySizeField);
        panel.add(keySizeComboBox, keySelectIndex);
    }

    private void showKeySizeTextField() {
        panel.remove(keySizeComboBox);
        panel.add(keySizeField, keySelectIndex);
    }

    // Phương thức mã hoá file
    private void encryptFile() {
        // Chọn file cần mã hoá
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                // Mã hoá file
                FileInputStream fileInputStream = new FileInputStream(file);
                byte[] fileBytes = new byte[(int) file.length()];
                fileInputStream.read(fileBytes);
                fileInputStream.close();

                // Mã hoá byte array
                byte[] encryptedBytes = encryptBytes(fileBytes);

                // Ghi file mã hoá
                FileOutputStream fileOutputStream = new FileOutputStream("encrypted_" + file.getName());
                fileOutputStream.write(encryptedBytes);
                fileOutputStream.close();
                JOptionPane.showMessageDialog(this, "File đã được mã hoá thành công tại đường dẫn: "
                        + new File("encrypted_" + file.getName()).getAbsolutePath());
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi mã hoá file!");
            }
        }
    }

    // Phương thức giải mã file
    private void decryptFile() {
        // Chọn file cần giải mã
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                // Giải mã file
                FileInputStream fileInputStream = new FileInputStream(file);
                byte[] fileBytes = new byte[(int) file.length()];
                fileInputStream.read(fileBytes);
                fileInputStream.close();

                // Giải mã byte array
                byte[] decryptedBytes = decryptBytes(fileBytes);

                // Ghi file giải mã
                FileOutputStream fileOutputStream = new FileOutputStream("decrypted_" + file.getName());
                fileOutputStream.write(decryptedBytes);
                fileOutputStream.close();

                JOptionPane.showMessageDialog(this, "File đã được giải mã thành công!");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi giải mã file!");
            }
        }
    }

    private byte[] encryptBytes(byte[] bytes) {
        try {
            // Tạo đối tượng Cipher
            String selectedAlgorithm = (String) algorithmComboBox.getSelectedItem();
            String mode = (String) modeCombobox.getSelectedItem();
            String padding = (String) paddingCombobox.getSelectedItem();
            Cipher cipher = Cipher.getInstance(selectedAlgorithm + "/" + mode + "/" + padding);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            // Mã hoá byte array
            return cipher.doFinal(bytes);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    // Phương thức giải mã byte array
    private byte[] decryptBytes(byte[] bytes) {
        try {
            // Tạo đối tượng Cipher
            String selectedAlgorithm = (String) algorithmComboBox.getSelectedItem();
            String mode = (String) modeCombobox.getSelectedItem();
            String padding = (String) paddingCombobox.getSelectedItem();
            Cipher cipher = Cipher.getInstance(selectedAlgorithm + "/" + mode + "/" + padding);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            // Giải mã byte array
            return cipher.doFinal(bytes);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public JTextArea getTextArea() {
        return textArea;
    }

    public void setTextArea(JTextArea textArea) {
        this.textArea = textArea;
    }

    public JTextField getKeyField() {
        return keyField;
    }

    public void setKeyField(JTextField keyField) {
        this.keyField = keyField;
    }

    public JComboBox<String> getAlgorithmComboBox() {
        return algorithmComboBox;
    }

    public void setAlgorithmComboBox(JComboBox<String> algorithmComboBox) {
        this.algorithmComboBox = algorithmComboBox;
    }

    public JComboBox<String> getKeySizeComboBox() {
        return keySizeComboBox;
    }

    public void setKeySizeComboBox(JComboBox<String> keySizeComboBox) {
        this.keySizeComboBox = keySizeComboBox;
    }

    public JTextField getKeySizeField() {
        return keySizeField;
    }

    public void setKeySizeField(JTextField keySizeField) {
        this.keySizeField = keySizeField;
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

    public JButton getGenerateKeyButton() {
        return generateKeyButton;
    }

    public void setGenerateKeyButton(JButton generateKeyButton) {
        this.generateKeyButton = generateKeyButton;
    }

    public JButton getEncryptFileButton() {
        return encryptFileButton;
    }

    public void setEncryptFileButton(JButton encryptFileButton) {
        this.encryptFileButton = encryptFileButton;
    }

    public JButton getDecryptFileButton() {
        return decryptFileButton;
    }

    public void setDecryptFileButton(JButton decryptFileButton) {
        this.decryptFileButton = decryptFileButton;
    }

    public JButton getSaveKeyButton() {
        return saveKeyButton;
    }

    public void setSaveKeyButton(JButton saveKeyButton) {
        this.saveKeyButton = saveKeyButton;
    }

    public JButton getLoadKeyButton() {
        return loadKeyButton;
    }

    public void setLoadKeyButton(JButton loadKeyButton) {
        this.loadKeyButton = loadKeyButton;
    }

    public JPanel getPanel() {
        return panel;
    }

    public void setPanel(JPanel panel) {
        this.panel = panel;
    }

    public SecretKey getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(SecretKey secretKey) {
        this.secretKey = secretKey;
    }

    public String[] getAesKeySize() {
        return aesKeySize;
    }

    public void setAesKeySize(String[] aesKeySize) {
        this.aesKeySize = aesKeySize;
    }

    public String[] getDesKeySize() {
        return desKeySize;
    }

    public void setDesKeySize(String[] desKeySize) {
        this.desKeySize = desKeySize;
    }

    public String[] getDesedeKeySize() {
        return desedeKeySize;
    }

    public void setDesedeKeySize(String[] desedeKeySize) {
        this.desedeKeySize = desedeKeySize;
    }

    public String[] getBlowfishKeySize() {
        return blowfishKeySize;
    }

    public void setBlowfishKeySize(String[] blowfishKeySize) {
        this.blowfishKeySize = blowfishKeySize;
    }

    public String[] getRc2KeySize() {
        return rc2KeySize;
    }

    public void setRc2KeySize(String[] rc2KeySize) {
        this.rc2KeySize = rc2KeySize;
    }

    public String[] getRc4KeySize() {
        return rc4KeySize;
    }

    public void setRc4KeySize(String[] rc4KeySize) {
        this.rc4KeySize = rc4KeySize;
    }

    public String[] getRc5KeySize() {
        return rc5KeySize;
    }

    public void setRc5KeySize(String[] rc5KeySize) {
        this.rc5KeySize = rc5KeySize;
    }

    public int getKeySelectIndex() {
        return keySelectIndex;
    }

    public void setKeySelectIndex(int keySelectIndex) {
        this.keySelectIndex = keySelectIndex;
    }

    public JComboBox<String> getModeCombobox() {
        return modeCombobox;
    }

    public void setModeCombobox(JComboBox<String> modeCombobox) {
        this.modeCombobox = modeCombobox;
    }

    public JComboBox<String> getPaddingCombobox() {
        return paddingCombobox;
    }

    public void setPaddingCombobox(JComboBox<String> paddingCombobox) {
        this.paddingCombobox = paddingCombobox;
    }

    public String[] getAlgorithms() {
        return algorithms;
    }

    public void setAlgorithms(String[] algorithms) {
        this.algorithms = algorithms;
    }

    public String[] getModes() {
        return modes;
    }

    public void setModes(String[] modes) {
        this.modes = modes;
    }

    public String[] getPaddings() {
        return paddings;
    }

    public void setPaddings(String[] paddings) {
        this.paddings = paddings;
    }
}
