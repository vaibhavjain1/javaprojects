package com.amazonaws.lambda_imageProcessing.model;

public class ImageAnalysisPayload {
	
	Long userId;
	Long gameId;
	Long resourceId;
	String resourceUrl;
	String profileUrl;

	public ImageAnalysisPayload() {
		super();
	}
	
	public ImageAnalysisPayload(Long userId, Long gameId, Long resourceId) {
		super();
		this.userId = userId;
		this.gameId = gameId;
		this.resourceId = resourceId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getGameId() {
		return gameId;
	}

	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}

	public Long getResourceId() {
		return resourceId;
	}

	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}

	public String getResourceUrl() {
		return resourceUrl;
	}

	public void setResourceUrl(String resourceUrl) {
		this.resourceUrl = resourceUrl;
	}

	public String getProfileUrl() {
		return profileUrl;
	}

	public void setProfileUrl(String profileUrl) {
		this.profileUrl = profileUrl;
	}

}
