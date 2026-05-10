package servlet;

import DAO.ClaimDataAccess;
import DAO.ItemDataAccess; // Assuming you have this
import entity.Claim;
import entity.Item; // Assuming you have this
import util.ResponseBuilder;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// This is your COMPOSITE SERVICE endpoint
@WebServlet(urlPatterns = "/api/process-claim/*")
public class ProcessClaimServlet extends HttpServlet {

    private ClaimDataAccess claimDAO = new ClaimDataAccess();
    // Instantiate your Item DAO to interact with the second resource
    private ItemDataAccess itemDAO = new ItemDataAccess();

    // We use POST because this is a complex business action, not just a simple
    // update
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            // 1. Read the incoming request (e.g., {"claimID": 1006, "action": "Approve"})
            Map<String, Object> payload = ResponseBuilder.parseBody(req, Map.class);

            if (!payload.containsKey("claimID") || !payload.containsKey("action")) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing claimID or action");
                return;
            }

            // Extract variables safely
            int claimId = ((Double) payload.get("claimID")).intValue();
            String action = (String) payload.get("action");

            // 2. Fetch the Claim to find out which Item it belongs to
            Claim claim = claimDAO.getClaimById(claimId);
            if (claim == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Claim not found");
                return;
            }

            int itemId = claim.getItemID();
            String finalClaimStatus = action.equals("Approve") ? "Approved" : "Rejected";
            String finalItemStatus = action.equals("Approve") ? "Returned" : "Found";

            // ==========================================
            // THE ORCHESTRATION (The core of Part 3)
            // ==========================================

            // Action A: Update the Claim Database
            claimDAO.updateClaimStatus(claimId, finalClaimStatus);

            // Action B: Update the Item Database
            // (You may need to change 'updateItemStatus' to match your actual ItemDAO
            // method name)
            itemDAO.updateItemStatus(itemId, finalItemStatus);
            // ==========================================

            // 3. Build a composite response to prove both worked
            Map<String, String> result = new HashMap<>();
            result.put("message", "Workflow completed successfully");
            result.put("claimUpdate", "Claim " + claimId + " marked as " + finalClaimStatus);
            result.put("itemUpdate", "Item " + itemId + " marked as " + finalItemStatus);

            ResponseBuilder.write(req, resp, result, HttpServletResponse.SC_OK);

        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Workflow failed: " + e.getMessage());
        }
    }
}