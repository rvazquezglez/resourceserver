package com.test.simplefileupload.resourceserver.aws.config;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AmazonS3Config {

	private final AmazonS3Properties amazonS3Properties;

	public AmazonS3Config(AmazonS3Properties amazonS3Properties) {
		this.amazonS3Properties = amazonS3Properties;
	}

	@Bean(name = "awsRegion")
	public Region awsRegion() {
		return Region.getRegion(Regions.fromName(amazonS3Properties.getRegion()));
	}

	@Bean(name = "awsCredentialsProvider")
	public AWSCredentialsProvider awsCredentialsProvider() {
		BasicAWSCredentials awsCredentials = new BasicAWSCredentials(
			amazonS3Properties.getAccessKeyId(),
			amazonS3Properties.getAccessSecret()
		);
		return new AWSStaticCredentialsProvider(awsCredentials);
	}
}
