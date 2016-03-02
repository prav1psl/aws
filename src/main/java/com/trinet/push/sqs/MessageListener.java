package com.trinet.push.sqs;


import org.apache.log4j.Logger;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

@Service("myMessageConsumerService")
public class MessageListener {

   final static Logger logger = Logger.getLogger(MessageListener.class);

   @Resource(name = "jmsTemplate")
   private JmsTemplate jmsTemplate;

   public void readMessage() throws JMSException {
       logger.debug("Reading message");
       Message msg = jmsTemplate.receive();

       if (msg instanceof TextMessage) {
           TextMessage txtmsg = (TextMessage) msg;
           logger.info(String.format("Received text: %s", txtmsg.getText()));

       }
       logger.debug(msg.getClass());
       logger.info("Done");
   }
}