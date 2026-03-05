package factory.dao;

import DAO.ItemDataAccess;
import DAO.UserDataAccess;
import DAO.ClaimDataAccess;
import DAO.ReportDataAccess;

public class SqlDataAccessFactory implements DataAccessFactory {

    @Override
    public ItemDataAccess createItemDAO() {
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

