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
        setSize(600, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new CardLayout());

        JTabbedPane tabbedPane = new JTabbedPane();
        add(tabbedPane);

        ClassicSymmetricView classicSymmetricView = new ClassicSymmetricView();
        ModernSymmetricView modernSymmetricView = new ModernSymmetricView();
        HashView hashView = new HashView();
        tabbedPane.addTab("Mã hoá đối xứng cổ điện", classicSymmetricView);
        tabbedPane.addTab("Mã hoá đối xứng hiện đại", modernSymmetricView);
        tabbedPane.addTab("Hash", hashView);

        setVisible(true);
    }
}


