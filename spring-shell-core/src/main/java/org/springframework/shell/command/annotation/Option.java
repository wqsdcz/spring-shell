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

import org.springframework.shell.command.CommandRegistration.OptionArity;

/**
 * Annotation marking a method parameter to be a candidate for an option.
 * <p>翻译：用于标记方法参数。被标记的方法参数将作为一个命令选项的一个候选。</p>
 * <>参数属性：参数的长名称、参数的短名称、是否必选、参数的默认值、参数的描述、参数的标签、参数的元数</>
 * @author Janne Valkealahti
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@Documented
public @interface Option {

	/**
	 * Long names of an option. There can be multiple names where first is primary
	 * one and other are aliases.
	 * <p>翻译：一个选项的长名称。</p>
	 *
	 * @return Option long names, defaults to empty.
	 */
	String[] longNames() default {};

	/**
	 * Short names of an option. There can be multiple names where first is primary
	 * one and other are aliases.
	 * <p>翻译：一个选项的短名称。</p>
	 *
	 * @return Option short names, defaults to empty.
	 */
	char[] shortNames() default {};

	/**
	 * Mark option required.
	 * <p>翻译：标记选项为必需选项。</p>
	 *
	 * @return true if option is required, defaults to false.
	 */
	boolean required() default false;

	/**
	 * Define option default value.
	 * <p>翻译：定义选项的默认值。</p>
	 *
	 * @return default value
	 */
	String defaultValue() default "";

	/**
	 * Return a short description of the option.
	 * <p>翻译：返回选项的简短描述。</p>
	 *
	 * @return description of the option
	 */
	String description() default "";

	/**
	 * Return a label of the option.
	 * <p>翻译：返回选项的标签。</p>
	 *
	 * @return label of the option
	 */
	String label() default "";

	/**
	 * Define option arity.
	 * <p>翻译：定义选项的元数（选项中参数的数量）。</p>
	 *
	 * @return option arity
	 * @see #arityMin()
	 * @see #arityMax()
	 */
	OptionArity arity() default OptionArity.NONE;

	/**
	 * Define option arity min. If Defined non-negative will be used instead of
	 * {@link #arity()}. If {@code arityMax} is not set non-negative it is set to
	 * same as this.
	 * <p>翻译：定义选项的元数的最小值。如果定义为非负数将被使用，而不是{@link #arity()}。如果{@code arityMax}未设置为非负，则设置为与此属性相同。</p>
	 *
	 * @return option arity min
	 * @see #arity()
	 */
	int arityMin() default -1;

	/**
	 * Define option arity max. If Defined non-negative will be used instead of
	 * {@link #arity()}. If {@code arityMin} is not set non-negative it is set to
	 * zero.
	 * <p>翻译：定义选项的元数的最大值。如果定义非负数否定将被使用，而不是{@link #arity()}。如果{@code arityMin}未设置为非负，则将其设置为零。</p>
	 *
	 * @return option arity max
	 * @see #arity()
	 */
	int arityMax() default -1;
}
