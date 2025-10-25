package com.example.javacore.threads.virtual;

public class VirtualThreadExample { 

	public static void main(String[] args) { 
		try { 
			

			Thread.Builder builder = Thread.ofVirtual().name("Ionut Thread");

			Runnable task = () -> {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
				System.out.println("Running thread"); 
			}; 

			Thread t = builder.start(task); 

			System.out.println("Thread t name: " + t.getName()); 

			// Add a delay to allow the virtual thread to run 
			// Sleep for 1 second 
			Thread.sleep(4000);

			// Wait for the thread to complete 
			t.join(); 
		} catch (InterruptedException e) { 
			e.printStackTrace(); 
		} 
	} 
} 
