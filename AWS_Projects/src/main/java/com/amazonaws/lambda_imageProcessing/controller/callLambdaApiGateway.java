package com.amazonaws.lambda_imageProcessing.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.amazonaws.services.rekognition.model.DetectFacesResult;
import com.eduthrill.model.ImageAnalysisPayload;
import com.eduthrill.model.ImageAnalysisResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson; 

public class callLambdaApiGateway {

	/** The Constant logger. */
	private static final Log logger = LogFactory.getLog(callLambdaApiGateway.class);
	
	private static ObjectMapper objectMapper = new ObjectMapper();

	private static RestTemplate restTemplate = new RestTemplate();
	
	public static void main(String args[]) throws JsonProcessingException{
		//String apiUrl = "https://rv2j4t2a3m.execute-api.ap-south-1.amazonaws.com/default/Eduthrill"; //without auth
		String apiUrl = "https://ldj8jx34zb.execute-api.ap-south-1.amazonaws.com/default/Eduthrill";
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("x-api-key", "snQO3DeEYz47RNNZqNpq65CEN3tz5iPS6v2A8HB3");
		
		ImageAnalysisPayload req = new ImageAnalysisPayload();
		req.setResourceUrl("http://cdn.eduthrill.com/userGameImage/d6dff032-60e6-4bba-ab88-6e415f946866-1570903466173.jpeg");

		String requestJson = "";
		try {
			requestJson = objectMapper.writeValueAsString(req);
			logger.info("Branch Request Payload " + requestJson );
		} catch (JsonProcessingException e) {
			logger.info(e);
		}
		
		HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);
		String resp = restTemplate.postForObject(apiUrl, entity, String.class);
		Gson gson = new Gson(); 
		ImageAnalysisResponse response = gson.fromJson(resp,ImageAnalysisResponse.class);
		System.out.println(response);
	}

}
