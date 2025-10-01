package com.example.custom.implementation;

import com.example.custom.api.CustomProcessor;

/**
 * Default implementation of CustomProcessor - plain Java, no OpenTelemetry
 */
public class DefaultCustomProcessor implements CustomProcessor {
    
    @Override
    public String transform(String input) {
        System.out.println("Transforming input: " + input);

        return input.toUpperCase();
    }
}