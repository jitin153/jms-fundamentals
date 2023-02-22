package com.jitin.jms.demo.pubsub;

import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import com.jitin.jms.demo.pubsub.model.Employee;

public class SecuritySubscriberApp {

	public static void main(String[] args) throws NamingException, JMSException {
		InitialContext ctx = new InitialContext();
		Topic topic = (Topic) ctx.lookup("topic/employeeTopic");
		try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext()) {
			Employee emp = jmsContext.createConsumer(topic).receiveBody(Employee.class);

			System.out.println("Message received by security app...\n" + emp);
		}

	}

}
