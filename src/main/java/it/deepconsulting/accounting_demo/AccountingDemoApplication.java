package it.deepconsulting.accounting_demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class AccountingDemoApplication {

	private static ApplicationContext context;

	public static void main(String[] args) {

		context = SpringApplication.run(AccountingDemoApplication.class, args);

	}

	public static ApplicationContext getContext() {
		return context;
	}

}
