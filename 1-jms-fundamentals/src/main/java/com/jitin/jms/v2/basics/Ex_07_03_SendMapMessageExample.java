package com.jitin.jms.v2.basics;

import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.MapMessage;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class Ex_07_03_SendMapMessageExample {

	public static void main(String[] args) throws NamingException, InterruptedException, JMSException {
		InitialContext ctx = new InitialContext();
		Queue queue = (Queue) ctx.lookup("queue/firstQueue");
		try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext()) {
			JMSProducer producer = jmsContext.createProducer();
			MapMessage mapMsg = jmsContext.createMapMessage();
			mapMsg.setBoolean("isActive", true);
			mapMsg.setString("username", "testuser01");
			producer.send(queue, mapMsg);

			MapMessage rcvdMsg = (MapMessage) jmsContext.createConsumer(queue).receive();
			System.out.println("isActive = "+rcvdMsg.getBoolean("isActive"));
			System.out.println("username = "+rcvdMsg.getString("username"));
		}

	}

}
