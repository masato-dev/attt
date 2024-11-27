import javax.swing.SwingUtilities;

import view.MainView;

public class App {
    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(() -> {
            new MainView().setVisible(true);
        });
    }
}
