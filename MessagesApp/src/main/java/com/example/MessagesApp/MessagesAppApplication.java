package com.example.MessagesApp;
import com.example.MessagesApp.observer.MessageNotifier;
import com.example.MessagesApp.observer.MessageStatusObserver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MessagesAppApplication {
	public static void main(String[] args) {
		SpringApplication.run(MessagesAppApplication.class, args);
	}

	@Bean
	public MessageNotifier messageNotifier(MessageStatusObserver statusObserver) {
		MessageNotifier notifier = new MessageNotifier();
		notifier.addObserver(statusObserver);
		return notifier;
	}
}