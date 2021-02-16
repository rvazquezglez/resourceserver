package com.test.simplefileupload.resourceserver.filemanager.controller;

import com.adobe.testing.s3mock.junit5.S3MockExtension;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.google.common.io.CharStreams;
import com.test.simplefileupload.resourceserver.ResourceServerApplication;
import com.test.simplefileupload.resourceserver.aws.config.AmazonS3Properties;
import com.test.simplefileupload.resourceserver.aws.service.AmazonS3ClientServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "aws.s3Bucket=" + FileManagerControllerTest.BUCKET_NAME)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
	ResourceServerApplication.class,
	FileManagerControllerTest.MockRestTemplateConfiguration.class
})
class FileManagerControllerTest {

	@RegisterExtension
	static final S3MockExtension S3_MOCK = S3MockExtension.builder().silent()
		.withSecureConnection(false).build();

	static AmazonS3 s3Client;

	public static final String BUCKET_NAME = "mock-bucket";

	@BeforeAll
	static void beforeAll() {
		s3Client = S3_MOCK.createS3Client();
	}

	@Autowired
	private WebApplicationContext webApplicationContext;


	@BeforeEach
	void setUp() {
		s3Client.createBucket(BUCKET_NAME);
	}

	@Test
	void whenFileUploaded_thenSavedOnS3() throws Exception {
		String fileName = "textfile.txt";
		String fileContent = "Lorem ipsum";
		MockMultipartFile file = new MockMultipartFile(
			"file",
			fileName,
			MediaType.TEXT_PLAIN_VALUE,
			fileContent.getBytes()
		);

		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		mockMvc.perform(
			multipart("/files")
				.file(file)
				.param("metadata[0].key", "key1")
				.param("metadata[1].key", "key2")
				.param("metadata[0].value", "value1")
				.param("metadata[1].value", "value2")
		).andExpect(status().isOk());


		final S3Object s3Object = s3Client.getObject(BUCKET_NAME, fileName);

		try (Reader contentReader = new InputStreamReader(s3Object.getObjectContent())) {
			final String downloadedFileContent = CharStreams.toString(contentReader);
			ObjectMetadata objectMetadata = s3Object.getObjectMetadata();
			Map<String, String> userMetadata = objectMetadata.getUserMetadata();

			assertEquals("Up and downloaded content are the same", fileContent, downloadedFileContent);
			assertEquals("Downloaded object contains metadata for key: key1", userMetadata.get("key1"), "value1");
			assertEquals("Downloaded object contains metadata for key: key2", userMetadata.get("key2"), "value2");
		}
	}

	@Configuration
	static class MockRestTemplateConfiguration {
		@Bean
		@Primary
		public AmazonS3ClientServiceImpl s3ClientService(AmazonS3Properties amazonS3Properties) {
			return new AmazonS3ClientServiceImpl(
				s3Client,
				amazonS3Properties
			);
		}
	}
}