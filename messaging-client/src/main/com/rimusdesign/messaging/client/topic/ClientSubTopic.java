package com.rimusdesign.messaging.client.topic;


import com.rimusdesign.messaging.core.codecs.DataFrameCodec;
import com.rimusdesign.messaging.core.codecs.IncompleteStreamException;
import com.rimusdesign.messaging.core.codecs.OptionFrameCodec;
import com.rimusdesign.messaging.core.codecs.protocol.constants.OptionType;
import com.rimusdesign.messaging.core.connection.vo.SocketConnectionDetails;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.nio.ByteBuffer;


/**
 * @author Rimas Krivickas.
 */
public class ClientSubTopic extends BaseTopic {


    private MessageConsumer consumer;

    private DataInputStream inputStream;

    private MessageMonitor monitor;


    public ClientSubTopic (String topicId, SocketConnectionDetails socketConnectionDetails) {

        super(topicId, socketConnectionDetails);

        setTopic();
    }


    // TODO Separate this out like handshake handler, this also should be blocking I believe
    private void setTopic () {
        // Get I/O streams
        try {

            DataInputStream inputStream = new DataInputStream(socketConnectionDetails.getSocket().getInputStream());
            DataOutputStream outputStream = new DataOutputStream(socketConnectionDetails.getSocket().getOutputStream());

            // Send TOPICSUBSCRIBE option
            OptionFrameCodec.encode(outputStream, OptionType.TOPIC_SUBSCRIBE, getId().getBytes());

            // Read response
            ByteBuffer ack = DataFrameCodec.decode(inputStream);
            //System.out.println("ack value: " + AckFrameCodec.getAckValue(ack));

            // TODO Handle if not acknowledged

        } catch (IOException e) {
            e.printStackTrace();
        } catch (IncompleteStreamException e) {
            e.printStackTrace();
        }
    }


    public MessageProducer getMessageProducer () throws UnsupportedOperationException, IOException {

        throw new UnsupportedOperationException("Client 'SUB' connection cannot send messages.");
    }


    public void setMessageConsumer (MessageConsumer consumer) throws UnsupportedOperationException, IOException {

        // TODO Check if messages will get delivered to different consumer if set to a different one after monitor is started
        this.consumer = consumer;

        if (monitor == null) {

            // Get input stream
            if (inputStream == null)
                inputStream = new DataInputStream(socketConnectionDetails.getSocket().getInputStream());

            // Start monitor thread
            monitor = new MessageMonitor(inputStream, consumer);

            new Thread(monitor).start();

        } else {
            // TODO Pass new consumer into monitor
        }
    }


    public ByteBuffer receiveMessage () throws UnsupportedOperationException, IOException {

        if (consumer != null)
            throw new UnsupportedOperationException("Message consumer has been detected,cannot receive synchronously");

        if (inputStream == null)
            inputStream = new DataInputStream(socketConnectionDetails.getSocket().getInputStream());

        ByteBuffer message = null;

        try {
            message = DataFrameCodec.decode(inputStream);
        } catch (IncompleteStreamException e) {
            // TODO Analyse what are the implications and how to handle
            e.printStackTrace();
        }
        return message;
    }
}


class MessageMonitor implements Runnable {


    private DataInputStream inputStream;
    private MessageConsumer consumer;


    public MessageMonitor (DataInputStream inputStream, MessageConsumer consumer) {

        this.inputStream = inputStream;

        this.consumer = consumer;
    }


    public void run () {

        ByteBuffer message;
        try {
            while ((message = DataFrameCodec.decode(inputStream)) != null) {
                consumer.onMessage(message);
            }
        } catch (IncompleteStreamException e) {
            e.printStackTrace();
            // TODO Add a way to notify consumer, this is temp
            consumer.onError(e.getMessage());
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
