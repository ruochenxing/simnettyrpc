package net.zxjava.common.spring;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.google.common.io.CharStreams;

import net.zxjava.client.spring.RpcReferenceParser;
import net.zxjava.server.spring.RpcRegisteryParser;

public class MyNamespaceHandlerSupport extends NamespaceHandlerSupport {

	static {
		Resource resource = new ClassPathResource("logo.txt");
		if (resource.exists()) {
			try {
				Reader reader = new InputStreamReader(resource.getInputStream(), "UTF-8");
				String text = CharStreams.toString(reader);
				System.out.println(text);
				reader.close();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("");
			System.out.println("  _ __ _ __   ___ ");
			System.out.println("| '__| '_ \\ / __|");
			System.out.println("| |  | |_) | (__ ");
			System.out.println("|_|  | .__/ \\___|");
			System.out.println("     | |        ");
			System.out.println("     |_|   ");
			System.out.println("");
		}
	}

	@Override
	public void init() {
		registerBeanDefinitionParser("registry", new RpcRegisteryParser());
		registerBeanDefinitionParser("reference", new RpcReferenceParser());
	}
}
