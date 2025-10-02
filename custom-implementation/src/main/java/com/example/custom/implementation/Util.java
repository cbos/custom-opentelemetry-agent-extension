package com.example.custom.implementation;

public class Util {

    public static void randomSleep(long min, long max) {
        try {
            long sleepTime = (long) (Math.random() * (max - min)) + min;
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
