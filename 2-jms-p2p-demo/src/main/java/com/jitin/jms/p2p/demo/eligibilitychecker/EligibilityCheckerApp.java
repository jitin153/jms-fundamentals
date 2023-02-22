package com.jitin.jms.p2p.demo.eligibilitychecker;

import java.util.concurrent.TimeUnit;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class EligibilityCheckerApp {

	public static void main(String[] args) throws NamingException, JMSException, InterruptedException {
		InitialContext ctx = new InitialContext();
		Queue queue = (Queue) ctx.lookup("queue/requestQueue");
		try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext()) {
			JMSConsumer consumer = jmsContext.createConsumer(queue);
			consumer.setMessageListener(new EligibilityCheckListener());
			TimeUnit.SECONDS.sleep(10000);
		}
	}

}
