package com.dictionaryservice.listener;



import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

import org.testng.Assert;

public class TCPClientTest {

	public static void main(String argv[]) throws Exception {

		List<String> sentValues = Arrays.asList("hello", "hello world");
		// test set
		set("message1", sentValues);
		set("message2", Arrays.asList("hello", "hello world", "sunshine"));

		// test get
		List<String> fromServer = get("message1");

		// assert equels
		Assert.assertEquals(fromServer, sentValues);
		
		//test get keys
		List<String> keys = getKeys("message");
		Assert.assertTrue(keys.contains("message1"));
		Assert.assertTrue(keys.contains("message2"));		
		
		
		List<String> keys2 = getKeys("message2");
		Assert.assertFalse(keys2.contains("message1"));
		Assert.assertTrue(keys.contains("message2"));
		
		//test add right
		addToRight("message2", "added one");
		List<String> fromServerNew = get("message2");
		Assert.assertEquals(fromServerNew, Arrays.asList("hello", "hello world", "sunshine", "added one"));
		
		//test add left
		addToLeft("message2", "added left");
		List<String> fromServerNew2 = get("message2");
		Assert.assertEquals(fromServerNew2, Arrays.asList("added left", "hello", "hello world", "sunshine", "added one"));
				
	}
	
	static void addToLeft(String key, String value) throws Exception {
		execut("leftAdd" + "," + key + "," + value);
	}
	static void addToRight(String key, String value) throws Exception {
		execut("rightAdd" + "," + key + "," + value);
	}
	private static List<String> getKeys(String pattern) throws Exception {
		String message = "getAllKeys," + pattern;
		return getList(message);
	}

	private static List<String> get(String string) throws Exception {
		String message = "get," + string;
		return getList(message);
	}
	
	private static List<String> getList(String message) throws Exception{
		Socket clientSocket = new Socket("localhost", 1987);
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		outToServer.writeBytes(message + "\n");
		String serverResponse = inFromServer.readLine();
		System.out.println("FROM SERVER: " + serverResponse);
		clientSocket.close();

		return Arrays.asList(serverResponse.split(","));
		
	}
	
	private static void execut(String message) throws Exception {
		Socket clientSocket = new Socket("localhost", 1987);
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		outToServer.writeBytes(message + "\n");
		String serverResponse = inFromServer.readLine();
		System.out.println("FROM SERVER: " + serverResponse);
		clientSocket.close();
	}
		
	private static void set(String key, List<String> values) throws Exception {
		StringBuilder message = new StringBuilder("set" + "," + key);

		for (String string : values) {
			message.append(",").append(string);
		}
		execut(message.toString());
	}

}
