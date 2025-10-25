package com.example.javacore.threads.virtual;// Java Program to implement
import java.io.*;
import java.net.*;

// Multithreaded Client Server
public class EchoServer {
	
	//Main method 
	public static void main(String[] args) throws IOException { 

		int portNumber = 1234;
		try (ServerSocket serverSocket = new ServerSocket(portNumber)) { 
			System.out.println("EchoServer listening on port " + portNumber); 

			while (true) { 
				Socket clientSocket = serverSocket.accept(); 
				// Accept incoming connections 
				// Start a service thread 
				Thread.ofVirtual().start(() -> { 
					try ( 
						PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true); 
						BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); 
					) { 
						String inputLine; 
						while ((inputLine = in.readLine()) != null) { 
							System.out.println("Received: " + inputLine); 
							out.println("Echo: " + inputLine); 
							out.flush(); // Explicitly flush the output 
						} 
					} catch (IOException e) { 
						e.printStackTrace(); 
					} finally { 
						try { 
							clientSocket.close(); 
						} catch (IOException e) { 
							e.printStackTrace(); 
						} 
					} 
				}); 
			} 
		} catch (IOException e) {
			System.out.println("Exception caught when trying to listen on port " + portNumber + " or listening for a connection");
			System.out.println(e.getMessage());
		}
	} 
} 
