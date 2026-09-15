package io.github.smk412.llmraglab.rag.service;

import java.util.List;

import org.springframework.ai.document.Document;

public interface DocumentSearchService {

	/* 사용자의 질문과 의미적으로 유사한 문서 chunk를 검색한다.
	 * 
	 * @param query 사용자가 입력한 검색 질문
	 * @param topK 반환할 최대 chunk 개수
	 * @return 유사도 순으로 검색된 문서 chnuk 목록
	 */
	
	List<Document> search(String query, int topK);
}
