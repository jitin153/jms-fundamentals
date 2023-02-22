package com.jitin.jms.v2.basics;

import javax.jms.BytesMessage;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class Ex_07_01_SendByteMessageExample {

	public static void main(String[] args) throws NamingException, InterruptedException, JMSException {
		InitialContext ctx = new InitialContext();
		Queue queue = (Queue) ctx.lookup("queue/firstQueue");
		try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext()) {
			JMSProducer producer = jmsContext.createProducer();
			BytesMessage msg = jmsContext.createBytesMessage();
			msg.writeUTF("Dummy Data");
			msg.writeLong(123L);
			producer.send(queue, msg);

			BytesMessage rcvdMsg = (BytesMessage) jmsContext.createConsumer(queue).receive();
			System.out.println("UTF Data = "+rcvdMsg.readUTF());
			System.out.println("Long Data = "+rcvdMsg.readLong());
		}

	}

}
