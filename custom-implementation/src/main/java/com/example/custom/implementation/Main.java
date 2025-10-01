package com.example.custom.implementation;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Custom Implementation Application Started");

        // Create instances of the implementations
        DefaultCustomService service = new DefaultCustomService();
        DefaultCustomProcessor processor = new DefaultCustomProcessor();

        // Demonstrate the service
        String data = "sample data";
        String result = service.process(data);
        System.out.println("Service result: " + result);

        service.execute("demo operation");

        // Demonstrate the processor
        String input = "hello world";
        String transformed = processor.transform(input);
        System.out.println("Processor transformed: " + transformed);

        System.out.println("Application Finished");

        System.out.println("Sleep to push metrics");
        Thread.sleep(20_000);
    }
}