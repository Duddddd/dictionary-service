package com.dictionaryservice.listener;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

import org.omg.CosNaming.NamingContextPackage.CannotProceedHolder;

import com.dictionaryservice.handlers.CustomProtocolHandler;
import com.dictionaryservice.handlers.ICallHandler;

public class TCPListener {
	static DataOutputStream outToClient;
	static final int DEFAULT_PORT = 1987; 
	
	public static void main(String argv[]) throws Exception {
		System.out.println("opening TCP socket. ");
		int port = DEFAULT_PORT;
		if (argv.length>0) {
			port= Integer.parseInt(argv[0]);
		}
		ServerSocket socket = new ServerSocket(port);
		ICallHandler handller = new CustomProtocolHandler();

		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				try {
					while (true) {
						String request = getCall(socket);
						String responseToClient = handller.handleCall(request);
						responseToClient(responseToClient);
					}
				} 
				catch(SocketException e) {
					System.out.println("socket has been shutdown.");
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		
		new Thread(runnable).start();
		Thread.sleep(1000);
		System.out.println("server is up. To stop it press 'e'");
		String key = null;
		Scanner scanner = new Scanner(System.in);
		while (!"e".equals(key)) {
			key = scanner.next();			
		}
		
		scanner.close();
		socket.close();
		System.out.println("server is down.");
	}
	
	public static String getCall(ServerSocket socket) throws IOException {
		System.out.println("waiting for new call");
		Socket connectionSocket = socket.accept();
		BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
		outToClient = new DataOutputStream(connectionSocket.getOutputStream());
		String clientRequest = inFromClient.readLine();
		System.out.println("Received: " + clientRequest);
		
		return clientRequest;
	}
	
	public static void responseToClient(String responseToClient) throws IOException {
		responseToClient = responseToClient + "\n";
		outToClient.writeBytes(responseToClient);
	}

}
