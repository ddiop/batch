package hello.filter;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

public class LogbackBatchFilter extends Filter<ILoggingEvent> {

    static final String MDC_KEY = "batch-job";

    private Mode mode = Mode.EXCLUDE_BATCH;

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    @Override
    public FilterReply decide(ILoggingEvent event) {
      //  boolean runningBatchJob = ...; // how to determine this?
        boolean runningBatchJob = event.getMDCPropertyMap().containsKey(MDC_KEY);
        if (mode == Mode.EXCLUDE_BATCH && runningBatchJob ||
                mode == Mode.BATCH_ONLY && !runningBatchJob)
        {
            return FilterReply.DENY;
        }
        return FilterReply.NEUTRAL;

    }


    enum Mode {
        BATCH_ONLY,
        EXCLUDE_BATCH
    }
}
