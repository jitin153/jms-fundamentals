package com.jitin.jms.p2p.demo.clinicals;

import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.MapMessage;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import com.jitin.jms.p2p.demo.model.Patient;

public class ClinicalsApp {

	public static void main(String[] args) throws NamingException, JMSException {
		InitialContext ctx = new InitialContext();
		Queue queue = (Queue) ctx.lookup("queue/requestQueue");
		try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory(); JMSContext jmsContext = cf.createContext()){
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
			MapMessage replyMessage = (MapMessage)jmsContext.createConsumer(replyQueue).receive(30000);
			System.out.println("Is patient eligible: "+replyMessage.getBoolean("isEligible"));
		}

	}

}
