package com.trinet.push.sqs;

import java.util.List;
import java.util.Map.Entry;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;

public class MessageSender {
	
	 public static AWSCredentials credentials = null;
		public static void sendMessage(){
	     try {
	         credentials = new ProfileCredentialsProvider().getCredentials();
	     } catch (Exception e) {
	         throw new AmazonClientException(
	                 "Cannot load the credentials from the credential profiles file. " +
	                 "Please make sure that your credentials file is at the correct " +
	                 "location (~/.aws/credentials), and is in valid format.",
	                 e);
	     }

	     AmazonSQS sqs = new AmazonSQSClient(credentials);
	     Region usWest2 = Region.getRegion(Regions.US_WEST_2);
	     //sqs.setRegion(usWest2);

	     System.out.println("===========================================");
	     System.out.println("Getting Started with Amazon SQS");
	     System.out.println("===========================================\n");
	     // List queues
         System.out.println("Listing all queues in your account.\n");
         for (String queueUrl : sqs.listQueues().getQueueUrls()) {
             System.out.println("  QueueUrl: " + queueUrl);
         }
         System.out.println();
         System.out.println("Sending a message to MyQueue.\n");
         sqs.sendMessage(new SendMessageRequest("mobilepushpoc", "This is my message text."));
         // Receive messages
         System.out.println("Receiving messages from MyQueue.\n");
         ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest("mobilepushpoc");
         List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
         for (Message message : messages) {
             System.out.println("  Message");
             System.out.println("    MessageId:     " + message.getMessageId());
             System.out.println("    ReceiptHandle: " + message.getReceiptHandle());
             System.out.println("    MD5OfBody:     " + message.getMD5OfBody());
             System.out.println("    Body:          " + message.getBody());
             for (Entry<String, String> entry : message.getAttributes().entrySet()) {
                 System.out.println("  Attribute");
                 System.out.println("    Name:  " + entry.getKey());
                 System.out.println("    Value: " + entry.getValue());
             }
         }
         System.out.println();

	 }
	
	public static void main(String args[]){
		sendMessage();
	}

    
    
}
