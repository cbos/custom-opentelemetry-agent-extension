package com.example.custom.api;

/**
 * Custom service interface
 */
public interface CustomService {
    
    /**
     * Process some data
     * @param data the data to process
     * @return the processed result
     */
    String process(String data);
    
    /**
     * Execute some operation
     * @param operation the operation name
     */
    void execute(String operation);
}