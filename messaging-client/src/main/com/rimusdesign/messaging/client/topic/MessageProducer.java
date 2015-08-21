package com.rimusdesign.messaging.client.topic;


import java.io.IOException;


/**
 * @author Rimas Krivickas.
 */
public interface MessageProducer {


    void sendTextMessage (String message) throws IOException;


    void sendRawData (byte[] data) throws IOException;

}
