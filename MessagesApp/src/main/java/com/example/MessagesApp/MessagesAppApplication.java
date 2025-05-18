package com.example.MessagesApp;
import com.example.MessagesApp.observer.MessageNotifier;
import com.example.MessagesApp.observer.MessageStatusObserver;
import com.example.MessagesApp.repositories.ChatRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(
		basePackages = "com.example.MessagesApp.repositories",
		considerNestedRepositories = true
)
@EnableFeignClients
public class MessagesAppApplication {
	public static void main(String[] args) {
		SpringApplication.run(MessagesAppApplication.class, args);
	}

//	@Bean
//	public MessageNotifier messageNotifier(MessageStatusObserver statusObserver) {
//		MessageNotifier notifier = new MessageNotifier();
//		notifier.addObserver(statusObserver);
//		return notifier;
//	}
}