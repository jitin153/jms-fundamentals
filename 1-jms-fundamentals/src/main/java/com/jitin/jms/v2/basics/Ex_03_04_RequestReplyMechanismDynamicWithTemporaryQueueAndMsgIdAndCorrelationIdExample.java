package com.jitin.jms.v2.basics;

import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.jms.TemporaryQueue;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class Ex_03_04_RequestReplyMechanismDynamicWithTemporaryQueueAndMsgIdAndCorrelationIdExample {

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
			System.out.println("Message = "+msgToBeSent.getText()+" has been sent with msg id = "+msgToBeSent.getJMSMessageID());
			Map<String, TextMessage> requestMessages = new HashMap<>();
			requestMessages.put(msgToBeSent.getJMSMessageID(), msgToBeSent);
			
			JMSConsumer requestConsumer = jmsContext.createConsumer(requestQueue);
			TextMessage rcvdMsg = (TextMessage) requestConsumer.receive();
			System.out.println("Received message: " + rcvdMsg.getText()+" | Correlation id = "+rcvdMsg.getJMSMessageID());

			JMSProducer replyProducer = jmsContext.createProducer();
			TextMessage replyMsg = jmsContext.createTextMessage("Replied to replyQueue...");
			/*
			 * Setting correlation id so that at the consumer side we'll be able to
			 * identify this reply is for which message.
			 */
			replyMsg.setJMSCorrelationID(rcvdMsg.getJMSMessageID());
			// Extracting destination queue dynamically...
			replyProducer.send(rcvdMsg.getJMSReplyTo(), replyMsg);

			JMSConsumer replyConsumer = jmsContext.createConsumer(replyQueue);
			TextMessage receivedReplyMsg = (TextMessage) replyConsumer.receive();
			System.out.println("Reply message = " + receivedReplyMsg.getText()+" received with correlation id = "+receivedReplyMsg.getJMSCorrelationID());
			
			// Getting the message back for which this reply has been be received...
			TextMessage msgForWhichReplyReceived = requestMessages.get(receivedReplyMsg.getJMSCorrelationID());
			
			System.out.println("Msg for which this reply received = "+msgForWhichReplyReceived.getText());
		}
	}
}
