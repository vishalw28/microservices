package org.techie.inventoryservice;

import com.netflix.discovery.EurekaNamespace;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
//@EnableDiscoveryClient // alternative to @EnableEurekaClient
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

	/*@Bean
	public CommandLineRunner loadData(InventoryRepository inventoryRepository){
		return args -> {
			Inventory inventory = new Inventory();
			inventory.setQty(100);
			inventory.setSkuCode("iphone_13");

			Inventory inventory1 = new Inventory();
			inventory1.setQty(0);
			inventory1.setSkuCode("iphone_13_red");
			inventoryRepository.save(inventory);
			inventoryRepository.save(inventory1);
		};
	}*/

}
