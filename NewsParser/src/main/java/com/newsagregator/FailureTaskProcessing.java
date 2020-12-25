package com.newsagregator;

import java.util.concurrent.CountDownLatch;

public class FailureTaskProcessing implements Runnable {

    static CountDownLatch countFailure = new CountDownLatch(3);

    @Override
    public void run() {
        while (countFailure.getCount() != 0) {
            try {
                countFailure.await();
                System.err.println("HOP");
                reset();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static synchronized void check() {
        countFailure.countDown();
    }

    public synchronized void reset(){
        countFailure = new CountDownLatch(3);
    }

}
