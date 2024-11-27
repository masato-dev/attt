package view;
import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import com.formdev.flatlaf.themes.FlatMacLightLaf;

public class MainView extends JFrame {
    public MainView() {
        FlatMacLightLaf.setup();
        setTitle("Main View with Card Layout");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new CardLayout());

        JTabbedPane tabbedPane = new JTabbedPane();
        add(tabbedPane);

        ClassicSymmetricView classicSymmetricView = new ClassicSymmetricView();
        ModernSymmetricView modernSymmetricView = new ModernSymmetricView();
        tabbedPane.addTab("Mã hoá đối xứng cổ điện", classicSymmetricView);
        tabbedPane.addTab("Mã hoá đối xứng hiện đại", modernSymmetricView);

        setVisible(true);
    }
}


