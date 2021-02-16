package com.test.simplefileupload.resourceserver.aws.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface AmazonS3ClientService {
	void uploadFileToS3Bucket(MultipartFile multipartFile, Map<String, String> metadata);
}
