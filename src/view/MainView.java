package view;
import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import com.formdev.flatlaf.FlatLightLaf;

public class MainView extends JFrame {
    public MainView() {
        // Setup theme
        FlatLightLaf.setup();
        setTitle("Ứng dụng mã hoá");
        setSize(900, 600);
        setLayout(new CardLayout());
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTabbedPane tabbedPane = new JTabbedPane();
        add(tabbedPane);

        ClassicSymmetricView classicSymmetricView = new ClassicSymmetricView();
        ModernSymmetricView modernSymmetricView = new ModernSymmetricView();
        AsymmetricView asymmetricView = new AsymmetricView();
        HashView hashView = new HashView();
        tabbedPane.addTab("Mã hoá đối xứng cổ điển", classicSymmetricView);
        tabbedPane.addTab("Mã hoá đối xứng hiện đại", modernSymmetricView);
        tabbedPane.addTab("Mã hoá bất đối xứng", asymmetricView);
        tabbedPane.addTab("Hash", hashView);

        setVisible(true);
    }
}


