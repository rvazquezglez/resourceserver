package com.test.simplefileupload.resourceserver.filemanager.controller;

import com.test.simplefileupload.resourceserver.aws.service.AmazonS3ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/files")
public class FileManagerController {
	private final AmazonS3ClientService amazonS3ClientService;

	public FileManagerController(AmazonS3ClientService amazonS3ClientService) {
		this.amazonS3ClientService = amazonS3ClientService;
	}

	@PostMapping
	public ResponseEntity<Map<String, String>> uploadFile(@ModelAttribute RequestWrapper requestWrapper) {

		this.amazonS3ClientService.uploadFileToS3Bucket(
			requestWrapper.file,
			requestWrapper.metadata.stream()
				.collect(Collectors.toMap(MetadataEntry::getKey,MetadataEntry::getValue))
		);

		return ResponseEntity.ok(Map.of("message", requestWrapper.file.getOriginalFilename() + "saved successfully"));
	}

	static class RequestWrapper {
		private MultipartFile file;
		private List<MetadataEntry> metadata;

		public MultipartFile getFile() {
			return file;
		}

		public void setFile(MultipartFile file) {
			this.file = file;
		}

		public List<MetadataEntry> getMetadata() {
			return metadata;
		}

		public void setMetadata(List<MetadataEntry> metadata) {
			this.metadata = metadata;
		}
	}

	static class MetadataEntry {
		private String key;
		private String value;

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}
}
