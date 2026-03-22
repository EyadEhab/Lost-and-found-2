package report;

/**
 * Bridge implementor: how report content is shown or exported (PDF-like, CSV, screen, etc.).
 */
public interface ReportFormatter {

    String format(String title, String body);
}
