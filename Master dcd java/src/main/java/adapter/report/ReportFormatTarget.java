package adapter.report;

/**
 * Target interface for the Report Adapter.
 * It defines the method the reporting system expects.
 */
public interface ReportFormatTarget {
    String getFormattedReport();
}
