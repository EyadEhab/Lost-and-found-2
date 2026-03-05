package factory.ui;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Color;

public class DarkUIFactory implements UIFactory {

    @Override
    public JButton createButton(String text) {
        JButton b = new JButton(text);
        b.setBackground(Color.DARK_GRAY);
        b.setForeground(Color.WHITE);
        return b;
    }

    @Override
    public JLabel createLabel(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(Color.WHITE);
        return l;
    }

    @Override
    public JTextField createTextField(int columns) {
        JTextField t = new JTextField(columns);
        t.setBackground(Color.GRAY);
        t.setForeground(Color.WHITE);
        return t;
    }
}

