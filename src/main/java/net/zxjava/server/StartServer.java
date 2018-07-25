package net.zxjava.server;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class StartServer {
	@SuppressWarnings({ "unused", "resource" })
	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"classpath:rpc-invoke-config-server.xml");
	}
}
