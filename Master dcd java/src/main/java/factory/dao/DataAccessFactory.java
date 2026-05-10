package factory.dao;

import DAO.ItemDataAccess;
import DAO.UserDataAccess;
import DAO.ClaimDataAccess;
import DAO.ReportDataAccess;

public interface DataAccessFactory {
    ItemDataAccess createItemDAO();
    UserDataAccess createUserDAO();
    ClaimDataAccess createClaimDAO();
    ReportDataAccess createReportDAO();
}

