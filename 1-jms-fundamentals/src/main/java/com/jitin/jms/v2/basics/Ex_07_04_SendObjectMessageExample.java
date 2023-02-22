package com.jitin.jms.v2.basics;

import java.io.Serializable;

import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class Ex_07_04_SendObjectMessageExample {

	public static void main(String[] args) throws NamingException, InterruptedException, JMSException {
		InitialContext ctx = new InitialContext();
		Queue queue = (Queue) ctx.lookup("queue/firstQueue");
		try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext()) {
			JMSProducer producer = jmsContext.createProducer();
			ObjectMessage objMsg = jmsContext.createObjectMessage();
			objMsg.setObject(new Student(1L, "Any Student Name"));
			producer.send(queue, objMsg);

			ObjectMessage rcvdMsg = (ObjectMessage) jmsContext.createConsumer(queue).receive();
			Student student = (Student) rcvdMsg.getObject();
			System.out.println("Received object = " + student);
		}

	}

	public static class Student implements Serializable {

		private static final long serialVersionUID = -9030878319456867041L;

		private Long id;
		private String name;

		public Student(Long id, String name) {
			this.id = id;
			this.name = name;
		}

		@Override
		public String toString() {
			return "Student [id=" + id + ", name=" + name + "]";
		}

	}

}
