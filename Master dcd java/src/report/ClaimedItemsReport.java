package report;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import entity.Claim;

/**
 * Refined abstraction: claims overview (uses domain {@link Claim}).
 */
public class ClaimedItemsReport extends Report {

    private final List<Claim> claims;

    public ClaimedItemsReport(ReportFormatter formatter, List<Claim> claims) {
        super(formatter);
        this.claims = claims;
    }

    @Override
    public String getTitle() {
        return "Claims report";
    }

    @Override
    public String generateBody() {
        if (claims == null || claims.isEmpty()) {
            return "No claims to list.";
        }
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
        StringBuilder sb = new StringBuilder();
        for (Claim c : claims) {
            sb.append("Claim #").append(c.getClaimID());
            sb.append(" (Item #").append(c.getItemID()).append(")");
            sb.append(" by Student #").append(c.getStudentID());
            sb.append(" — ").append(c.getStatus() != null ? c.getStatus() : "?");
            Date d = c.getRequestDate();
            sb.append(" — ").append(d != null ? df.format(d) : "no date");
            sb.append("\n");
        }
        return sb.toString();
    }
}
