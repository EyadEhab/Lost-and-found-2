package factory;

import javax.swing.JFrame;
import Boundary.OfficerDashboard;

public class OfficerDashboardCreator extends DashboardCreator {
    @Override
    public JFrame createDashboard() {
        return new OfficerDashboard();
    }
}

