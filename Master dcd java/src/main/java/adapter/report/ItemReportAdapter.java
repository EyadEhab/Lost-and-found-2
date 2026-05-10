package adapter.report;

import entity.Item;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Adapter that connects a list of Item objects
 * to the reporting system which expects a formatted String (like CSV).
 */
public class ItemReportAdapter implements ReportFormatTarget {
    
    private List<Item> items;

    public ItemReportAdapter(List<Item> items) {
        this.items = items;
    }

    @Override
    public String getFormattedReport() {
        if (items == null || items.isEmpty()) {
            return "No Item Data";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("ItemID, Title, Category, DateFound, Status\n");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        for (Item item : items) {
            String date = item.getDateFound() != null ? sdf.format(item.getDateFound()) : "Unknown Date";
            
            // Convert Item data into a CSV-like formatted string
            sb.append(String.format("%d, \"%s\", \"%s\", %s, \"%s\"\n", 
                    item.getItemID(),
                    item.getTitle() != null ? item.getTitle().replace("\"", "\"\"") : "No Title",
                    item.getCategory() != null ? item.getCategory().replace("\"", "\"\"") : "No Category",
                    date,
                    item.getStatus() != null ? item.getStatus().replace("\"", "\"\"") : "Unknown Setup"
            ));
        }
        
        return sb.toString();
    }
    
    public void exportToCsv(String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(getFormattedReport());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
