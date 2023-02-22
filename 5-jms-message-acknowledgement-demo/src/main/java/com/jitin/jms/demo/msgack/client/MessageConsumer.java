package com.jitin.jms.demo.msgack.client;

import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class MessageConsumer {

	public static void main(String[] args) throws NamingException, JMSException {
		InitialContext ctx = new InitialContext();
		Queue claimQueue = (Queue) ctx.lookup("queue/ackDemoQueue");
		try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext(JMSContext.CLIENT_ACKNOWLEDGE)) {
			TextMessage msg = (TextMessage)jmsContext.createConsumer(claimQueue).receive();
			System.out.println("Received message: " + msg.getText());
			/*
			 * If we remove the below method call and try to run consumer again & again
			 * we'll see that the same message is being consumed again & again.
			 */
			msg.acknowledge();
		}

	}

}
