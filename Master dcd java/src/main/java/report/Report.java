package report;

/**
 * Bridge abstraction: report *kind*; presentation is delegated to ReportFormatter.
 */
public abstract class Report {

    protected final ReportFormatter formatter;

    protected Report(ReportFormatter formatter) {
        this.formatter = formatter;
    }

    public abstract String getTitle();

    /** Report-specific data turned into readable lines. */
    public abstract String generateBody();

    public String export() {
        return formatter.format(getTitle(), generateBody());
    }
}
