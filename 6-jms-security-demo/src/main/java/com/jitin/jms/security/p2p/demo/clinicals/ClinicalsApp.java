package com.jitin.jms.security.p2p.demo.clinicals;

import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.MapMessage;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import com.jitin.jms.security.p2p.demo.model.Patient;

/*
 * Kindly refer the src/main/resources/readme.txt file for the broker specific configuration.
 */
public class ClinicalsApp {

	private static final String USERNAME = "clinicaluser";
	private static final String PASSWORD = "clinicaluserpassword";
	
	public static void main(String[] args) throws NamingException, JMSException {
		InitialContext ctx = new InitialContext();
		Queue queue = (Queue) ctx.lookup("queue/requestQueue");
		/*
		 * If you try to run this program without credentials while creating context
		 * you'll end up having below error.
		 * ActiveMQSecurityException[errorType=SECURITY_EXCEPTION message=AMQ229032:
		 * User: null does not have permission='SEND' on address
		 * jitin.queues.request.requestQueue]
		 */
		try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext(USERNAME, PASSWORD)) {
			JMSProducer producer = jmsContext.createProducer();
			ObjectMessage objMsg = jmsContext.createObjectMessage();
			Patient patient = new Patient();
			patient.setId(1);
			patient.setName("TestPatient");
			patient.setInsuranceProvider("XYZ Insurance Company");
			patient.setCopay(30d);
			patient.setAmountToBePaid(500d);
			objMsg.setObject(patient);
			producer.send(queue, objMsg);

			Queue replyQueue = (Queue) ctx.lookup("queue/replyQueue");
			MapMessage replyMessage = (MapMessage) jmsContext.createConsumer(replyQueue).receive(30000);
			System.out.println("Is patient eligible: " + replyMessage.getBoolean("isEligible"));
		}

	}

}
