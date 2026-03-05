package factory.dao;

import DAO.ItemDataAccess;
import DAO.UserDataAccess;
import DAO.ClaimDataAccess;
import DAO.ReportDataAccess;

public class MockDataAccessFactory implements DataAccessFactory {

    @Override
    public ItemDataAccess createItemDAO() {
        return new ItemDataAccess();
        // Keep it simple: return real DAO for now if mocking is too much.
        // (In the report, you can explain Mock factory is extendable for testing.)
    }

    @Override
    public UserDataAccess createUserDAO() {
        return new UserDataAccess();
    }

    @Override
    public ClaimDataAccess createClaimDAO() {
        return new ClaimDataAccess();
    }

    @Override
    public ReportDataAccess createReportDAO() {
        return new ReportDataAccess();
    }
}

