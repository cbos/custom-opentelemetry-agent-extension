package com.example.custom.implementation;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Custom Implementation Application Started");

        // Number of threads to create
        int numberOfThreads = 5; // You can change this value to create desired number of threads

        int iterations = 100;
        
        // Create a fixed thread pool
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        
        // Submit tasks to thread pool
        for (int i = 0; i < numberOfThreads; i++) {
            final int threadNumber = i + 1;
            executorService.submit(() -> {
                try {
                    System.out.println("Thread " + threadNumber + " starting execution");

                    // Create instances of the implementations
                    DefaultCustomService service = new DefaultCustomService();
                    DefaultCustomProcessor processor = new DefaultCustomProcessor();

                    for (int j = 0; j < iterations; j++) {
// Demonstrate the service
                        String data = "sample data from thread " + threadNumber;
                        service.process(data);
                        service.execute("demo operation from thread " + threadNumber);

                        Util.randomSleep(50, 150);

                        // Demonstrate the processor
                        String input = "hello world from thread " + threadNumber;
                        processor.transform(input);
                    }


                } catch (Throwable e) {
                    System.err.println("Error in thread " + threadNumber + ": " + e.getMessage());
                    e.printStackTrace();
                }
            });
        }

        // Initiate an orderly shutdown of the executor service
        executorService.shutdown();

        if (executorService.awaitTermination(120, TimeUnit.SECONDS)) {
            System.out.println("\nAll threads completed successfully!");
        } else {
            System.out.println("\nTimeout reached before all threads could complete!");
            executorService.shutdownNow(); // Force shutdown
        }

        System.out.println("Application Finished - All threads completed");
        Util.randomSleep(5000, 5000);
    }
}