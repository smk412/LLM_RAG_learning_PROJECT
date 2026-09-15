package io.github.smk412.llmraglab.rag.controller;

import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.smk412.llmraglab.rag.dto.DocumentSearchResult;
import io.github.smk412.llmraglab.rag.service.DocumentSearchService;

@RestController
@RequestMapping("/api/documents")
public class DocumentSearchApiController {

	private final DocumentSearchService documentSearchService;
	
	public DocumentSearchApiController(DocumentSearchService documentSearchService) {
		this.documentSearchService = documentSearchService;
	}
	
	@GetMapping("/search")
	public List<DocumentSearchResult> search(
			@RequestParam("query") String query,
			@RequestParam(name = "topK", defaultValue = "5") int topK
	) {
		List<Document> documents = documentSearchService.search(query, topK);
		
		return documents.stream()
				.map(DocumentSearchResult::from)
				.toList();
	}
}
