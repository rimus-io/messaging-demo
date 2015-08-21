package com.rimusdesign.messaging.client.topic;


import com.rimusdesign.messaging.core.codecs.DataFrameCodec;
import com.rimusdesign.messaging.core.codecs.IncompleteStreamException;
import com.rimusdesign.messaging.core.codecs.MessageFrameCodec;
import com.rimusdesign.messaging.core.codecs.OptionFrameCodec;
import com.rimusdesign.messaging.core.codecs.protocol.constants.MessageAck;
import com.rimusdesign.messaging.core.codecs.protocol.constants.MessageType;
import com.rimusdesign.messaging.core.codecs.protocol.constants.OptionType;
import com.rimusdesign.messaging.core.connection.vo.SocketConnectionDetails;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;


/**
 * @author Rimas Krivickas.
 */
public class ClientPubTopic extends BaseTopic {


    private MessageProducer messageProducer;


    public ClientPubTopic (String topicId, SocketConnectionDetails socketConnectionDetails) {

        super(topicId, socketConnectionDetails);

        setTopic();
    }


    // TODO Separate this out like handshake handler, this also should be blocking I believe
    private void setTopic () {
        // Get I/O streams
        try {

            DataInputStream inputStream = new DataInputStream(socketConnectionDetails.getSocket().getInputStream());
            DataOutputStream outputStream = new DataOutputStream(socketConnectionDetails.getSocket().getOutputStream());

            // Send SETTOPIC option
            OptionFrameCodec.encode(outputStream, OptionType.TOPIC_SET, getId().getBytes());

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

        // If instance of message producer exists return it
        if (messageProducer != null) return messageProducer;

        // Get I/O streams
        DataInputStream inputStream = new DataInputStream(socketConnectionDetails.getSocket().getInputStream());
        DataOutputStream outputStream = new DataOutputStream(socketConnectionDetails.getSocket().getOutputStream());

        // Else, create it and then return it
        messageProducer = new ClientPubMessageProducer(inputStream, outputStream);
        return messageProducer;
    }


    public void setMessageConsumer (MessageConsumer consumer) throws UnsupportedOperationException, IOException {

        throw new UnsupportedOperationException("Client 'PUB' connection cannot receive messages.");
    }


    public ByteBuffer receiveMessage () throws UnsupportedOperationException, IOException {

        throw new UnsupportedOperationException("Client 'PUB' connection cannot receive messages.");
    }
}


class ClientPubMessageProducer implements MessageProducer {


    private DataInputStream inputStream;
    private DataOutputStream outputStream;


    public ClientPubMessageProducer (DataInputStream inputStream, DataOutputStream outputStream) {

        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }


    public void sendTextMessage (String message) throws IOException {

        MessageFrameCodec.encode(outputStream, MessageType.TEXT, MessageAck.NO, message);
    }


    public void sendRawData (byte[] data) throws IOException {

        MessageFrameCodec.encode(outputStream, MessageType.TEXT, MessageAck.NO, data);
    }
}