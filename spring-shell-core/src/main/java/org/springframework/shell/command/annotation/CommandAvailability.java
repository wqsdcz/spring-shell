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
package org.springframework.shell.command.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.shell.Availability;

/**
 * Annotation marking a method having {@link Availability}.
 * <p>翻译：标注具有{@link Availability}的命令方法的注释。示例如下：</p>
 * <pre class="code">
 * &#64;Command
 * class MyCommands {
 *
 * 	private boolean connected;
 *
 * 	&#64;Command(command = "connect")
 * 	public void connect(String user, String password) {
 * 		connected = true;
 * 	}
 *
 *
 * 	&#64;Command(command = "download")
 *	&#64;CommandAvailability(provider = "downloadAvailability")
 * 	public void download() {
 * 		// do something
 * 	}
 *
 * 	&#64;Bean
 * 	public AvailabilityProvider downloadAvailability() {
 * 		return () -> connected
 * 			? Availability.available()
 * 			: Availability.unavailable("you are not connected");
 * 	}
 * }
 * </pre>
 *
 * @author Janne Valkealahti
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface CommandAvailability {

	/**
	 * Names of provider beans for {@link Availability}.
	 * <p>翻译：{@link Availability}的提供者bean名称。AvailabilityProvider类型</p>
	 *
	 *
	 * @return names of supplier beans
	 */
	String[] provider() default {};
}
