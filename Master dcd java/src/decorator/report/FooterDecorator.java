package decorator.report;

import java.text.DateFormat;
import java.util.Date;

/**
 * Adds a footer to the report body.
 */
public class FooterDecorator extends ReportContentDecorator {

    public FooterDecorator(ReportContent wrapped) {
        super(wrapped);
    }

    @Override
    public String render(ReportContext ctx) {
        String base = wrapped.render(ctx);
        Date now = new Date();
        String when = DateFormat.getDateTimeInstance().format(now);
        return base + "\nFooter\nGenerated: " + when;
    }
}

