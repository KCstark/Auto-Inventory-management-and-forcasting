package com.inventory_forcasting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableAsync
public class InventoryForcastingApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryForcastingApplication.class, args);
	}

}
