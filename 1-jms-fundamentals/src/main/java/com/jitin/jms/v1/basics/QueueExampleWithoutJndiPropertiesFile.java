package com.jitin.jms.v1.basics;

import java.util.Hashtable;
import java.util.Objects;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/*
 * This class doesn't need jndi.properties on the classpath.
 */
public class QueueExampleWithoutJndiPropertiesFile {

	public static void main(String[] args) {
		InitialContext ctx = null;
		Connection connection = null;
		try {
			/*Hashtable<String, String> jndiParams = createJndiParams("queue/firstQueue","firstQueue");
			Hashtable<String, String> jndiParams = createJndiParams("queue/firstQueue","firstQueue");
			Hashtable<String, String> jndiParams = createJndiParams("queue/firstQueue","firstQueue");
			*/
			ctx = new InitialContext(getJNDIProperties());
			ConnectionFactory cf = (ConnectionFactory) ctx.lookup("ConnectionFactory");
			connection = cf.createConnection();
			Session session = connection.createSession();
			Queue queue = (Queue) ctx.lookup("queue/firstQueue");
			MessageProducer producer = session.createProducer(queue);
			MessageConsumer consumer = session.createConsumer(queue);
			TextMessage msg = session.createTextMessage("Hello World!");
			producer.send(msg);
			System.out.println("Message sent: " + msg.getText());			
			
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
	
	private static Hashtable<String, String> getJNDIProperties() {
		Hashtable<String, String> jndiProps = new Hashtable<>();
		jndiProps.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.artemis.jndi.ActiveMQInitialContextFactory");
		jndiProps.put(Context.PROVIDER_URL, "tcp://localhost:61616");
		jndiProps.put("queue.queue/firstQueue", "firstQueue");
		/*jndiProps.put("queue.queue/secondQueue", "firstQueue");
		jndiProps.put("topic.topic/firstTopic", "firstTopic");
		jndiProps.put("topic.topic/secondTopic", "secondTopic");*/
		return jndiProps;
	}
}
