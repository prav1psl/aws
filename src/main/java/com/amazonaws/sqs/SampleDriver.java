//*********************************************************************************************************************
//Copyright 2008 Amazon Technologies, Inc.  
//Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in 
//compliance with the License. 

//You may obtain a copy of the License at:http://aws.amazon.com/apache2.0  This file is distributed on 
//an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 

//See the License for the specific language governing permissions and limitations under the License. 
//*********************************************************************************************************************
package com.amazonaws.sqs;

import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;

import com.amazon.sqs.javamessaging.AmazonSQSMessagingClientWrapper;
import com.amazon.sqs.javamessaging.SQSConnection;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.sqs.util.QMessage;
import com.amazonaws.sqs.util.QueueException;

public class SampleDriver {

    static public String accessKeyId = "AKIAJ5AMCH2IEG7HBYXA";
    static public String secretAccessKey = "U6NFpXvm6LZ4KeIAc6GujcL6rvSGc4r3o1ZHaCqY";
    static public String QueueServiceURL = "http://queue.amazonaws.com/";
    static private String queueName = "SQS-Test-Queue-Java";
    static private String testMessage = "This is a test message.";

    static Session session;
    static SQSConnection connection;
    static Queue queue;
    static MessageProducer producer;
    
    public static Queue createConnection() throws JMSException{
    	// Create the connection factory using the environment variable credential 
    	//provider.
    	// Connections this factory creates can talk to the queues in us-east-1 region.
    	SQSConnectionFactory connectionFactory =
    	 SQSConnectionFactory.builder()
    	 .withRegion(Region.getRegion(Regions.US_EAST_1))
    	 .withAWSCredentialsProvider(new EnvironmentVariableCredentialsProvider())
    	 .build();
    	// Create the connection.
    	connection = connectionFactory.createConnection();

    	// Get the wrapped client
    	AmazonSQSMessagingClientWrapper client = connection.getWrappedAmazonSQSClient();
    	// Create an SQS queue named 'TestQueue' – if it does not already exist.
    	if (!client.queueExists("mobilepushpoc")) {
    	 client.createQueue("TestQueue");
    	}

    	// Create the non-transacted session with AUTO_ACKNOWLEDGE mode
    	session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    	
    	// Create a queue identity with name 'TestQueue' in the session
    	QueueSession queueSession = connection.createQueueSession(false,Session.AUTO_ACKNOWLEDGE);
        //QueueSender queueSender = queueSession.createSender(queue);
    	// Create a producer for the 'TestQueue'.
    	producer = queueSession.createProducer(queue);
    	
    	// Create the text message.
    	TextMessage message = session.createTextMessage("Hello World!");

    	// Send the message.
    	producer.send(message);
    	System.out.println("JMS Message " + message.getJMSMessageID());
    	
    	return queue;
    }
    
    public static void receiveMsgSynch(Queue queue) throws JMSException{
    	// Create a consumer for the 'TestQueue'.
    	MessageConsumer consumer = session.createConsumer(queue);
    	// Start receiving incoming messages.
    	connection.start();

    	// Receive a message from 'TestQueue' and wait up to 1 second
    	Message receivedMessage = consumer.receive(1000);
    	// Cast the received message as TextMessage and print the text to screen.
    	if (receivedMessage != null) {
    	 System.out.println("Received: " + ((TextMessage) receivedMessage).getText());
    	}
    	
    	// Close the connection (and the session).
    	connection.close();


    }
    
    public static void sendMessage(int i) throws JMSException{
    	// Create the text message.
    	TextMessage message = session.createTextMessage("Hello World!"+i);

    	// Send the message.
    	producer.send(message);
    	System.out.println("JMS Message " + message.getJMSMessageID());
    }
    public static void receiveMsgAsynch() throws JMSException, InterruptedException{
    	// Create a consumer for the 'TestQueue'.
    	MessageConsumer consumer = session.createConsumer(queue);
    	// Instantiate and set the message listener for the consumer.
    	consumer.setMessageListener(new MyListener());
    	// Start receiving incoming messages.
    	connection.start();
    	// Wait for 1 second. The listener onMessage() method will be invoked when a 
    	//message is received.
    	Thread.sleep(1000);
    	//connection.close();

    }
    public static void main(String[] args) throws Exception {
    	Queue queue = createConnection();
    	//receiveMsgSynch(queue);
    	receiveMsgAsynch();
    	int count = 1;
    	do{
    		sendMessage(count);
    		Thread.sleep(1000);
    		count++;
    	}while(count < 5);
    }
    
    public void testqueue() throws Exception{
        System.out.println("Amazon Sample SQS Java application");
        System.out.println("  - SQS WSDL 2008-01-01");
        System.out.println("For demonstration purposes only");
        System.out.println("--------------------------------------");

        // Before running this sample, you need to paste in your own accessKeyId
        // and your own accessKey
        if (accessKeyId.equals("") || secretAccessKey.equals("")) {
            System.out
            .println("Please paste the values for your accessKey and your accessKeyId into the program before running the sample.");
            System.exit(1);
        }

        AWSQueue testQueue = null;

        // try out queue operations
        // create a queue
        boolean retry = false;
        do {
            retry = false;
            try {
                testQueue = AWSQueue.createQueue(queueName);
                System.out.println("Queue created: " + testQueue.getQueueEndpoint());              
            } catch (QueueException e) {
                System.out.println("CreateQueue failed with error: " + e.getErrorCode());
                if (e.getErrorCode().equals("AWS.SimpleQueueService.QueueDeletedRecently")) {
                    // let's wait 60 seconds before re-creating the queue
                    System.out.println("Recently Deleted Queue, wait 60 seconds");
                    Thread.sleep(60 * 1000); // wait 60 seconds
                    retry = true;
                } else {
                    // some other exception, rethrow
                    throw e;
                }
            }
        } while (retry);

        // list all my queues, verify our queue exists
        do {
            retry = true;
            List<AWSQueue> queues = AWSQueue.listQueues("SQS-Test-Queue-Java");
            System.out.println("Looking for queue " + testQueue.getQueueEndpoint());
            
            for(AWSQueue queue : queues) {
                if(queue.getQueueEndpoint().equals(testQueue.getQueueEndpoint())) {
                    System.out.println("Queue found");
                    retry = false; 
                }
            }
            if(retry == true) {
                // If we didn't find the queue, give SQS a chance to
                // propagate...
                System.out.println("Queue not available yet - keep polling\r");
                Thread.sleep(10 * 1000);    // wait 10 seconds
            }
        } while(retry);

        // send a message
        String msgId = testQueue.sendMessage(testMessage).getId();
        System.out.println("Message sent, message id: " + msgId);

        // Get Approximate Queue Count...
        // Since SQS is a distributed system, the count may not be accurate.
        String qCount = testQueue.getApproximateNumberOfMessages();
        System.out.println("Approximate Number of Messages: " + qCount);

        // now receive a message
        // because SQS is a distributed system, we need to poll until we get
        // the message
        List<QMessage> messages = testQueue.receiveMessage(1);
        do {
            Thread.sleep(1000);  // wait for a second
            messages = testQueue.receiveMessage(1);
        } while (messages.size() == 0);


        QMessage message = messages.get(0);

        System.out.println("\nMessage received");
        System.out.println("  message id:     " + message.getId());
        System.out.println("  receipt handle: " + message.getReceiptHandle());
        System.out.println(" message content: " + message.getContent());

        testQueue.deleteMessage(message.getReceiptHandle());
        System.out.println("Deleted the message.");
    }
}
