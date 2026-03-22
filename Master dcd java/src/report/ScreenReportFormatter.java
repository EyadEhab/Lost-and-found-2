package report;

/**
 * On-screen style: short plain text suitable for a dialog or text area.
 */
public class ScreenReportFormatter implements ReportFormatter {

    @Override
    public String format(String title, String body) {
        return title + "\n\n" + body;
    }
}
