package io.github.smk412.llmraglab.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {

	@Bean
	ChatClient chatClient(ChatClient.Builder builder) {
		return builder
				.defaultSystem("""
						당신은 사내 업무를 지원하는 AI 어시스턴트입니다.
						사용자의 질문에 명확하고 간결한 한국어로 답변하세요.
						확실하지 않은 내용은 추측하지 말고 모른다고 답변하세요.
						""")
				.build();
	}

}
