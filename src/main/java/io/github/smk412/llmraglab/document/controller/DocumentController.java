package io.github.smk412.llmraglab.document.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import io.github.smk412.llmraglab.document.dto.DocumentUploadResponse;
import io.github.smk412.llmraglab.document.service.DocumentIngestionService;

@Controller
public class DocumentController {

	private static final Logger log = LoggerFactory.getLogger(DocumentController.class);

	private final DocumentIngestionService documentIngestionService;

	public DocumentController(DocumentIngestionService documentIngestionService) {
		this.documentIngestionService = documentIngestionService;
	}

	@GetMapping("/documents")
	public String documentPage() {
		return "document/index";
	}

	@PostMapping("/documents/upload")
	public String upload(
			@RequestParam("file") MultipartFile file,
			RedirectAttributes redirectAttributes,
			Model model) {

		try {
			DocumentUploadResponse response = documentIngestionService.ingest(file);
			redirectAttributes.addFlashAttribute("uploadResponse", response);
			return "redirect:/documents";
		}
		catch (IllegalArgumentException exception) {
			model.addAttribute("uploadError", exception.getMessage());
		}
		catch (RuntimeException exception) {
			log.error("Document ingestion failed", exception);
			model.addAttribute("uploadError", "문서 처리 중 오류가 발생했습니다. 파일과 서버 로그를 확인해 주세요.");
		}

		return "document/index";
	}

}
