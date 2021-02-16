package com.test.simplefileupload.resourceserver.aws.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.test.simplefileupload.resourceserver.aws.config.AmazonS3Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class AmazonS3ClientServiceImpl implements AmazonS3ClientService {

	private AmazonS3 amazonS3;
	private AmazonS3Properties amazonS3Properties;
	private static final Logger LOGGER = LoggerFactory.getLogger(AmazonS3ClientServiceImpl.class);

	public AmazonS3ClientServiceImpl(AmazonS3 amazonS3, AmazonS3Properties amazonS3Properties) {
		this.amazonS3 = amazonS3;
		this.amazonS3Properties = amazonS3Properties;
	}

	@Autowired
	public AmazonS3ClientServiceImpl(
		Region region,
		AWSCredentialsProvider awsCredentialsProvider,
		AmazonS3Properties amazonS3Properties
	) {
		this.amazonS3 = AmazonS3ClientBuilder.standard()
			.withCredentials(awsCredentialsProvider)
			.withRegion(region.getName())
			.build();
		this.amazonS3Properties = amazonS3Properties;
	}

	@Override
	@Async
	public void uploadFileToS3Bucket(MultipartFile multipartFile, Map<String, String> metadata) {
		String fileName = multipartFile.getOriginalFilename();
		try {
			ObjectMetadata objectMetadata = new ObjectMetadata();
			metadata.forEach(objectMetadata::addUserMetadata);
			PutObjectRequest putObjectRequest = new PutObjectRequest(
				amazonS3Properties.getS3Bucket(),
				fileName,
				multipartFile.getInputStream(),
				objectMetadata
			);
			putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead);
			this.amazonS3.putObject(putObjectRequest);
		} catch (IOException | AmazonServiceException ex) {
			LOGGER.error("Error uploading file with name: {} message:{}", fileName, ex.getMessage());
		}
	}
}
