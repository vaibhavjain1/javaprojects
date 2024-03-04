package com.amazonaws.s3Samples;

import java.sql.ResultSet;
import java.util.HashMap;

public class TestJsonObjectv {

	public static void main(String[] args) {
		ResultSet rs = null;
		HashMap<String, String> test = new HashMap<String, String>();
	test.put("a", "b");
	test.replace("a", "c");
	System.out.println(test);
	
	}
}
