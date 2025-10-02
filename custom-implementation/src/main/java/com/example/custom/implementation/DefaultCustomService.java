package com.example.custom.implementation;

import com.example.custom.api.CustomService;

/**
 * Default implementation of CustomService - plain Java, no OpenTelemetry
 */
public class DefaultCustomService implements CustomService {
    
    @Override
    public String process(String data) {
        Util.randomSleep(50, 150);
        return "Processed: " + data;
    }
    
    @Override
    public void execute(String operation) {
        Util.randomSleep(100, 200);
    }
}