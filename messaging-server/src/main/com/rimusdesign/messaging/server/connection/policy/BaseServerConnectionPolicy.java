package com.rimusdesign.messaging.server.connection.policy;


import java.util.concurrent.LinkedBlockingQueue;


/**
 * @author Rimas Krivickas.
 */
public abstract class BaseServerConnectionPolicy implements ConnectionPolicy {


    protected boolean isTerminated;
    protected LinkedBlockingQueue controlQueue;


    public BaseServerConnectionPolicy (LinkedBlockingQueue controlQueue) {

        this.controlQueue = controlQueue;
    }


    public void init () {
        // Set initial value
        isTerminated = false;
    }


    /**
     * A convenience method that iterates through any specified list and checks if provided value is present in the list
     *
     * @param list
     * @param value
     * @return 'true' if list contains the value
     */
    protected boolean listContainsValue (int[] list, int value) {

        for (int i = 0; i < list.length; i++) {
            if (list[i] == value) return true;
        }
        return false;
    }

}
