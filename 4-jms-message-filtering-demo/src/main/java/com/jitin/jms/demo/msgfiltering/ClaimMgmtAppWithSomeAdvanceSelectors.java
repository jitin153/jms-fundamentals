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

public class ClaimMgmtAppWithSomeAdvanceSelectors {

	private static final String MSG_SELECTOR_1 = "hospitalId=1";
	private static final String MSG_SELECTOR_2 = "claimAmount BETWEEN 10000 AND 20000";
	private static final String MSG_SELECTOR_3 = "doctorName LIKE 'A%'";
	private static final String MSG_SELECTOR_4 = "insuranceProvider IN ('XYZ Company','PQR Company')";
	private static final String MSG_SELECTOR_5 = "insuranceProvider IN ('XYZ Company','PQR Company') OR JMSPriority BETWEEN 3 AND 6";
	
	public static void main(String[] args) throws NamingException, JMSException {
		InitialContext ctx = new InitialContext();
		Queue claimQueue = (Queue) ctx.lookup("queue/claimQueue");
		try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext()) {
			JMSProducer producer = jmsContext.createProducer();

			//JMSConsumer comsumer = jmsContext.createConsumer(claimQueue, MSG_SELECTOR_1);
			//JMSConsumer comsumer = jmsContext.createConsumer(claimQueue, MSG_SELECTOR_2);
			//JMSConsumer comsumer = jmsContext.createConsumer(claimQueue, MSG_SELECTOR_3);
			//JMSConsumer comsumer = jmsContext.createConsumer(claimQueue, MSG_SELECTOR_4);
			JMSConsumer comsumer = jmsContext.createConsumer(claimQueue, MSG_SELECTOR_5); // It'll evaluate to true due to OR operator in selector.
			
			ObjectMessage objMsg = jmsContext.createObjectMessage();
			//objMsg.setIntProperty("hospitalId", 1);
			//objMsg.setDoubleProperty("claimAmount", 12000);
			//objMsg.setStringProperty("doctorName", "ABC");
			//objMsg.setStringProperty("insuranceProvider", "XYZ Company");
			objMsg.setStringProperty("insuranceProvider", "KLM Company"); // // It'll evaluate to true due to OR operator in selector.
			
			Claim claim = new Claim();
			claim.setId(1);
			claim.setHospitalId(1001);
			claim.setDoctorName("ABC");
			claim.setInsuranceProvider("XYZ Company");
			claim.setClaimAmount(12000d);
			objMsg.setObject(claim);
			
			producer.send(claimQueue, objMsg);

			Claim msg = comsumer.receiveBody(Claim.class);
			System.out.println("Received message: " + msg);
		}

	}

}
