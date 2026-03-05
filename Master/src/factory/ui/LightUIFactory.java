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
}

