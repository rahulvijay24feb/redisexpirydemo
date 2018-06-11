package hello;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@SpringBootApplication
public class RedisApplication {
	private static final Logger logger = LoggerFactory.getLogger(RedisApplication.class);

	@Bean
	RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,MessageListenerAdapter listenerAdapter) {
	RedisMessageListenerContainer container = new RedisMessageListenerContainer();
	container.setConnectionFactory(connectionFactory);
	container.addMessageListener(listenerAdapter, new PatternTopic( "__key*__:expired"));
	return container;
	}

	@Bean
	MessageListenerAdapter listenerAdapter(RedisReceiver receiver) {
		return new MessageListenerAdapter(receiver, "receiveMessage");
	}

	@Bean
	RedisReceiver receiver(CountDownLatch latch) {
		return new RedisReceiver(latch);
	}

	@Bean
	CountDownLatch latch() {
		return new CountDownLatch(1);
	}

	@Bean
	StringRedisTemplate template(RedisConnectionFactory connectionFactory) {
		return new StringRedisTemplate(connectionFactory);
	}
	
	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		return new LettuceConnectionFactory(new RedisStandaloneConfiguration("192.168.84.170", 6379));
	}

	public static void main(String args[]) throws InterruptedException {
		ApplicationContext ctx = SpringApplication.run(RedisApplication.class, args);
		StringRedisTemplate template = ctx.getBean(StringRedisTemplate.class);
		CountDownLatch latch = ctx.getBean(CountDownLatch.class);
		logger.info("Sending Message..");
		template.convertAndSend("chat", "Hello Rahul");
		//latch.wait();
		boolean start = true;
		System.out.println("Connected --->");
		while(start){
		
		}
	}
}
