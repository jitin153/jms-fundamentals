package com.jitin.jms.v2.basics;

import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class Ex_01_JmsContextExample {

	public static void main(String[] args) throws NamingException {
		InitialContext ctx = new InitialContext();
		Queue queue = (Queue) ctx.lookup("queue/firstQueue");
		try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory(); JMSContext jmsContext = cf.createContext()){
			jmsContext.createProducer().send(queue, "Message from JMS 2.0 API.");
			String rcvdMsg = jmsContext.createConsumer(queue).receiveBody(String.class);
			System.out.println("Received message: "+rcvdMsg);
		}
	}
}
