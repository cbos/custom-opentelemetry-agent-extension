package com.example.custom.implementation;

import com.example.custom.api.CustomService;

/**
 * Default implementation of CustomService - plain Java, no OpenTelemetry
 */
public class DefaultCustomService implements CustomService {
    
    @Override
    public String process(String data) {
        System.out.println("Processing data: " + data);
        return "Processed: " + data;
    }
    
    @Override
    public void execute(String operation) {
        System.out.println("Executing operation: " + operation);
    }
}