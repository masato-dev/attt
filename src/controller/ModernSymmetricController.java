package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.JFileChooser;

import algo.ModrenSymmetric;
import enums.Action;
import view.ModernSymmetricView;

public class ModernSymmetricController implements ActionListener {

    private ModernSymmetricView view;
    private Action action;

    public ModernSymmetricController(ModernSymmetricView view, Action action) {
        this.view = view;
        this.action = action;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (action) {
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

    private void doGenerateKey() {
        String selectedAlgorithm = (String) view.getAlgorithmComboBox().getSelectedItem();
        int keySize = Integer.parseInt((String) view.getKeySizeComboBox().getSelectedItem());
        String key = ModrenSymmetric.generateKey(selectedAlgorithm, keySize);
        view.getKeyField().setText(key);
    }

    private void doEncrypt() {
        String selectedAlgorithm = (String) view.getAlgorithmComboBox().getSelectedItem();
        String mode = (String) view.getModeCombobox().getSelectedItem();
        String padding = (String) view.getPaddingCombobox().getSelectedItem();
        String key = view.getKeyField().getText();
        String plainText = view.getTextArea().getText();
        String cipherText;
        try {
            cipherText = ModrenSymmetric.encryptText(selectedAlgorithm, mode, padding, key, plainText);
            view.getTextArea().setText(cipherText);
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException
                | NoSuchPaddingException | InvalidAlgorithmParameterException e) {
            view.showMessageDialog(e.getMessage());
            e.printStackTrace();
        }
    }

    private void doDecrypt() {
        String selectedAlgorithm = (String) view.getAlgorithmComboBox().getSelectedItem();
        String mode = (String) view.getModeCombobox().getSelectedItem();
        String padding = (String) view.getPaddingCombobox().getSelectedItem();
        String key = view.getKeyField().getText();
        String cipherText = view.getTextArea().getText();
        String plainText;
        try {
            plainText = ModrenSymmetric.decryptText(selectedAlgorithm, mode, padding, key, cipherText);
            view.getTextArea().setText(plainText);
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException
                | NoSuchPaddingException | InvalidAlgorithmParameterException e) {
            view.showMessageDialog(e.getMessage());
        }
    }

    private void doSaveKey() {
        // Kiểm tra nếu trường key trống
        if (view.getKeyField().getText().isBlank()) {
            view.showMessageDialog("Vui lòng nhập key");
            return;
        }

        // Mở hộp thoại lưu file
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Lưu key vào file");

        // Thiết lập chế độ lưu file
        int userSelection = fileChooser.showSaveDialog(view);

        // Kiểm tra nếu người dùng chọn lưu file
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            // Lấy file người dùng chọn
            File fileToSave = fileChooser.getSelectedFile();

            // Kiểm tra nếu file không có đuôi .txt thì tự động thêm
            if (!fileToSave.getName().endsWith(".txt")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".txt");
            }

            // Ghi key vào file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
                writer.write(view.getKeyField().getText());
                view.showMessageDialog("Key đã được lưu vào file: " + fileToSave.getAbsolutePath());
            } catch (IOException e) {
                view.showMessageDialog("Có lỗi xảy ra khi lưu key: " + e.getMessage());
            }
        }
    }

    private void doLoadKey() {
        // Mở hộp thoại chọn file
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn tệp chứa key");

        // Thiết lập chế độ mở tệp
        int userSelection = fileChooser.showOpenDialog(view);

        // Kiểm tra nếu người dùng chọn tệp
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            // Lấy tệp người dùng chọn
            File fileToOpen = fileChooser.getSelectedFile();
            // Đọc nội dung từ tệp
            try (BufferedReader reader = new BufferedReader(new FileReader(fileToOpen))) {
                // Lưu nội dung tệp vào biến keyStr
                StringBuilder keyStrBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    keyStrBuilder.append(line).append("\n");
                }
                // Gán giá trị cho biến keyStr
                String keyStr = keyStrBuilder.toString().trim();
                if(!keyStr.isBlank()) {
                    view.getKeyField().setText(keyStr);
                    view.showMessageDialog("Load key thành công");
                }
                
            } catch (IOException e) {
                view.showMessageDialog("Có lỗi khi đọc tệp: " + e.getMessage());
            }
        }
    }

}
