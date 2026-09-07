package io.github.smk412.llmraglab.chat.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import io.github.smk412.llmraglab.chat.dto.ChatRequest;
import io.github.smk412.llmraglab.chat.dto.ChatResponse;
import io.github.smk412.llmraglab.chat.service.ChatService;
import jakarta.validation.Valid;

@Controller
public class ChatController {

	private static final Logger log = LoggerFactory.getLogger(ChatController.class);

	private final ChatService chatService;

	public ChatController(ChatService chatService) {
		this.chatService = chatService;
	}

	@GetMapping({ "/", "/chat" })
	public String chatForm(Model model) {
		model.addAttribute("chatRequest", new ChatRequest());
		return "chat/index";
	}

	@PostMapping("/chat")
	public String ask(
			@Valid @ModelAttribute("chatRequest") ChatRequest request,
			BindingResult bindingResult,
			Model model) {

		if (bindingResult.hasErrors()) {
			return "chat/index";
		}

		try {
			ChatResponse response = chatService.ask(request.getQuestion());
			model.addAttribute("chatResponse", response);
		}
		catch (RuntimeException exception) {
			log.error("Gemini API request failed", exception);
			model.addAttribute("chatError", "AI 답변을 생성하지 못했습니다. 잠시 후 다시 시도해 주세요.");
		}

		return "chat/index";
	}

}
