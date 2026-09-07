package io.github.smk412.llmraglab.chat.service;

import io.github.smk412.llmraglab.chat.dto.ChatResponse;

public interface ChatService {

	ChatResponse ask(String question);

}
