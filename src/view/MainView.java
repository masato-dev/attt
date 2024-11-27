package view;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class MainView extends JFrame {
    public MainView() {
        setTitle("Main View with Card Layout");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new CardLayout());

        JTabbedPane tabbedPane = new JTabbedPane();
        add(tabbedPane);

        ClassicSymmetric classicSymmetric = new ClassicSymmetric();
        ModernSymmetric modernSymmetric = new ModernSymmetric();
        tabbedPane.addTab("Mã hoá đối xứng cổ điện", classicSymmetric);
        tabbedPane.addTab("Mã hoá đối xứng hiện đại", modernSymmetric);

        setVisible(true);
    }
}

