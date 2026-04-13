package behaviouralpatterns.template_method.report_generation;

/**
 * Template Method Pattern - Abstract Class
 *
 * Defines the fixed workflow for report generation while allowing subclasses
 * to provide report-specific behavior for each step.
 */
public abstract class AbstractReportGenerator {

    /**
     * Template method: defines the report generation algorithm skeleton.
     */
    public final String generateReport() {
        StringBuilder report = new StringBuilder();

        report.append(buildHeader());
        report.append(buildBody());
        report.append(buildFooter());

        postProcess(report);
        return report.toString();
    }

    protected abstract String buildHeader();

    protected abstract String buildBody();

    protected abstract String buildFooter();

    /**
     * Optional hook for subclasses that need post-processing.
     */
    protected void postProcess(StringBuilder report) {
        // Default no-op hook.
    }
}
