package com.trinet.push.sqs;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
 
import javax.jms.JMSException;
 
public class SpringMain {
 
    final static Logger logger = Logger.getLogger(SpringMain.class);
 
    public static void main(String[] args) {
        //Build application context by reading spring-config.xml
        ApplicationContext ctx = new ClassPathXmlApplicationContext(new String[]{"claspath:application-context.xml"});
 
        //Get an instance of ProductService class;
        MyMessageProvider prdSvc = (MyMessageProvider) ctx.getBean("myMessageProviderService");
 
        MessageListener conSvc = (MessageListener) ctx.getBean("myMessageConsumerService");
 
        //Call getProduct method of ProductService
        prdSvc.sendMessage("This is a test");
 
        try {
            conSvc.readMessage();
        } catch (JMSException e) {
            logger.error(e);
        }
    }
}

