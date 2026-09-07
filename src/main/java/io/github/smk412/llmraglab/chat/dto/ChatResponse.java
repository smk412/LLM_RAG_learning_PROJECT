package io.github.smk412.llmraglab.chat.dto;

public record ChatResponse(
		String question,
		String answer,
		long latencyMs) {
}
