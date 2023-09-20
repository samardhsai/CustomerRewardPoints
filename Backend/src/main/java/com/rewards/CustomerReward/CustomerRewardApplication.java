package com.rewards.CustomerReward;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@ComponentScan(basePackages = {"com.rewards.CustomerReward.*"})
public class CustomerRewardApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerRewardApplication.class, args);
	}

}
