package com.jitin.jms.v2.basics;

import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class Ex_05_MessageDeliveryDelayExample {

	public static void main(String[] args) throws NamingException, InterruptedException, JMSException {
		InitialContext ctx = new InitialContext();
		Queue queue = (Queue) ctx.lookup("queue/firstQueue");
		try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory(); JMSContext jmsContext = cf.createContext()){
			JMSProducer producer = jmsContext.createProducer();
			// Message will get delivered after 3 seconds.
			producer.setDeliveryDelay(3000);
			producer.send(queue, "Message from JMS 2.0 API.");
			
			TextMessage rcvdMsg = (TextMessage) jmsContext.createConsumer(queue).receive();
			System.out.println("Received message: "+rcvdMsg.getText());
		}

	}

}
