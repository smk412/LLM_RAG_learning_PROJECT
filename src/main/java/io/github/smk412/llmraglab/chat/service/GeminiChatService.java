package io.github.smk412.llmraglab.chat.service;

import java.util.concurrent.TimeUnit;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import io.github.smk412.llmraglab.chat.dto.ChatResponse;

@Service
public class GeminiChatService implements ChatService {

	private final ChatClient chatClient;

	public GeminiChatService(ChatClient chatClient) {
		this.chatClient = chatClient;
	}

	@Override
	public ChatResponse ask(String question) {
		if (question == null || question.isBlank()) {
			throw new IllegalArgumentException("Question must not be blank");
		}

		long startTime = System.nanoTime();

		String answer = chatClient.prompt()
				.user(question)
				.call()
				.content();

		long latencyMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);

		if (answer == null || answer.isBlank()) {
			throw new IllegalStateException("Gemini returned an empty response");
		}

		return new ChatResponse(question, answer, latencyMs);
	}

}
