package io.github.smk412.llmraglab.document.service;

import java.time.Instant;
import java.util.List;
import java.util.Locale;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import io.github.smk412.llmraglab.document.dto.DocumentUploadResponse;

@Service
public class VectorDocumentIngestionService implements DocumentIngestionService {

	private static final String PDF_EXTENSION = "pdf";
	private static final String TXT_EXTENSION = "txt";

	private final VectorStore vectorStore;
	private final TokenTextSplitter textSplitter;

	public VectorDocumentIngestionService(
			VectorStore vectorStore,
			@Value("${app.rag.chunk-size:300}") int chunkSize) {
		this.vectorStore = vectorStore;
		this.textSplitter = TokenTextSplitter.builder()
				.withChunkSize(chunkSize)
				.build();
	}

	@Override
	public DocumentUploadResponse ingest(MultipartFile file) {
		validate(file);

		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		String extension = getExtension(fileName);
		List<Document> extractedDocuments = extract(file, extension);
		String uploadedAt = Instant.now().toString();

		List<Document> documentsWithMetadata = extractedDocuments.stream()
				.map(document -> document.mutate()
						.metadata("file_name", fileName)
						.metadata("content_type", extension)
						.metadata("uploaded_at", uploadedAt)
						.build())
				.toList();

		List<Document> chunks = textSplitter.apply(documentsWithMetadata);
		if (chunks.isEmpty()) {
			throw new IllegalArgumentException("문서에서 저장할 텍스트를 찾지 못했습니다.");
		}

		vectorStore.add(chunks);
		return new DocumentUploadResponse(fileName, chunks.size());
	}

	private List<Document> extract(MultipartFile file, String extension) {
		return switch (extension) {
			case PDF_EXTENSION -> new PagePdfDocumentReader(
					file.getResource(),
					PdfDocumentReaderConfig.builder()
							.withPagesPerDocument(1)
							.build())
					.get();
			case TXT_EXTENSION -> new TextReader(file.getResource()).get();
			default -> throw new IllegalArgumentException("PDF 또는 TXT 파일만 업로드할 수 있습니다.");
		};
	}

	private void validate(MultipartFile file) {
		if (file == null || file.isEmpty()) {
			throw new IllegalArgumentException("업로드할 파일을 선택해 주세요.");
		}

		String fileName = file.getOriginalFilename();
		if (!StringUtils.hasText(fileName)) {
			throw new IllegalArgumentException("파일 이름을 확인할 수 없습니다.");
		}

		String extension = getExtension(fileName);
		if (!PDF_EXTENSION.equals(extension) && !TXT_EXTENSION.equals(extension)) {
			throw new IllegalArgumentException("PDF 또는 TXT 파일만 업로드할 수 있습니다.");
		}
	}

	private String getExtension(String fileName) {
		String extension = StringUtils.getFilenameExtension(fileName);
		return extension == null ? "" : extension.toLowerCase(Locale.ROOT);
	}

}
