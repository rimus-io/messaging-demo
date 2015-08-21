package com.rimusdesign.messaging.server.managers.control;


import java.util.concurrent.ConcurrentLinkedQueue;


/**
 * @author Rimas Krivickas.
 */
public class ControlMessage {


    private ControlMsgType type;
    private String topicId;
    private ConcurrentLinkedQueue dataQueue;


    // TODO 'topicId' should be bytes as per protocol
    public ControlMessage (ControlMsgType type, String topicId, ConcurrentLinkedQueue dataQueue) {

        this.type = type;
        this.topicId = topicId;
        this.dataQueue = dataQueue;
    }


    public ControlMsgType getType () {

        return type;
    }


    public ConcurrentLinkedQueue getDataQueue () {

        return dataQueue;
    }


    public String getTopicId () {

        return topicId;
    }
}
