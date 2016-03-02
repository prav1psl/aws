package com.trinet.fx.gateway.util;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Quartz JOB Listener for PayCheckPushNotification Quartz Job
 * @author shbhatia
 *
 */
public class PayCheckPushNotificationJobListener implements JobListener {

	public static final String LISTENER_NAME = "PayCheckPushNotificationJobListener";
	private static final Logger log = LoggerFactory.getLogger(PayCheckPushNotificationJobListener.class);
	
	@Override
	public String getName() {
		return LISTENER_NAME;
	}

	/**
	 * Invoke the method before the Quartz Job starts processing
	 */
	@Override
	public void jobToBeExecuted(JobExecutionContext context) {

		String jobName = context.getJobDetail().getKey().toString();
		log.info("Job : " + jobName + " is going to start...");

	}

	@Override
	public void jobExecutionVetoed(JobExecutionContext context) {
		log.info("jobExecutionVetoed");
	}

	/**
	 * Invoke the method after Quartz Job has finished processing
	 */
	@Override
	public void jobWasExecuted(JobExecutionContext context,
			JobExecutionException jobException) {

		String jobName = context.getJobDetail().getKey().toString();
		log.info("Job : " + jobName + " is finished...");

		if (!jobException.getMessage().equals("")) {
			log.error("Exception thrown by: " + jobName
				+ " Exception: " + jobException.getStackTrace());
		}

	}

}
