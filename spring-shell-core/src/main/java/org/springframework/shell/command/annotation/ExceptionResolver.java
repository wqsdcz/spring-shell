/*
 * Copyright 2022 the original author or authors.
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
package org.springframework.shell.command.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记在特定的命令类 或
 * 用于在特定命令类及其方法中处理异常的注解。
 * Annotation for handling exceptions in specific command classes and/or its methods.
 *
 * @author Janne Valkealahti
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface ExceptionResolver {

	/**
	 *
	 * Exceptions handled by the annotated method. If empty, will default to any
	 * exceptions listed in the method argument list.
	 * <p>翻译：由被标记的方法处理的异常集合。如果为空，那么默认为方法参数列表中列出的异常。</p>
	 *
	 * @return Exceptions handled by annotated method
	 */
	Class<? extends Throwable>[] value() default {};
}
