package com.jitin.jms.v2.basics;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class Ex_02_MessagePriorityExample {

	public static void main(String[] args) throws NamingException {
		InitialContext ctx = new InitialContext();
		Queue queue = (Queue) ctx.lookup("queue/firstQueue");
		try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext()) {
			JMSProducer producer = jmsContext.createProducer();
			List<String> messages = Arrays.asList("First msg", "Second msg", "Third msg");
			// Highest priority - This should be received first.
			producer.setPriority(3); // Default priority is 4
			producer.send(queue, messages.get(0));
			// Lowest priority - This should be received at last.
			producer.setPriority(1); // Default priority is 4
			producer.send(queue, messages.get(1));
			producer.setPriority(2); // Default priority is 4
			producer.send(queue, messages.get(2));

			final JMSConsumer consumer = jmsContext.createConsumer(queue);
			IntStream.range(0, 3).forEach(idx -> {
				Message receivedMsg = consumer.receive();
				try {
					System.out.println(receivedMsg.getBody(String.class)+" -> Priority: "+receivedMsg.getJMSPriority());
				} catch (JMSException e) {
					e.printStackTrace();
				}
				//String receivedMsg = consumer.receiveBody(String.class);
				//System.out.println(receivedMsg);
			});
			// OUTPUT
			// First msg
			// Third msg
			// Second msg
		}

	}

}
