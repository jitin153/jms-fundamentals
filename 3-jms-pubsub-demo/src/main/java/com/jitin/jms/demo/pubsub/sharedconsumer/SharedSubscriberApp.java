package com.jitin.jms.demo.pubsub.sharedconsumer;

import java.util.stream.IntStream;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import com.jitin.jms.demo.pubsub.model.Employee;

public class SharedSubscriberApp {

	private static final String SHARED_SUBSCRIPTION_NAME = "shared-subscription";

	public static void main(String[] args) throws NamingException, JMSException, InterruptedException {
		InitialContext ctx = new InitialContext();
		Topic topic = (Topic) ctx.lookup("topic/employeeTopic");
		try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext()) {
			JMSConsumer consumer1 = jmsContext.createSharedConsumer(topic, SHARED_SUBSCRIPTION_NAME);
			JMSConsumer consumer2 = jmsContext.createSharedConsumer(topic, SHARED_SUBSCRIPTION_NAME);
			IntStream.iterate(1, i -> i + 2).limit(5).forEach(e -> {
				Employee emp1 = consumer1.receiveBody(Employee.class);
				System.out.println("Message received by consumer-1: \n" + emp1);
				Employee emp2 = consumer2.receiveBody(Employee.class);
				System.out.println("Message received by consumer-2: \n" + emp2);
			});
		}

	}

}
