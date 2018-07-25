package net.zxjava.server.spring;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * 自定义标签解析
 */
public class RpcRegisteryParser implements BeanDefinitionParser {

	@Override
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		String id = element.getAttribute("id");
		String ipAddress = element.getAttribute("ipAddress");
		String echoApiPort = element.getAttribute("echoApiPort");
		String protocolType = element.getAttribute("protocol");

		RootBeanDefinition beanDefinition = new RootBeanDefinition();
		beanDefinition.setBeanClass(RpcRegistery.class);
		beanDefinition.getPropertyValues().addPropertyValue("ipAddress", ipAddress);
		beanDefinition.getPropertyValues().addPropertyValue("echoApiPort", echoApiPort);
		beanDefinition.getPropertyValues().addPropertyValue("protocol", protocolType);
		parserContext.getRegistry().registerBeanDefinition(id, beanDefinition);
		return beanDefinition;
	}

}
