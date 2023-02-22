package com.jitin.jms.v2.basics;

import java.util.stream.IntStream;

import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

//Might not be proper example... Need to look into it.
public class Ex_08_01_LoadBalancingProducerExample {

	public static void main(String[] args) throws NamingException, InterruptedException, JMSException {
		InitialContext ctx = new InitialContext();
		Queue queue = (Queue) ctx.lookup("queue/firstQueue");
		try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext()) {
			JMSProducer producer = jmsContext.createProducer();
			IntStream.iterate(0, e -> e + 1).limit(10).forEach(e -> {
				producer.send(queue, "Message from JMS 2.0 API.");
			});
		}

	}

}
