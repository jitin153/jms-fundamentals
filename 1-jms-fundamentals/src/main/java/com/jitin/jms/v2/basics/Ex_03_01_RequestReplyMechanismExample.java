package com.jitin.jms.v2.basics;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class Ex_03_01_RequestReplyMechanismExample {

	public static void main(String[] args) throws NamingException {
		InitialContext ctx = new InitialContext();
		Queue requestQueue = (Queue) ctx.lookup("queue/requestQueue");
		Queue replyQueue = (Queue) ctx.lookup("queue/replyQueue");
		try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext()) {
			JMSProducer requestProducer = jmsContext.createProducer();
			requestProducer.send(requestQueue, "Message from JMS 2.0 API.");

			JMSConsumer requestConsumer = jmsContext.createConsumer(requestQueue);
			String rcvdMsg = requestConsumer.receiveBody(String.class);
			System.out.println("Received message: " + rcvdMsg);

			JMSProducer replyProducer = jmsContext.createProducer();
			replyProducer.send(replyQueue, "Replied to replyQueue...");
			
			JMSConsumer replyConsumer = jmsContext.createConsumer(replyQueue);
			System.out.println("Received message from reply queue: " + replyConsumer.receiveBody(String.class));
		}
	}
}
