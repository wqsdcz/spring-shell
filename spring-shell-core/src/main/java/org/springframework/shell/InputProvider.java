package org.springframework.shell;

/**
 * 由能够提供用户输入的“行”（无论是通过交互方式还是批处理方式提供）的组件来实现。
 *
 * To be implemented by components able to provide a "line" of user input, whether interactively or by batch.
 *
 * @author Eric Bottard
 */
public interface InputProvider {

	/**
	 * 返回用户为调用命令而输入的文本。
	 *
	 * <p>返回{@literal null}表示输入结束，请求shell退出。</p>
	 *
	 * Return text entered by user to invoke commands.
	 *
	 * <p>Returning {@literal null} indicates end of input, requesting shell exit.</p>
	 */
	Input readInput();
}
