package org.springframework.shell.jline;

import org.jline.utils.AttributedString;

/**
 * 在每个REPL循环时调用，以决定提示符（prompt）应显示什么内容。
 * Called at each REPL cycle to decide what the prompt should be.
 *
 * @author Eric Bottard
 */
public interface PromptProvider {

	AttributedString getPrompt();
}
