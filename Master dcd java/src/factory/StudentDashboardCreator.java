package factory;

import javax.swing.JFrame;
import Boundary.SearchWindow;

public class StudentDashboardCreator extends DashboardCreator {
    @Override
    public JFrame createDashboard() {
        return new SearchWindow();
    }
}

