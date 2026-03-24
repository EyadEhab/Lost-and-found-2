package decorator.report;

/**
 * Component in the Decorator chain for building report body content.
 */
public interface ReportContent {
    String render(ReportContext ctx);
}

