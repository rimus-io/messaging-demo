package com.rimusdesign.messaging.client.topic;


import com.rimusdesign.messaging.core.connection.vo.SocketConnectionDetails;


/**
 * @author Rimas Krivickas.
 */
public abstract class BaseTopic implements Topic {


    protected String topicId;
    protected SocketConnectionDetails socketConnectionDetails;


    public BaseTopic (String topicId, SocketConnectionDetails socketConnectionDetails) {

        this.topicId = topicId;
        this.socketConnectionDetails = socketConnectionDetails;
    }


    public SocketConnectionDetails getDetails () {

        return socketConnectionDetails;
    }


    public String getId () {

        return topicId;
    }

}
