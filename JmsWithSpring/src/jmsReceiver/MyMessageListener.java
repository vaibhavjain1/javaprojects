package jmsReceiver;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class MyMessageListener implements MessageListener{

	@Override
	public void onMessage(Message m) {
		TextMessage message=(TextMessage)m;
		try{
			System.out.println(message.getText());
		}catch (Exception e) {e.printStackTrace();	}
	}

}
