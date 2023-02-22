package com.jitin.jms.v1.basics;

import java.util.Enumeration;
import java.util.Objects;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class QueueBrowserExample {

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
			TextMessage msg1 = session.createTextMessage("First Message");
			TextMessage msg2 = session.createTextMessage("Second Message");
			producer.send(msg1);
			producer.send(msg2);
			
			QueueBrowser browser = session.createBrowser(queue);
			Enumeration messagesEnum = browser.getEnumeration();
			while(messagesEnum.hasMoreElements()) {
				TextMessage msg = (TextMessage)messagesEnum.nextElement();
				System.out.println("Message in the queue before it gets consumed: "+msg.getText());
			}
			
			MessageConsumer consumer = session.createConsumer(queue);
			connection.start();
			TextMessage receivedMsg = (TextMessage) consumer.receive(5000);
			System.out.println("Message received: " + receivedMsg.getText());
			receivedMsg = (TextMessage) consumer.receive(5000);
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
