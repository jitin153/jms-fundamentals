package com.jitin.jms.v2.basics;

import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.jms.StreamMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class Ex_07_02_SendStreamMessageExample {

	public static void main(String[] args) throws NamingException, InterruptedException, JMSException {
		InitialContext ctx = new InitialContext();
		Queue queue = (Queue) ctx.lookup("queue/firstQueue");
		try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext()) {
			JMSProducer producer = jmsContext.createProducer();
			StreamMessage msg = jmsContext.createStreamMessage();
			msg.writeBoolean(false);
			msg.writeInt(123);
			producer.send(queue, msg);

			StreamMessage rcvdMsg = (StreamMessage) jmsContext.createConsumer(queue).receive();
			System.out.println("Boolean Data = "+rcvdMsg.readBoolean());
			System.out.println("Int Data = "+rcvdMsg.readInt());
		}

	}

}
