package com.trinet.push.sqs;

import org.apache.log4j.Logger;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
 
import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
 
@Service("myMessageProviderService")
public class MyMessageProvider {
 
    final static Logger logger = Logger.getLogger(MyMessageProvider.class);
 
    @Resource(name = "jmsTemplate")
    private JmsTemplate jmsTemplate;
 
    public void sendMessage(final String txt) {
        logger.debug(String.format("Sending message with txt: %s", txt));
        jmsTemplate.send(new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                final TextMessage msg = session.createTextMessage(txt);
                return msg;
            }
        });
        logger.debug("Message sent ");
    }
}
