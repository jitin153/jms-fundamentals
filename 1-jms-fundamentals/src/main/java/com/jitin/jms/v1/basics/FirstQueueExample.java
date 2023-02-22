package com.jitin.jms.v1.basics;

import java.util.Objects;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class FirstQueueExample {

	public static void main(String[] args) {
		InitialContext ctx = null;
		Connection connection = null;
		try {
			/*
			 * Commented code is equivalent to jndi.properties. If you don't have jndi.properties
			 * file on classpath you can achieve the same thing by using below lines of code.
			 */
			/*Hashtable<String, String> jndiProps = new Hashtable<>();
			jndiProps.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.artemis.jndi.ActiveMQInitialContextFactory");
			jndiProps.put(Context.PROVIDER_URL, "tcp://localhost:61616");
			jndiProps.put("queue.queue/firstQueue", "firstQueue");
			ctx = new InitialContext(jndiProps);*/
			
			ctx = new InitialContext();
			ConnectionFactory cf = (ConnectionFactory) ctx.lookup("ConnectionFactory");
			connection = cf.createConnection();
			Session session = connection.createSession();
			Queue queue = (Queue) ctx.lookup("queue/firstQueue");
			MessageProducer producer = session.createProducer(queue);
			TextMessage msg = session.createTextMessage("Hello World!");
			producer.send(msg);
			System.out.println("Message sent: " + msg.getText());
			
			MessageConsumer consumer = session.createConsumer(queue);
			connection.start();
			TextMessage receivedMsg = (TextMessage) consumer.receive(5000);
			System.out.println("Message received: " + receivedMsg.getText());
		} catch (NamingException | JMSException e) {
			e.printStackTrace();
		} finally {
			if (Objects.nonNull(ctx)) {
				try {
					ctx.close();
				} catch (NamingException e) {
					e.printStackTrace();
				}
			}
			if (Objects.nonNull(connection)) {
				try {
					connection.close();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
