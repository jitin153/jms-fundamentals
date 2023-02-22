package com.jitin.jms.msg.grouping.demo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class MessageGroupingDemo {

	private static final String GROUP_ID_KEY = "JMSXGroupID";
	private static final String GROUP_ID_VALUE = "Group-0";

	public static void main(String[] args) throws NamingException, JMSException, InterruptedException {
		InitialContext ctx = new InitialContext();
		Queue queue = (Queue) ctx.lookup("queue/myQueue");

		Map<String, String> receivedMsgs = new ConcurrentHashMap<>();

		try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext();
				JMSContext jmsContext2 = cf.createContext()) {
			JMSProducer producer = jmsContext.createProducer();
			JMSConsumer consumer1 = jmsContext2.createConsumer(queue);
			consumer1.setMessageListener(new MyListener("Consumer-1", receivedMsgs));
			JMSConsumer consumer2 = jmsContext2.createConsumer(queue);
			consumer2.setMessageListener(new MyListener("Consumer-2", receivedMsgs));

			int noOfMsgs = 10;
			TextMessage[] msgs = new TextMessage[noOfMsgs];
			for (int idx = 0; idx < noOfMsgs; idx++) {
				msgs[idx] = jmsContext.createTextMessage("Group message - " + idx);
				/*
				 * By setting below property we are grouping bunch of messages and JMS broker
				 * will make sure that all of these message(group of messages) should be
				 * consumed by the same consumer.
				 */
				msgs[idx].setStringProperty(GROUP_ID_KEY, GROUP_ID_VALUE);
				producer.send(queue, msgs[idx]);
			}
			
			TimeUnit.SECONDS.sleep(2);
			
			for (TextMessage msg : msgs) {
				if (!receivedMsgs.get(msg.getText()).equals("Consumer-1")) {
					throw new IllegalStateException(
							"Group message - " + msg.getText() + " has gone to the wrong receiver.");
				}
			}
		}

	}

}
