package io.github.smk412.llmraglab.rag.service;

import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class VectorDocumentSearchService implements DocumentSearchService {
		
		private final VectorStore vectorStore;
		private final double similarityThreshold;
		
		public VectorDocumentSearchService(
				VectorStore vectorStore,
				@Value("${app.rag.similarity-threshold:0.6}")
				double similarityThreshold) {
			
			this.vectorStore = vectorStore;
			this.similarityThreshold = similarityThreshold;
		}
		
		@Override
		public List<Document> search(String query, int topK) {
			validate(query, topK);
			
			SearchRequest searchRequest = SearchRequest.builder()
					.query(query.trim())
					.topK(topK)
					.similarityThreshold(similarityThreshold)
					.build();
			
			return vectorStore.similaritySearch(searchRequest);
		}
		
		private void validate(String query, int topK) {
			if (!StringUtils.hasText(query)) {
				throw new IllegalArgumentException("검색 질문을 입력해 주세요.");
			}
			if (topK <= 0) {
				throw new IllegalArgumentException("검색할 문서 개수는 1개 이상이어야 합니다.");
			}
		}
}
