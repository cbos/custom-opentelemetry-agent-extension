package com.example.custom.api;

/**
 * Custom processor interface
 */
public interface CustomProcessor {

    /**
     * Transform input data
     * @param input the input to transform
     * @return the transformed output
     */
    String transform(String input);
}