/*
 * Copyright 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.shell.command.annotation.support;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.HierarchicalBeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.core.annotation.MergedAnnotations.SearchStrategy;
import org.springframework.shell.command.annotation.Command;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils.MethodFilter;

/**
 * Delegate used by {@link EnableCommandRegistrar} and
 * {@link CommandScanRegistrar} to register a bean definition(s) for a
 * {@link Command @Command} class.
 *
 * @author Janne Valkealahti
 */
public final class CommandRegistrationBeanRegistrar {

	private final BeanDefinitionRegistry registry;
	private final BeanFactory beanFactory;
	private static final MethodFilter COMMAND_METHODS = method ->
			AnnotatedElementUtils.hasAnnotation(method, Command.class);

	public CommandRegistrationBeanRegistrar(BeanDefinitionRegistry registry) {
		this.registry = registry;
		this.beanFactory = (BeanFactory) this.registry;
	}

	/**
	 * 注册【指定类型】中的【shell命令】。
	 *
	 * @param type
	 */
	public void register(Class<?> type) {
		MergedAnnotation<Command> annotation = MergedAnnotations.from(type, SearchStrategy.TYPE_HIERARCHY)
				.get(Command.class);
		register(type, annotation);
	}

	/**
	 * 注册【指定类型】中的【shell命令】。分析@Command注解。
	 *
	 * @param type
	 * @param annotation
	 */
	void register(Class<?> type, MergedAnnotation<Command> annotation) {
		// 1、获取类型的名称
		String name = type.getName();
		if (!containsBeanDefinition(name)) {
			// 2、如果【指定type的BeanDefinition】不存在，那么就注册【指定type的BeanDefinition】
			registerCommandClassBeanDefinition(name, type, annotation);
		}
		// 3、扫描【指定type的@Command的注解】
		scanMethods(type, name, annotation);
	}

	/**
	 *
	 * @param type 添加@Command的类型
	 * @param containerBean  类型的名称
	 * @param classAnnotation
	 */
	void scanMethods(Class<?> type, String containerBean, MergedAnnotation<Command> classAnnotation) {
		Set<Method> methods = MethodIntrospector.selectMethods(type, COMMAND_METHODS);
		methods.forEach(m -> {
			String name = type.getName();
			String methodName = m.getName();  // 方法名
			Class<?>[] methodParameterTypes = m.getParameterTypes();  // 方法参数类型
			String postfix = Stream.of(methodParameterTypes).map(clazz -> ClassUtils.getShortName(clazz))
					.collect(Collectors.joining());
			// 【全限定类名】/【方法名】【方法参数类型的短类名的拼接】
			name = name +  "/" + methodName + postfix;

			if (!containsBeanDefinition(name)) {
				registerCommandMethodBeanDefinition(type, name, containerBean, methodName, methodParameterTypes);
			}
		});

	}

	/**
	 * 注册明磊
	 *
	 * @param beanName
	 * @param type
	 * @param annotation
	 */
	private void registerCommandClassBeanDefinition(String beanName, Class<?> type,
			MergedAnnotation<Command> annotation) {
		Assert.state(annotation.isPresent(), () -> "No " + Command.class.getSimpleName()
				+ " annotation found on  '" + type.getName() + "'.");
		this.registry.registerBeanDefinition(beanName, createCommandClassBeanDefinition(type));
	}

	/**
	 * 注册命令方法的Bean定义
	 *
	 * @param commandBeanType
	 * @param commandBeanName
	 * @param containerBean
	 * @param methodName
	 * @param methodParameterTypes
	 */
	private void registerCommandMethodBeanDefinition(Class<?> commandBeanType, String commandBeanName, String containerBean, String methodName,
			Class<?>[] methodParameterTypes) {
		this.registry.registerBeanDefinition(commandBeanName,
				createCommandMethodBeanDefinition(commandBeanType, containerBean, methodName, methodParameterTypes));
	}

	private BeanDefinition createCommandClassBeanDefinition(Class<?> type) {
		RootBeanDefinition definition = new RootBeanDefinition(type);
		return definition;
	}

	/**
	 * 创建命令方法的Bean定义（CommandRegistrationFactoryBean）
	 *
	 * @param commandBeanType
	 * @param commandBeanName
	 * @param commandMethodName
	 * @param commandMethodParameters
	 * @return
	 */
	private BeanDefinition createCommandMethodBeanDefinition(Class<?> commandBeanType, String commandBeanName,
			String commandMethodName, Class<?>[] commandMethodParameters) {
		RootBeanDefinition definition = new RootBeanDefinition(CommandRegistrationFactoryBean.class);
		definition.getPropertyValues().add(CommandRegistrationFactoryBean.COMMAND_BEAN_TYPE, commandBeanType);
		definition.getPropertyValues().add(CommandRegistrationFactoryBean.COMMAND_BEAN_NAME, commandBeanName);
		definition.getPropertyValues().add(CommandRegistrationFactoryBean.COMMAND_METHOD_NAME, commandMethodName);
		definition.getPropertyValues().add(CommandRegistrationFactoryBean.COMMAND_METHOD_PARAMETERS, commandMethodParameters);
		return definition;
	}

	/**
	 * 指定Bean是否已经存在
	 *
	 * @param name
	 * @return
	 */
	private boolean containsBeanDefinition(String name) {
		return containsBeanDefinition(this.beanFactory, name);
	}

	/**
	 * 指定Bean是否已经存在
	 *
	 * @param beanFactory
	 * @param name
	 * @return
	 */
	private boolean containsBeanDefinition(BeanFactory beanFactory, String name) {
		if (beanFactory instanceof ListableBeanFactory listableBeanFactory
				&& listableBeanFactory.containsBeanDefinition(name)) {
			return true;
		}
		if (beanFactory instanceof HierarchicalBeanFactory hierarchicalBeanFactory) {
			return containsBeanDefinition(hierarchicalBeanFactory.getParentBeanFactory(), name);
		}
		return false;
	}
}
