package com.jitin.jms.demo.pubsub.durableconsumer;

import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import com.jitin.jms.demo.pubsub.model.Employee;

public class EmployeeInfoPublisherApp {

	public static void main(String[] args) throws NamingException, JMSException {
		InitialContext ctx = new InitialContext();
		Topic queue = (Topic) ctx.lookup("topic/employeeTopic");
		try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext()) {
			Employee employee = new Employee();
			employee.setId(1);
			employee.setName("TestEmployee");
			employee.setEmail("testemp@xyz.abc");
			employee.setPhone("0000000000");
			employee.setDesignation("Senior Associate");
			jmsContext.createProducer().send(queue, employee);

			System.out.println("Message sent...");
		}

	}

}
