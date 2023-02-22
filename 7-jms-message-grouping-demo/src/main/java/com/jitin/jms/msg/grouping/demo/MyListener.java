package com.jitin.jms.msg.grouping.demo;

import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

class MyListener implements MessageListener {

	private final String name;
	private final Map<String, String> receiveMessages;

	public MyListener(String name, Map<String, String> receiveMessages) {
		this.name = name;
		this.receiveMessages = receiveMessages;
	}

	@Override
	public void onMessage(Message message) {
		TextMessage textMsg = (TextMessage) message;
		try {
			System.out.println("Message received by listener-" + name + " : " + textMsg.getText());
			receiveMessages.put(textMsg.getText(), name);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}