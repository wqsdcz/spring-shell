/*
 * Copyright 2015 the original author or authors.
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

package org.springframework.shell.standard;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于自定义{@link ShellMethod}参数的处理。
 *
 * Used to customize handling of a {@link ShellMethod} parameter.
 *
 * @author Eric Bottard
 * @author Florent Biville
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface ShellOption {

	/**
	 * 用于表示默认值为{@literal null}，这与没有默认值不同。
	 * Used to indicate that the default value is the value {@literal null}, which is different from the fact that
	 * there is no default value.
	 */
	String NULL = "__NULL__";

	/**
	 * 用于表示没有默认值（<em>）i.e.</em>参数是必填参数)。
	 * Used to indicate that there is no default value (<em>i.e.</em> parameter is mandatory).
	 */
	String NONE = "__NONE__";

	/**
	 * Marker value to indicate that heuristics should be used to derive arity.
	 */
	int ARITY_USE_HEURISTICS = -1;

	/**
	 * 在用于命名参数时，可以引用该参数的key。如果没有指定，则将使用实际的方法参数名称，并以方法{@link ShellMethod#prefix()}作为前缀。
	 * @return 用于传递此参数值的显式key
	 *
	 * The key(s) by which this parameter can be referenced
	 * when using named parameters. If none is specified, the actual method parameter name will be used, prefixed by the
	 * method {@link ShellMethod#prefix()}.
	 * @return explicit key(s) to use to pass a value for this parameter
	 */
	String[] value() default {};

	/**
	 * 返回此参数消耗的输入“words”的个数。默认值是1，除非参数类型是布尔值，在这种情况下它是0。
	 * @return 要映射到该参数的单词数
	 *
	 * Return the number of input "words" this parameter consumes. Default is 1, except when parameter type is boolean,
	 * in which case it is 0.
	 * @return the number of words to map to this parameter
	 */
	int arity() default ARITY_USE_HEURISTICS;

	/**
	 * 如果用户没有提供值，则要分配给该参数的文本（转换前）值。
	 * @return 此参数的默认值
	 * The textual (pre-conversion) value to assign to this parameter if no value is provided by the user.
	 * @return the default value for this parameter
	 */
	String defaultValue() default NONE;

	/**
	 * 返回参数的简短描述。
	 * @return 参数的描述
	 * Return a short description of the parameter.
	 * @return description of the parameter
	 */
	String help() default "";

	Class<? extends ValueProvider> valueProvider() default NoValueProvider.class;

	/**
	 * Used to indicate to the framework that the given parameter should NOT be resolved by
	 * {@code StandardParameterResolver}. This is useful if several implementations of
	 * {@code org.springframework.shell.ParameterResolver} are present, given that the standard one can work with no
	 * annotation at all.
	 * Note that this is not used anymore!
	 * @return true to indicate that the {@code StandardParameterResolver} should not be used for this parameter
	 */
	boolean optOut() default false;

	interface NoValueProvider extends ValueProvider {

	}
}
