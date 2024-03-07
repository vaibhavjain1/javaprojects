package com.amazonaws.lambda_imageProcessing.handler;

import com.amazonaws.lambda_imageProcessing.model.ImageAnalysisPayload;
import com.amazonaws.lambda_imageProcessing.model.ImageAnalysisResponse;
import com.amazonaws.lambda_imageProcessing.utility.AmazonRekognitionUtil;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.rekognition.model.*;
import com.amazonaws.util.IOUtils;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;

@Component
public class ImageAnalysisLambdaHandler implements RequestHandler<ImageAnalysisPayload, ImageAnalysisResponse>{

	//@Autowired
	AmazonRekognitionUtil rekognitionClient = new AmazonRekognitionUtil();

	@Override
	public ImageAnalysisResponse handleRequest(ImageAnalysisPayload payload, Context context) {
		context.getLogger().log("ResourceUrl: " + payload.getResourceUrl());
		
		ImageAnalysisResponse response = new ImageAnalysisResponse();
		response.setPayload(payload);
		
		if (payload.getResourceUrl() == null || payload.getResourceUrl().isEmpty()) {
			response.setStatusText("Resource URL is empty");
			response.setStatusCode(HttpStatus.SC_NO_CONTENT);;
			return response;
		}

		try {
			processMultipleFacesRequest(payload, response);
			processObjectLabelDetectionRequest(payload, response);
			processCompareFacesDetectionRequest(payload, response);
		} catch (Exception e) {
			response.setStatusText(e.getMessage());
			response.setStatusCode(HttpStatus.SC_NO_CONTENT);;
			return response;
		}

		response.setStatusCode(HttpStatus.SC_OK);
		return response;
	}

	public void processMultipleFacesRequest(ImageAnalysisPayload payload, ImageAnalysisResponse response) throws Exception {
		DetectFacesRequest request = createDetectFacesRequest(payload.getResourceUrl());
		if (request == null) {
			return;
		}
		try {
			DetectFacesResult result = callDetectFacesApi(request);
			if (result != null) {
				response.setDetectFaces(result);
			}
		} catch (Exception e) {
			throw new Exception("Error in Face detection. " + e.getMessage());
		}
	}

	public void processObjectLabelDetectionRequest(ImageAnalysisPayload payload, ImageAnalysisResponse response) throws Exception {
		DetectLabelsRequest request = createDetectLabelsRequest(payload.getResourceUrl());
		if (request == null) {
			return;
		}
		try {
			DetectLabelsResult result = callDetectLabelsApi(request);
			if (result != null) {
				response.setDetectLabels(result);
			}
		} catch (Exception e) {
			throw new Exception("Error in Label detection. " + e.getMessage());
		}
	}

	public void processCompareFacesDetectionRequest(ImageAnalysisPayload payload, ImageAnalysisResponse response) throws Exception {
		if(payload.getProfileUrl()==null || payload.getProfileUrl().isEmpty())
			return;
	
		CompareFacesRequest request = createCompareFacesRequest(payload.getResourceUrl(), payload.getProfileUrl());
		if (request == null) {
			return;
		}
		try {
			CompareFacesResult result = callCompareFacesApi(request);
			if (result != null) {
				response.setCompareFaces(result);
			}
		} catch (Exception e) {
			throw new Exception("Error in Compare Face. " + e.getMessage());
		}
	}

	public DetectFacesResult callDetectFacesApi(DetectFacesRequest request) {
		return rekognitionClient.getRekognitionClient().detectFaces(request);
	}

	public DetectLabelsResult callDetectLabelsApi(DetectLabelsRequest request) {
		return rekognitionClient.getRekognitionClient().detectLabels(request);
	}

	public CompareFacesResult callCompareFacesApi(CompareFacesRequest request){
		return rekognitionClient.getRekognitionClient().compareFaces(request);
	}
	
	private DetectFacesRequest createDetectFacesRequest(String url) {
		try {
			ByteBuffer imageBytes = convertImageToBytes(url);
			return new DetectFacesRequest().withImage(new Image().withBytes(imageBytes)).withAttributes(Attribute.ALL);
		} catch (Exception e) {
			return null;
		}
	}

	private DetectLabelsRequest createDetectLabelsRequest(String url) {
		try {
			ByteBuffer imageBytes = convertImageToBytes(url);
			return new DetectLabelsRequest().withImage(new Image().withBytes(imageBytes));
		} catch (Exception e) {
			return null;
		}
	}

	private CompareFacesRequest createCompareFacesRequest(String url, String profileUrl) {
		try {
			ByteBuffer imageBytesImage = convertImageToBytes(url);
			ByteBuffer imageBytesProfile = convertImageToBytes(profileUrl);
			return new CompareFacesRequest().withSourceImage(new Image().withBytes(imageBytesImage))
					.withTargetImage(new Image().withBytes(imageBytesProfile));
		} catch (Exception e) {
			return null;
		}
	}

	ByteBuffer convertImageToBytes(String url) {
		ByteBuffer imageBytes = null;
		try (InputStream inputStream = new URL(url).openStream()) {
			try {
				return ByteBuffer.wrap(IOUtils.toByteArray(inputStream));
			} catch (IOException e) {
			}
		} catch (FileNotFoundException e1) {
		} catch (MalformedURLException e2) {
		} catch (IOException e2) {
			;
		}
		return imageBytes;
	}

}
