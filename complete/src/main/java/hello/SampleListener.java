package hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class SampleListener implements JobExecutionListener {

	private static final Logger log = LoggerFactory.getLogger(SampleListener.class);


	@Override
	public void beforeJob(JobExecution jobExecution) {
		if( jobExecution.getStatus() == BatchStatus.STARTING){
			log.info("================================ starting=========");
		}
		else if(jobExecution.getStatus() == BatchStatus.STOPPING){
			log.info("================================ stopping=========");
			//job failure
		}
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		if( jobExecution.getStatus() == BatchStatus.COMPLETED ){
			//job success
			log.info("================================ completed=========");
		}
		else if(jobExecution.getStatus() == BatchStatus.FAILED){
			//job failure
			log.info("================================ failed=========");
		}
	}
}
