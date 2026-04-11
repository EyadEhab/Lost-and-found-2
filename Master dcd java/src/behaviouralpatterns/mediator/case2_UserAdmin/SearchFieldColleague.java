package behaviouralpatterns.mediator.case2_UserAdmin;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Mediator Pattern - Search Field Colleague
 *
 * Concrete colleague that wraps the search JTextField and button.
 * Notifies mediator when search is requested.
 *
 * Role: ConcreteColleague
 */
public class SearchFieldColleague implements UserAdminColleague {

    private final JTextField searchField;
    private final JButton searchButton;
    private UserAdminMediator mediator;

    public SearchFieldColleague(JTextField searchField, JButton searchButton) {
        this.searchField = searchField;
        this.searchButton = searchButton;
        setupListener();
    }

    /**
     * Sets up an ActionListener on the search button.
     */
    private void setupListener() {
        searchButton.addActionListener((e) -> {
            if (mediator != null) {
                String userId = searchField.getText().trim();
                mediator.notifySearchRequested(userId);
            }
        });

        // Also trigger search on Enter key in text field
        searchField.addActionListener((e) -> {
            if (mediator != null) {
                String userId = searchField.getText().trim();
                mediator.notifySearchRequested(userId);
            }
        });
    }

    @Override
    public void setMediator(UserAdminMediator mediator) {
        this.mediator = mediator;
    }

    @Override
    public void onMediatorEvent(String event, Object data) {
        if ("CLEAR_SEARCH".equals(event)) {
            searchField.setText("");
        } else if ("FOCUS_SEARCH".equals(event)) {
            searchField.requestFocus();
        }
    }

    @Override
    public Object getState() {
        return searchField.getText().trim();
    }

    /**
     * Gets the search field for initial setup.
     */
    public JTextField getSearchField() {
        return searchField;
    }

    /**
     * Gets the search button for initial setup.
     */
    public JButton getSearchButton() {
        return searchButton;
    }
}
