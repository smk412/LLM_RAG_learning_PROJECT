package io.github.smk412.llmraglab.document.service;

import org.springframework.web.multipart.MultipartFile;

import io.github.smk412.llmraglab.document.dto.DocumentUploadResponse;

public interface DocumentIngestionService {

	DocumentUploadResponse ingest(MultipartFile file);

}
