package com.jitin.jms.v2.basics;

import java.io.Serializable;

import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class Ex_07_05_SendObjectMessageExampleV2 {

	public static void main(String[] args) throws NamingException, InterruptedException, JMSException {
		InitialContext ctx = new InitialContext();
		Queue queue = (Queue) ctx.lookup("queue/firstQueue");
		try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext()) {
			JMSProducer producer = jmsContext.createProducer();
			producer.send(queue, new Student(1L, "Any Student Name"));

			Student rcvdStudentMsg = jmsContext.createConsumer(queue).receiveBody(Student.class);
			System.out.println("Received object = " + rcvdStudentMsg);
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
