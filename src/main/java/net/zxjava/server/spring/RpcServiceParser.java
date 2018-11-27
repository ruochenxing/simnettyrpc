package net.zxjava.server.spring;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * Spring的自定义标签的实现 parser实现
 */
public class RpcServiceParser implements BeanDefinitionParser {

	@Override
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		String interfaceName = element.getAttribute("interfaceName");
		String ref = element.getAttribute("ref");
		String filter = element.getAttribute("filter");
		// 代表一个从配置源（XML，Java Config等）中生成的BeanDefinition
		RootBeanDefinition beanDefinition = new RootBeanDefinition();
		beanDefinition.setBeanClass(RpcService.class);
		beanDefinition.setLazyInit(false);
		beanDefinition.getPropertyValues().addPropertyValue("interfaceName", interfaceName);
		beanDefinition.getPropertyValues().addPropertyValue("ref", ref);
		beanDefinition.getPropertyValues().addPropertyValue("filter", filter);

		// BeanDefinitionRegistry的作用主要是向注册表中注册 BeanDefinition 实例，完成 注册的过程
		parserContext.getRegistry().registerBeanDefinition(interfaceName, beanDefinition);

		return beanDefinition;
	}

}
