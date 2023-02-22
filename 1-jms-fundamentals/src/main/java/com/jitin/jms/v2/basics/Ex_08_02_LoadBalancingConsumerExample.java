package com.jitin.jms.v2.basics;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

// Might not be proper example... Need to look into it.
public class Ex_08_02_LoadBalancingConsumerExample {

	public static void main(String[] args) throws NamingException, InterruptedException, JMSException {
		InitialContext ctx = new InitialContext();
		Queue queue = (Queue) ctx.lookup("queue/firstQueue");
		try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext()) {

			JMSConsumer consumer1 = jmsContext.createConsumer(queue);
			JMSConsumer consumer2 = jmsContext.createConsumer(queue);
			for (int i = 1; i <= 10; i += 2) {
				System.out.println("Message received by consumer 1: " + consumer1.receive());
				System.out.println("Message received by consumer 2: " + consumer2.receive());
			}
		}

	}

}
