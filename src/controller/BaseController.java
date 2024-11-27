package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import algo.Symmetric;
import enums.Action;
import view.ModernSymmetric;

public class BaseController implements ActionListener {

    private ModernSymmetric mainView;
    private Action action;

    public BaseController(ModernSymmetric mainView, Action action) {
        this.mainView = mainView;
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
            default:
                break;
        }
    }

    private void doGenerateKey() {
        String selectedAlgorithm = (String) mainView.getAlgorithmComboBox().getSelectedItem();
        int keySize = Integer.parseInt((String) mainView.getKeySizeComboBox().getSelectedItem());
        String key = Symmetric.generateKey(selectedAlgorithm, keySize);
        mainView.getKeyField().setText(key);
    }

    private void doEncrypt() {
        String selectedAlgorithm = (String) mainView.getAlgorithmComboBox().getSelectedItem();
        String mode = (String) mainView.getModeCombobox().getSelectedItem();
        String padding = (String) mainView.getPaddingCombobox().getSelectedItem();
        String key = mainView.getKeyField().getText();
        String plainText = mainView.getTextArea().getText();
        String cipherText = Symmetric.encryptText(selectedAlgorithm, mode, padding, key, plainText);
        mainView.getTextArea().setText(cipherText);
    }

    private void doDecrypt() {
        String selectedAlgorithm = (String) mainView.getAlgorithmComboBox().getSelectedItem();
        String mode = (String) mainView.getModeCombobox().getSelectedItem();
        String padding = (String) mainView.getPaddingCombobox().getSelectedItem();
        String key = mainView.getKeyField().getText();
        String cipherText = mainView.getTextArea().getText();
        String plainText = Symmetric.decryptText(selectedAlgorithm, mode, padding, key, cipherText);
        mainView.getTextArea().setText(plainText);
    }

}
