package factory.ui;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class LightUIFactory implements UIFactory {

    @Override
    public JButton createButton(String text) {
        return new JButton(text);
    }

    @Override
    public JLabel createLabel(String text) {
        return new JLabel(text);
    }

    @Override
    public JTextField createTextField(int columns) {
        return new JTextField(columns);
    }

    @Override
    public java.awt.Color getBackgroundColor() {
        return new java.awt.Color(235, 235, 235);
    }

    @Override
    public java.awt.Color getTextColor() {
        return java.awt.Color.BLACK;
    }

    @Override
    public java.awt.Color getSurfaceColor() {
        return java.awt.Color.WHITE;
    }

    @Override
    public java.awt.Color getAccentColor() {
        return new java.awt.Color(70, 130, 180);
    }
}
