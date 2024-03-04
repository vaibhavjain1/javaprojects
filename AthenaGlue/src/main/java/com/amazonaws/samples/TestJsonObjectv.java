package com.amazonaws.samples;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TestJsonObjectv {

	public static void main(String[] args) {
		ResultSet rs = null;
		HashMap<String, String> test = new HashMap<String, String>();
	test.put("a", "b");
	test.replace("a", "c");
	System.out.println(test);
	
	}
}
