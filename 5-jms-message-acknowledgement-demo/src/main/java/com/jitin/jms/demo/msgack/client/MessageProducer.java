package com.jitin.jms.demo.msgack.client;

import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class MessageProducer {

	public static void main(String[] args) throws NamingException, JMSException {
		InitialContext ctx = new InitialContext();
		Queue claimQueue = (Queue) ctx.lookup("queue/ackDemoQueue");
		try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext(JMSContext.CLIENT_ACKNOWLEDGE)) {
			
			JMSProducer producer = jmsContext.createProducer();
			producer.send(claimQueue, "Dummy Message...");
		}

	}

}
