package com.amazonaws.s3Samples;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.glue.AWSGlue;
import com.amazonaws.services.glue.AWSGlueClient;
import com.amazonaws.services.glue.model.GetDatabaseRequest;
import com.amazonaws.services.glue.model.GetDatabaseResult;

public class Glue {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BasicAWSCredentials awsCreds = new BasicAWSCredentials("", "");
		AWSGlue a = AWSGlueClient.builder().withRegion("us-east-1").withCredentials(new AWSStaticCredentialsProvider(awsCreds)).build() ;
		GetDatabaseRequest req = new GetDatabaseRequest();
		req.setName("bistudio");
		GetDatabaseResult d = a.getDatabase(req);
		System.out.println(d);
	}

}
