package com.jitin.jms.security.p2p.demo.eligibilitychecker;

import java.util.concurrent.TimeUnit;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

/*
 * Kindly refer the src/main/resources/readme.txt file for the broker specific configuration.
 */
public class EligibilityCheckerApp {

	private static final String USERNAME = "eligibilityuser";
	private static final String PASSWORD = "eligibilityuserpassword";

	public static void main(String[] args) throws NamingException, JMSException, InterruptedException {
		InitialContext ctx = new InitialContext();
		Queue queue = (Queue) ctx.lookup("queue/requestQueue");
		/*
		 * If you try to run this program without credentials while creating context
		 * you'll end up having below error.
		 * ActiveMQSecurityException[errorType=SECURITY_EXCEPTION message=AMQ229213:
		 * User: null does not have permission='CREATE_DURABLE_QUEUE' for queue
		 * jitin.queues.request.requestQueue on address
		 * jitin.queues.request.requestQueue]
		 */
		try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext(USERNAME, PASSWORD)) {
			JMSConsumer consumer = jmsContext.createConsumer(queue);
			consumer.setMessageListener(new EligibilityCheckListener());
			TimeUnit.SECONDS.sleep(10000);
		}
	}

}
