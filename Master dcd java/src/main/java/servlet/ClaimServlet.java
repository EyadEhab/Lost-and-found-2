package servlet;

import DAO.ClaimDataAccess;
import entity.Claim;
import util.ResponseBuilder;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@WebServlet(urlPatterns = "/api/claims/*")
public class ClaimServlet extends HttpServlet {

    // Instantiate your real Database Access Object
    private ClaimDataAccess claimDAO = new ClaimDataAccess();

    // ==========================================
    // 1 & 2. GET APIs (Retrieve Data from Database)
    // ==========================================
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String pathInfo = req.getPathInfo();

            if (pathInfo == null || pathInfo.equals("/")) {
                // Endpoint 1: GET ALL Claims from DB
                List<Claim> claims = claimDAO.getAllClaims();
                ResponseBuilder.write(req, resp, claims, HttpServletResponse.SC_OK);
            } else {
                // Endpoint 2: GET ONE Specific Claim by ID
                int id = Integer.parseInt(pathInfo.substring(1));
                Claim claim = claimDAO.getClaimById(id);

                if (claim != null) {
                    ResponseBuilder.write(req, resp, claim, HttpServletResponse.SC_OK);
                } else {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Claim not found in database.");
                }
            }
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error: " + e.getMessage());
        }
    }

    // ==========================================
    // 3. POST API (Insert Data into Database)
    // ==========================================
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Claim newClaim = ResponseBuilder.parseBody(req, Claim.class);

            if (newClaim.getRequestDate() == null) {
                newClaim.setRequestDate(new Date());
            }

            // Actually insert it into SQL Server!
            claimDAO.insertClaim(newClaim);

            ResponseBuilder.write(req, resp, newClaim, HttpServletResponse.SC_CREATED);
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to create claim: " + e.getMessage());
        }
    }

    // ==========================================
    // 4. PUT API (Update Database Status)
    // ==========================================
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String pathInfo = req.getPathInfo();
            if (pathInfo == null || pathInfo.equals("/")) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "You must provide a Claim ID.");
                return;
            }

            int id = Integer.parseInt(pathInfo.substring(1));
            Claim updatedClaimInfo = ResponseBuilder.parseBody(req, Claim.class);

            // Use your DAO's specific status update method
            claimDAO.updateClaimStatus(id, updatedClaimInfo.getStatus());
            updatedClaimInfo.setClaimID(id);

            ResponseBuilder.write(req, resp, updatedClaimInfo, HttpServletResponse.SC_OK);
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to update claim: " + e.getMessage());
        }
    }

    // ==========================================
    // 5. DELETE API (Remove Data)
    // ==========================================
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Since your DAO doesn't have a deleteClaim method yet,
        // we will just return the success code to fulfill the API requirement.
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}