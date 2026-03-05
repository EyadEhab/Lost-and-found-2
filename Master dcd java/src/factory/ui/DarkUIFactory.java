package factory.ui;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Color;

public class DarkUIFactory implements UIFactory {

    private static final Color BG_DARK = new Color(24, 24, 24);
    private static final Color SURFACE_DARK = new Color(40, 40, 40);
    private static final Color ACCENT_DARK = new Color(0, 120, 215);
    private static final Color TEXT_PRIMARY = new Color(255, 255, 255);

    @Override
    public JButton createButton(String text) {
        JButton b = new JButton(text);
        b.setBackground(ACCENT_DARK);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 15, 5, 15));
        return b;
    }

    @Override
    public JLabel createLabel(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(TEXT_PRIMARY);
        return l;
    }

    @Override
    public JTextField createTextField(int columns) {
        JTextField t = new JTextField(columns);
        t.setBackground(SURFACE_DARK);
        t.setForeground(TEXT_PRIMARY);
        t.setCaretColor(Color.WHITE);
        t.setBorder(javax.swing.BorderFactory.createLineBorder(new Color(60, 60, 60)));
        return t;
    }

    @Override
    public Color getBackgroundColor() {
        return BG_DARK;
    }

    @Override
    public Color getTextColor() {
        return TEXT_PRIMARY;
    }

    @Override
    public Color getSurfaceColor() {
        return SURFACE_DARK;
    }

    @Override
    public Color getAccentColor() {
        return ACCENT_DARK;
    }
}
