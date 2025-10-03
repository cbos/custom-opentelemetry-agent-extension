package com.example.custom.implementation;

import com.example.custom.api.CustomProcessor;

/**
 * Default implementation of CustomProcessor - plain Java, no OpenTelemetry
 */
public class DefaultCustomProcessor implements CustomProcessor {
    
    @Override
    public String transform(String input) {
        Util.randomSleep(25, 500);

        //random error once in 10 times
        if (Math.random() < 0.1) {
            throw new RuntimeException("Random error");
        }

        return input.toUpperCase();
    }
}