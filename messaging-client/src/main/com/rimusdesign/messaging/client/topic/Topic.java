package com.rimusdesign.messaging.client.topic;


import com.rimusdesign.messaging.core.connection.vo.SocketConnectionDetails;

import java.io.IOException;
import java.nio.ByteBuffer;


/**
 * @author Rimas Krivickas.
 */
public interface Topic {


    SocketConnectionDetails getDetails ();


    String getId ();


    MessageProducer getMessageProducer () throws UnsupportedOperationException, IOException;


    void setMessageConsumer (MessageConsumer consumer) throws UnsupportedOperationException, IOException;


    ByteBuffer receiveMessage () throws UnsupportedOperationException, IOException;
}
