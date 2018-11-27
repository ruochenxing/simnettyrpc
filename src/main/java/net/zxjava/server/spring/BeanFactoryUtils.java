package net.zxjava.server.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

public class BeanFactoryUtils implements BeanFactoryAware {
	private static BeanFactory beanFactory;

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		BeanFactoryUtils.beanFactory = beanFactory;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		if (beanFactory == null) {
			return null;
		}
		try {
			return (T) beanFactory.getBean(name);
		} catch (NoSuchBeanDefinitionException e) {
			return null;
		}
	}

}
