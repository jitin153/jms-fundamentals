package com.jitin.jms.p2p.demo.eligibilitychecker;

import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import com.jitin.jms.p2p.demo.model.Patient;

public class EligibilityCheckListener implements MessageListener{

	@Override
	public void onMessage(Message message) {
		try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext()) {
			//ObjectMessage objectMessage = (ObjectMessage)message;
			//Patient patient = (Patient)objectMessage.getObject();
			Patient patient = message.getBody(Patient.class);
			
			MapMessage replyMsg = jmsContext.createMapMessage();
			System.out.println("Insurance provider is: "+patient.getInsuranceProvider());
			if(patient.getInsuranceProvider().equalsIgnoreCase("XYZ Insurance Company") && patient.getCopay() < 40 && patient.getAmountToBePaid() < 1000) {
				replyMsg.setBoolean("isEligible", true);
			} else {
				replyMsg.setBoolean("isEligible", false);
			}
			// Sending reply to the replyQueue
			InitialContext ctx= new InitialContext();
			Queue replyQueue = (Queue) ctx.lookup("queue/replyQueue");
			JMSProducer replyProducer = jmsContext.createProducer();
			replyProducer.send(replyQueue, replyMsg);
		}catch(NamingException | JMSException e) {
			e.printStackTrace();
		}
	}

}
