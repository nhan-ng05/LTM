import bus.SinhVienBUS;
import ui.MainFrame;
import ui.Theme;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Làm mượt chữ trên một số hệ điều hành
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        SwingUtilities.invokeLater(() -> {
            Theme.install();
            new MainFrame(new SinhVienBUS()).setVisible(true);
        });
    }
}
