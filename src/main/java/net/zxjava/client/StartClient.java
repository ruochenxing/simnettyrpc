package net.zxjava.client;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import net.zxjava.service.AddCalculate;

public class StartClient {
	public static void main(String[] args) {
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"classpath:rpc-invoke-config-client.xml");) {
			int result = ((AddCalculate) context.getBean("addCalc")).add(1, 2);
			System.out.println("calc add success,result=" + result);
		}
	}
}
