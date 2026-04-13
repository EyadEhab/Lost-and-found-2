package behaviouralpatterns.template_method.report_generation;

import entity.Claim;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class ClaimedItemsReportGenerator extends AbstractReportGenerator {

    private final List<Claim> claims;

    public ClaimedItemsReportGenerator(List<Claim> claims) {
        this.claims = claims;
    }

    @Override
    protected String buildHeader() {
        return "=== Claims Report ===\n";
    }

    @Override
    protected String buildBody() {
        if (claims == null || claims.isEmpty()) {
            return "No claims to list.\n";
        }

        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
        StringBuilder sb = new StringBuilder();
        for (Claim claim : claims) {
            sb.append("Claim #").append(claim.getClaimID());
            sb.append(" | Item #").append(claim.getItemID());
            sb.append(" | Student #").append(claim.getStudentID());
            sb.append(" | Status: ").append(claim.getStatus() != null ? claim.getStatus() : "?");
            Date requestDate = claim.getRequestDate();
            sb.append(" | Date: ").append(requestDate != null ? dateFormat.format(requestDate) : "no date");
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    protected String buildFooter() {
        int total = claims == null ? 0 : claims.size();
        return "Total claims: " + total + "\n";
    }
}
