package report;

/**
 * Simulated PDF output (no PDF library): plain text with a clear PDF-style layout.
 */
public class PdfReportFormatter implements ReportFormatter {

    @Override
    public String format(String title, String body) {
        StringBuilder sb = new StringBuilder();
        sb.append("========== PDF (simulated) ==========\n");
        sb.append(title).append("\n");
        sb.append("-------------------------------------\n");
        sb.append(body);
        sb.append("\n=====================================");
        return sb.toString();
    }
}
