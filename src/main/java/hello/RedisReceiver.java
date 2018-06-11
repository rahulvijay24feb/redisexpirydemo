package hello;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class RedisReceiver {
	public static final Logger logger = LoggerFactory.getLogger(RedisReceiver.class);
	private CountDownLatch latch;
	
	@Autowired
	public RedisReceiver(CountDownLatch latch) {
		this.latch = latch;
	}
	
	public void receiveMessage(String message) {
		logger.info("####Message Received-->"+message);
		latch.countDown();
	}
}
