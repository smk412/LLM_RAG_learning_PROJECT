package io.github.smk412.llmraglab.rag.controller;

import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.github.smk412.llmraglab.rag.dto.DocumentSearchResult;
import io.github.smk412.llmraglab.rag.service.DocumentSearchService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class DocumentSearchController {
	
	private final DocumentSearchService documentSearchService;
		
	private static final Logger log = LoggerFactory.getLogger(DocumentSearchController.class);
	
	public DocumentSearchController(
			DocumentSearchService documentSearchService
	) {
		this.documentSearchService = documentSearchService;
	}
	
	@GetMapping("/search")
	public String search(
			@RequestParam(
					name = "query",
					required = false
			)
			String query,
			
			@RequestParam(
					name = "topK",
					defaultValue = "5"
			)
			int topK,
			
			Model model
	) {
		model.addAttribute("query", query);
		model.addAttribute("topK", topK);
		
		if (!StringUtils.hasText(query)) {
			return "rag/search";
			
		}
		
		try {
			List<Document> documents = documentSearchService.search(query, topK);
			List<DocumentSearchResult> results = documents.stream()
					.map(DocumentSearchResult::from)
					.toList();
			
			model.addAttribute("searchResults", results);
			model.addAttribute("resultCount", results.size());
		}
		catch (IllegalArgumentException exception) {
			model.addAttribute("searchError", exception.getMessage());
		}
		catch (RuntimeException exception) {
			log.error("Vector document search failed", exception);
			
			model.addAttribute("searchError", "문서를 검색하는 중 오류가 발생했습니다.");
		}
		
		return "rag/search";
	}
	
}
