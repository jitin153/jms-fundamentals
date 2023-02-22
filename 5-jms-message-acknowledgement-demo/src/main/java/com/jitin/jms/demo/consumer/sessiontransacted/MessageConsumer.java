package com.jitin.jms.demo.consumer.sessiontransacted;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class MessageConsumer {

	public static void main(String[] args) throws NamingException, JMSException {
		InitialContext ctx = new InitialContext();
		Queue claimQueue = (Queue) ctx.lookup("queue/ackDemoQueue");
		try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext(JMSContext.SESSION_TRANSACTED)) {
			
			JMSConsumer consumer = jmsContext.createConsumer(claimQueue);
			System.out.println("Received message by consumer 1: " + consumer.receiveBody(String.class));
			jmsContext.commit();
			/*
			 * Below consumed message will not be removed from the queue since
			 * we are not committing the transaction. And if we run the consumer 2nd time
			 * we'll see the only one message get consumed by the 1st consumer & if run the
			 * consumer 3rd time without running producer again, there won't be any message
			 * to be consumed.
			 */
			System.out.println("Received message by consumer 2: " + consumer.receiveBody(String.class));
		}

	}

}
