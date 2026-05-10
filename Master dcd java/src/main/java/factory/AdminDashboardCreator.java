package factory;

import javax.swing.JFrame;
import Boundary.AdminDashboard;

public class AdminDashboardCreator extends DashboardCreator {
    @Override
    public JFrame createDashboard() {
        return new AdminDashboard();
    }
}

