package com.amazonaws.sqs;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.annotation.PostConstruct;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazon.sqs.javamessaging.AmazonSQSMessagingClientWrapper;
import com.amazon.sqs.javamessaging.SQSConnection;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.auth.SystemPropertiesCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.trinet.push.config.ApplicationConfig;

public class QueueAsyncMessageListener{
	
	private static final Logger log = LoggerFactory.getLogger(QueueAsyncMessageListener.class);

	@Autowired
	private ApplicationConfig applicationConfig;
	
//	@Autowired
//	public QueueAsyncMessageListener(ApplicationConfig applicationConfig) {
//		System.out.println("appconfig :: " + applicationConfig);
//		log.info("accessKeyId :: "+applicationConfig.getAccessKey());
//		log.info("secretKey :: "+applicationConfig.getSecretKey());
//		log.info("Queue Name :: "+applicationConfig.getQueueName());
//		
//		System.setProperty("aws.accessKeyId", applicationConfig.getAccessKey());
//		System.setProperty("aws.secretKey", applicationConfig.getSecretKey());
//		SQSConnectionFactory connectionFactory = SQSConnectionFactory.builder()
//				.withRegion(Region.getRegion(Regions.US_EAST_1))
//				.withAWSCredentialsProvider(new SystemPropertiesCredentialsProvider()).build();
//		try {
//			// Create the connection.
//			SQSConnection connection = connectionFactory.createConnection();
//
//			// Get the wrapped client
//			AmazonSQSMessagingClientWrapper client = connection.getWrappedAmazonSQSClient();
//
//			// Create the non-transacted session with AUTO_ACKNOWLEDGE mode
//			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//			
//
//			Queue queue = session.createQueue(applicationConfig.getQueueName());
//			// Create a consumer for the AWS Queue
//			MessageConsumer consumer = session.createConsumer(queue);
//			// Instantiate and set the message listener for the consumer.
//			consumer.setMessageListener(new QueueAsyncMessageReceiver());
//			// Start receiving incoming messages.
//			connection.start();
//			// Wait for 1 second. The listener onMessage() method will be
//			// invoked when a
//			// message is received.
//			Thread.sleep(1000);
//		} catch (JMSException | InterruptedException e) {
//			logMessage(e);
//		}
//
//	}

	
	/**
	 * 
	 * @param ex
	 */
	private void logMessage(Exception ex) {
		StringWriter sw = new StringWriter();
		ex.printStackTrace(new PrintWriter(sw));
		log.error(sw.toString());

	}

	public void listen() throws Exception {
		System.out.println("appconfig :: " + applicationConfig);
		log.info("accessKeyId :: "+applicationConfig.getAccessKey());
		log.info("secretKey :: "+applicationConfig.getSecretKey());
		log.info("Queue Name :: "+applicationConfig.getQueueName());
		
		System.setProperty("aws.accessKeyId", applicationConfig.getAccessKey());
		System.setProperty("aws.secretKey", applicationConfig.getSecretKey());
		SQSConnectionFactory connectionFactory = SQSConnectionFactory.builder()
				.withRegion(Region.getRegion(Regions.US_EAST_1))
				.withAWSCredentialsProvider(new SystemPropertiesCredentialsProvider()).build();
		try {
			// Create the connection.
			SQSConnection connection = connectionFactory.createConnection();

			// Get the wrapped client
			AmazonSQSMessagingClientWrapper client = connection.getWrappedAmazonSQSClient();

			// Create the non-transacted session with AUTO_ACKNOWLEDGE mode
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			

			Queue queue = session.createQueue(applicationConfig.getQueueName());
			// Create a consumer for the AWS Queue
			MessageConsumer consumer = session.createConsumer(queue);
			// Instantiate and set the message listener for the consumer.
			//consumer.setMessageListener(new QueueAsyncMessageReceiver());
			// Start receiving incoming messages.
			connection.start();
			// Wait for 1 second. The listener onMessage() method will be
			// invoked when a
			// message is received.
			Thread.sleep(1000);
		} catch (JMSException | InterruptedException e) {
			logMessage(e);
		}
		
	}
}
