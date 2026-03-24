package decorator.report;

/**
 * Base decorator for report content.
 */
public abstract class ReportContentDecorator implements ReportContent {
    protected final ReportContent wrapped;

    protected ReportContentDecorator(ReportContent wrapped) {
        this.wrapped = wrapped;
    }
}

