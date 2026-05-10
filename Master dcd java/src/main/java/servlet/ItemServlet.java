package servlet;

import entity.Item;
import DAO.ItemDataAccess;
import util.ResponseBuilder;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = "/api/items/*")
public class ItemServlet extends HttpServlet {

    private ItemDataAccess itemDAO;

    @Override
    public void init() {
        // Initialize the DAO when the servlet starts
        itemDAO = new ItemDataAccess();
    }

    // ==========================================
    // 1 & 2. GET APIs (Retrieve Data)
    // ==========================================
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String pathInfo = req.getPathInfo();

            if (pathInfo == null || pathInfo.equals("/")) {
                // Endpoint 1: GET /api/items (Get ALL items)
                List<Item> items = itemDAO.getAllItems();
                ResponseBuilder.write(req, resp, items, HttpServletResponse.SC_OK);
            } else {
                // Endpoint 2: GET /api/items/{id} (Get ONE specific item)
                int id = Integer.parseInt(pathInfo.substring(1)); // Removes the "/"
                Item item = itemDAO.findItemByID(id);

                if (item != null) {
                    ResponseBuilder.write(req, resp, item, HttpServletResponse.SC_OK);
                } else {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Item not found in database.");
                }
            }
        } catch (Exception e) {
            // THIS IS THE MAGIC LINE TO ADD:
            e.printStackTrace();

            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error: " + e.getMessage());
        }
    }

    // ==========================================
    // 3. POST API (Create Data)
    // ==========================================
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            // Read JSON from Postman and turn it into an Item object
            // Read JSON from Postman and turn it into an Item object
            Item newItem = ResponseBuilder.parseBody(req, Item.class);

            // --- ADD THIS LINE TO FIX THE DATE CRASH ---
            if (newItem.getDateFound() == null)
                newItem.setDateFound(new java.util.Date());

            // Save it to SQL Server using your existing DAO
            itemDAO.saveItem(newItem);

            // Return success message
            ResponseBuilder.write(req, resp, newItem, HttpServletResponse.SC_CREATED);
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to create item: " + e.getMessage());
        }
    }

    // ==========================================
    // 4. PUT API (Update Data)
    // ==========================================
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String pathInfo = req.getPathInfo();
            if (pathInfo == null || pathInfo.equals("/")) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "You must provide an Item ID to update.");
                return;
            }

            int id = Integer.parseInt(pathInfo.substring(1));
            Item updatedItem = ResponseBuilder.parseBody(req, Item.class);

            // Inject the ID from the URL into the object
            updatedItem.setItemID(id);

            // Pass ONLY the object to the database, exactly as Eyad's code expects
            itemDAO.updateItemRecord(updatedItem);

            ResponseBuilder.write(req, resp, updatedItem, HttpServletResponse.SC_OK);
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to update item: " + e.getMessage());
        }
    }

    // ==========================================
    // 5. DELETE API (Remove Data)
    // ==========================================
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String pathInfo = req.getPathInfo();
            if (pathInfo == null || pathInfo.equals("/")) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "You must provide an Item ID to delete.");
                return;
            }

            int id = Integer.parseInt(pathInfo.substring(1));

            // Delete from SQL Server
            itemDAO.removeItem(id);

            resp.setStatus(HttpServletResponse.SC_NO_CONTENT); // 204 Status = Successfully deleted
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to delete item: " + e.getMessage());
        }
    }
}