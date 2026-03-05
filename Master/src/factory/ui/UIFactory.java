package factory.ui;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

public interface UIFactory {

    JButton createButton(String text);

    JLabel createLabel(String text);

    JTextField createTextField(int columns);
}

