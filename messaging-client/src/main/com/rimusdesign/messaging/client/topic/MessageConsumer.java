package com.rimusdesign.messaging.client.topic;


import java.nio.ByteBuffer;


/**
 * @author Rimas Krivickas.
 */
public interface MessageConsumer {


    void onMessage (ByteBuffer message);


    // TODO Not ideal solution, refactor
    void onError (String message);


}
