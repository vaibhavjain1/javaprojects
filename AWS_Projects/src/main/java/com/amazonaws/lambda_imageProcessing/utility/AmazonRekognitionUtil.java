package com.amazonaws.lambda_imageProcessing.utility;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;

@Service
public class AmazonRekognitionUtil {

	private final AmazonRekognition rekognitionClient;
	
	public AmazonRekognitionUtil() {
		rekognitionClient = AmazonRekognitionClientBuilder.standard().withRegion("ap-south-1").build();
	}
	
	public AmazonRekognitionUtil(@Value("${amazon.rekognition.accessKey}") String accessKey,
			@Value("${amazon.rekognition.secretKey}") String secretKey,
			@Value("${amazon.rekognition.region}") String region) {
		AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
		rekognitionClient =  AmazonRekognitionClientBuilder.standard().withRegion("ap-south-1")
				.withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
	}

	public AmazonRekognition getRekognitionClient() {
		return rekognitionClient;
	}	
	
}
