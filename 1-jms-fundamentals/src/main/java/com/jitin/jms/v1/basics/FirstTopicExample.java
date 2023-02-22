package com.jitin.jms.v1.basics;

import java.util.Objects;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class FirstTopicExample {

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
			jndiProps.put("topic.topic/firstTopic", "firstTopic");
			ctx = new InitialContext(jndiProps);*/
			ctx = new InitialContext();
			ConnectionFactory cf = (ConnectionFactory) ctx.lookup("ConnectionFactory");
			connection = cf.createConnection();
			Session session = connection.createSession();
			Topic topic = (Topic) ctx.lookup("topic/firstTopic");
			MessageProducer producer = session.createProducer(topic);
			/*
			 * Don't move consumer1 & consumer2 instantiation code below the producer.send(msg);
			 * otherwise receiver won't receive the message. Observe this in case of sending
			 * message to topic.
			 */
			MessageConsumer consumer1 = session.createConsumer(topic);
			MessageConsumer consumer2 = session.createConsumer(topic);
			
			TextMessage msg = session.createTextMessage("Hello World From Topic!");
			
			producer.send(msg);
			System.out.println("Message sent: " + msg.getText());
			
			
			connection.start();
			TextMessage receivedMsg1 = (TextMessage) consumer1.receive(5000);
			System.out.println("Message received by consumer 1: " + receivedMsg1.getText());
			TextMessage receivedMsg2 = (TextMessage) consumer2.receive(5000);
			System.out.println("Message received by consumer 2: " + receivedMsg2.getText());
		} catch (NamingException | JMSException e) {
			e.printStackTrace();
		} finally {
			if (Objects.nonNull(connection)) {
				try {
					connection.close();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
			if (Objects.nonNull(ctx)) {
				try {
					ctx.close();
				} catch (NamingException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
