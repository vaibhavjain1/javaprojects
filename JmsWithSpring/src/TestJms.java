import jmsSender.MyMessageSender;

import org.springframework.context.support.GenericXmlApplicationContext;

public class TestJms {
	public static void main(String[] args) {
		sendMessage("Hello Vaibhav1");
		receiveMessage();
	}
	
	public static void sendMessage(String message){
		GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();
		ctx.load("classpath:applicationContext.xml");
		ctx.refresh();

		MyMessageSender sender = ctx.getBean("messageSender",MyMessageSender.class);
		sender.sendMessage(message);
	}
	
	public static void receiveMessage(){
		GenericXmlApplicationContext ctx=new GenericXmlApplicationContext();
		ctx.load("classpath:applicationContext.xml");
		ctx.refresh();
		
		// To receive messages continuously.
		//while(true){} 
	}
}
