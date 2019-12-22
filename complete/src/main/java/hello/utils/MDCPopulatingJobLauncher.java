package hello.utils;

import org.slf4j.MDC;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.core.task.TaskExecutor;

/**
 * SimpleJobLauncher subclass which ensures that an MDC key {@code batch-job}
 * will be set with the name of the job to be run, both in the current thread
 * as well as in the thread used by a custom configured {@link TaskExecutor}.
 * This in turn enables filtering of log messages issued in the context of
 * batch jobs.
 */
public class MDCPopulatingJobLauncher extends SimpleJobLauncher {

    static final String MDC_KEY = "batch-job";

    @Override
    public JobExecution run(Job job, JobParameters jobParameters)
            throws JobExecutionAlreadyRunningException, JobRestartException,
            JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        MDC.put(MDC_KEY, job.getName());
        try {
            return super.run(job, jobParameters);
        } finally {
            MDC.remove(MDC_KEY);
        }
    }

    @Override
    public void setTaskExecutor(TaskExecutor taskExecutor) {
        super.setTaskExecutor(
                new MDCPopulatingTaskExecutorDecorator(taskExecutor));
    }

    private static class MDCPopulatingTaskExecutorDecorator
            implements TaskExecutor {

        private TaskExecutor targetExecutor;

        MDCPopulatingTaskExecutorDecorator(TaskExecutor targetExecutor) {
            this.targetExecutor = targetExecutor;
        }

        @Override
        public void execute(Runnable task) {
            String mdcValue = MDC.get(MDC_KEY);
            targetExecutor.execute(() -> {
                MDC.put(MDC_KEY, mdcValue);
                try {
                    task.run();
                } finally {
                    MDC.remove(MDC_KEY);
                }
            });
        }
    }
}
