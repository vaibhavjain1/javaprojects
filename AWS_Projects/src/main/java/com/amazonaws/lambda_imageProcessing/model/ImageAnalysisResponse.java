package com.amazonaws.lambda_imageProcessing.model;

import com.amazonaws.services.rekognition.model.CompareFacesResult;
import com.amazonaws.services.rekognition.model.DetectFacesResult;
import com.amazonaws.services.rekognition.model.DetectLabelsResult;

public class ImageAnalysisResponse {
	
	ImageAnalysisPayload payload;
	DetectFacesResult detectFaces;
	CompareFacesResult compareFaces;
	DetectLabelsResult detectLabels;
	int statusCode;
	String statusText;
	
	public ImageAnalysisResponse() {
		super();
	}

	public ImageAnalysisResponse(ImageAnalysisPayload payload, DetectFacesResult detectFaces, CompareFacesResult compareFaces,
			DetectLabelsResult detectLabels) {
		this.payload = payload;
		this.detectFaces = detectFaces;
		this.compareFaces = compareFaces;
		this.detectLabels = detectLabels;
	}
	

	public ImageAnalysisPayload getPayload() {
		return payload;
	}

	public void setPayload(ImageAnalysisPayload payload) {
		this.payload = payload;
	}

	public DetectFacesResult getDetectFaces() {
		return detectFaces;
	}

	public void setDetectFaces(DetectFacesResult detectFaces) {
		this.detectFaces = detectFaces;
	}

	public CompareFacesResult getCompareFaces() {
		return compareFaces;
	}

	public void setCompareFaces(CompareFacesResult compareFaces) {
		this.compareFaces = compareFaces;
	}

	public DetectLabelsResult getDetectLabels() {
		return detectLabels;
	}

	public void setDetectLabels(DetectLabelsResult detectLabels) {
		this.detectLabels = detectLabels;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusText() {
		return statusText;
	}

	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}
}
