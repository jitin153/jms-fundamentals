package com.jitin.jms.demo.pubsub.durableconsumer;

import java.util.concurrent.TimeUnit;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import com.jitin.jms.demo.pubsub.model.Employee;

public class DurableSubscriberApp {

	private static final String CLIENT_ID = "DurableSubscriberApp";
	private static final String SUBSCRIBER_ID = "subscription-1";

	public static void main(String[] args) throws NamingException, JMSException, InterruptedException {
		InitialContext ctx = new InitialContext();
		Topic topic = (Topic) ctx.lookup("topic/employeeTopic");
		try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext()) {
			jmsContext.setClientID(CLIENT_ID);
			JMSConsumer consumer = jmsContext.createDurableConsumer(topic, SUBSCRIBER_ID);
			// Assume subscriber gets down here..simulating by closing the consumer.
			consumer.close();
			// Assuming it was down for 10 seconds...
			TimeUnit.SECONDS.sleep(10);
			// And consumer comes up after 10 seconds and message has already been published to the topic.
			consumer = jmsContext.createDurableConsumer(topic, SUBSCRIBER_ID);
			Employee emp = consumer.receiveBody(Employee.class);
			System.out.println("Message received by DurableSubscriberApp...\n" + emp);
			
			consumer.close();
			jmsContext.unsubscribe(SUBSCRIBER_ID);
		}

	}

}
