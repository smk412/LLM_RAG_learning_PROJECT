package io.github.smk412.llmraglab.rag.dto;

import java.util.Map;
import java.util.Objects;

import org.springframework.ai.document.Document;

public record DocumentSearchResult(
		String content,
		String fileName,
		String contentType,
		int chunkIndex,
		int totalChunks,
		Double similarityScore
) {
	
	public static DocumentSearchResult from(Document document) {
		Map<String, Object> metadata = document.getMetadata();
		
		return new DocumentSearchResult(
				Objects.requireNonNullElse(document.getText(), ""),
				getString(metadata, "file_name", "알 수 없는 파일"),
				getString(metadata, "content_type", "알 수 없음"),
				getInt(metadata, "chunk_index", 0),
				getInt(metadata, "total_chunks", 0),
				document.getScore()
				);
	}
	
	private static String getString(
			Map<String, Object> metadata,
			String key,
			String defaultValue
	) {
		Object value = metadata.get(key);
		
		if (value == null) {
			return defaultValue;
		}
		return value.toString();
	}
	
	private static int getInt(
			Map<String, Object> metadata,
			String key,
			int defaultValue
	) {
		Object value = metadata.get(key);
		
		if (value instanceof Number number) {
			return number.intValue();
		}
		if (value instanceof String text) {
			try {
				return Integer.parseInt(text);
			}
			catch (NumberFormatException exception) {
				return defaultValue;
			}
		}
		return defaultValue;
	}
	
	
}
