package com.jitin.jms.demo.msgfiltering;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import com.jitin.jms.demo.msgfiltering.model.Claim;

public class ClaimMgmtAppWith2Consumers {

	private static final String MSG_SELECTOR_1 = "hospitalId=1";
	private static final String MSG_SELECTOR_2 = "hospitalId=2";
	/*private static final String MSG_SELECTOR_3 = "claimAmount BETWEEN 10000 AND 20000";
	private static final String MSG_SELECTOR_4 = "doctorName LIKE 'A%'";
	private static final String MSG_SELECTOR_5 = "insuranceProvider IN ('XYZ Company','PQR Company')";
	private static final String MSG_SELECTOR_6 = "insuranceProvider IN ('XYZ Company','PQR Company') OR JMSPriority BETWEEN 3 AND 6";
	*/
	public static void main(String[] args) throws NamingException, JMSException {
		InitialContext ctx = new InitialContext();
		Queue claimQueue = (Queue) ctx.lookup("queue/claimQueue");
		try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext()) {
			JMSProducer producer = jmsContext.createProducer();

			JMSConsumer consumerWithSelector1 = jmsContext.createConsumer(claimQueue, MSG_SELECTOR_1);
			JMSConsumer consumerWithSelector2 = jmsContext.createConsumer(claimQueue, MSG_SELECTOR_2);
			
			producer.send(claimQueue, prepareObjectMsg(jmsContext, 1));
			producer.send(claimQueue, prepareObjectMsg(jmsContext, 2));

			Claim msg1 = consumerWithSelector1.receiveBody(Claim.class);
			System.out.println("Message received by consumer-1: " + msg1);
			Claim msg2 = consumerWithSelector2.receiveBody(Claim.class);
			System.out.println("Message received by consumer-2: " + msg2);
		}

	}

	private static ObjectMessage prepareObjectMsg(JMSContext jmsContext, int hospitalId) throws JMSException {
		ObjectMessage objMsg = jmsContext.createObjectMessage();
		objMsg.setIntProperty("hospitalId", hospitalId);
		Claim claim = new Claim();
		claim.setId(1);
		claim.setHospitalId(1001);
		claim.setDoctorName("ABC");
		claim.setInsuranceProvider("XYZ Company");
		claim.setClaimAmount(12000d);
		objMsg.setObject(claim);
		return objMsg;
	}

}
