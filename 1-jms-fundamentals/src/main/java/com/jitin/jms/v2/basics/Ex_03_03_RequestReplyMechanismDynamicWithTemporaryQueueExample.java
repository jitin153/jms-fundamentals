package com.jitin.jms.v2.basics;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.TemporaryQueue;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class Ex_03_03_RequestReplyMechanismDynamicWithTemporaryQueueExample {

	public static void main(String[] args) throws NamingException, JMSException {
		InitialContext ctx = new InitialContext();
		Queue requestQueue = (Queue) ctx.lookup("queue/requestQueue");
		//Queue replyQueue = (Queue) ctx.lookup("queue/replyQueue");
		try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext()) {
			JMSProducer requestProducer = jmsContext.createProducer();
			TextMessage msgToBeSent = jmsContext.createTextMessage("Message from JMS 2.0 API.");
			// Creating temporary queue for reply programatically rather asking the JMS broker to create it.
			TemporaryQueue replyQueue = jmsContext.createTemporaryQueue();
			// Setting destination queue for reply...
			msgToBeSent.setJMSReplyTo(replyQueue);
			requestProducer.send(requestQueue, msgToBeSent);

			JMSConsumer requestConsumer = jmsContext.createConsumer(requestQueue);
			TextMessage rcvdMsg = (TextMessage) requestConsumer.receive();
			System.out.println("Received message: " + rcvdMsg.getText());

			JMSProducer replyProducer = jmsContext.createProducer();
			// Extracting destination queue dynamically...
			replyProducer.send(rcvdMsg.getJMSReplyTo(), "Replied to replyQueue...");

			JMSConsumer replyConsumer = jmsContext.createConsumer(replyQueue);
			System.out.println("Received message from reply queue: " + replyConsumer.receiveBody(String.class));
		}
	}
}
