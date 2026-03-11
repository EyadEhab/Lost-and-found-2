package factory.dao;

import DAO.ItemDataAccess;
import DAO.UserDataAccess;
import DAO.ClaimDataAccess;
import DAO.ReportDataAccess;

/**
 * MockDataAccessFactory is a testing/extension hook for data access.
 * <p>
 * In the current deliverable it simply delegates to the real DAO classes, but
 * it can be swapped to return in-memory or stub implementations for unit tests
 * without changing calling code.
 */
public class MockDataAccessFactory implements DataAccessFactory {

    @Override
    public ItemDataAccess createItemDAO() {
        // For now, reuse the real DAO implementation.
        return new ItemDataAccess();
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

