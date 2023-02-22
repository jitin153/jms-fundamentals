package com.jitin.jms.v2.basics;

import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class Ex_04_02_ConsumeExpiredQueueExample {

	public static void main(String[] args) throws NamingException, InterruptedException {
		InitialContext ctx = new InitialContext();
		Queue queue = (Queue) ctx.lookup("queue/firstQueue");
		try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory(); JMSContext jmsContext = cf.createContext()){
			JMSProducer producer = jmsContext.createProducer();
			/*
			 * Setting timeToLive property to 2000ms.
			 * This message will remain for 2 seconds in the queue.
			 * After that message will get moved to expiry queue.
			 */
			producer.setTimeToLive(2000);
			producer.send(queue, "Message from JMS 2.0 API.");
			Thread.sleep(3000);
			// Setting timeout in receive() method. Receives the next message that arrives within the specified timeout interval.
			Message rcvdMsg = jmsContext.createConsumer(queue).receive(4000);
			System.out.println("Received message: "+rcvdMsg);
			
			// Consuming expired messages from ExpiryQueue
			Queue expiryQueue = (Queue) ctx.lookup("queue/expiryQueue");
			System.out.println("Message from expiry queue = "+jmsContext.createConsumer(expiryQueue).receiveBody(String.class));
		}

	}

}
