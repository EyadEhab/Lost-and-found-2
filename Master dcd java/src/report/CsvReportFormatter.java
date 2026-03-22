package report;

/**
 * Simulated CSV: title and body as quoted fields (no external CSV library).
 */
public class CsvReportFormatter implements ReportFormatter {

    @Override
    public String format(String title, String body) {
        String safeTitle = escapeCsv(title);
        String safeBody = escapeCsv(body.replace("\n", " | "));
        return "type,content\ntitle," + safeTitle + "\nbody," + safeBody;
    }

    private static String escapeCsv(String s) {
        if (s == null) {
            return "\"\"";
        }
        return "\"" + s.replace("\"", "\"\"") + "\"";
    }
}
