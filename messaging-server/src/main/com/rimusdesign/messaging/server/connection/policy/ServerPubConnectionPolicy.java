package com.rimusdesign.messaging.server.connection.policy;


import com.rimusdesign.messaging.core.codecs.*;
import com.rimusdesign.messaging.core.codecs.protocol.constants.Acknowledgement;
import com.rimusdesign.messaging.core.codecs.protocol.constants.DataFrameType;
import com.rimusdesign.messaging.core.codecs.protocol.constants.OptionType;
import com.rimusdesign.messaging.server.managers.control.ControlMessage;
import com.rimusdesign.messaging.server.managers.control.ControlMsgType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * @author Rimas Krivickas.
 */
public class ServerPubConnectionPolicy extends BaseServerConnectionPolicy {


    private boolean hasRegisteredTopic;
    private ConcurrentLinkedQueue outboundQueue;


    public ServerPubConnectionPolicy (LinkedBlockingQueue controlQueue) {

        super(controlQueue);
    }


    @Override
    public void init () {

        super.init();
        outboundQueue = new ConcurrentLinkedQueue();
    }


    public int[] acceptsFrameTypes () {

        return new int[]{
                DataFrameType.OPTION,
                DataFrameType.MESSAGE
        };
    }


    public int[] sendsFrameTypes () {

        return new int[]{
                DataFrameType.ACKNOWLEDGEMENT
        };
    }


    public int[] acceptsOptions () {

        return new int[]{
                OptionType.TOPIC_SET
        };
    }


    public void handle (DataInputStream inputStream, DataOutputStream outputStream) {

        try {

            while (true) {

                handleReceivedData(DataFrameCodec.decode(inputStream), inputStream, outputStream);
            }
        } catch (IncompleteStreamException e) {
//            e.printStackTrace();
        } catch (IOException e) {
//            e.printStackTrace();
        }
    }


    private void handleReceivedData (ByteBuffer byteBuffer, DataInputStream inputStream, DataOutputStream outputStream) {

        // Check if connection handles this data frame type
        if (!listContainsValue(acceptsFrameTypes(), HeaderCodec.getFrameType(byteBuffer))) {
            // TODO Do something if necessary
        }

        // Forward message to the relevant handler
        switch (HeaderCodec.getFrameType(byteBuffer)) {

            case DataFrameType.OPTION:

                handleOption(byteBuffer, outputStream);
                break;

            case DataFrameType.MESSAGE:

                handleMessage(byteBuffer);
                break;
        }
    }


    private void handleOption (ByteBuffer option, DataOutputStream outputStream) {

        int ackValue = Acknowledgement.FAIL;

        if (listContainsValue(acceptsOptions(), OptionFrameCodec.getOptionType(option))) {
            ackValue = Acknowledgement.SUCCESS;

            // Pass connection details to publishers queue
            controlQueue.add(new ControlMessage(ControlMsgType.REGISTER_TOPIC_PUBLISHER, new String(OptionFrameCodec.getOptionValue(option)), outboundQueue));

            // Mark as has registered topics
            hasRegisteredTopic = true;
        }
        try {
            AckFrameCodec.encode(outputStream, HeaderCodec.getUid(option), ackValue);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void handleMessage (ByteBuffer message) {

        if (hasRegisteredTopic) {
            //System.out.println("handleMessage: " + MessageFrameCodec.getTextBody(message));
            outboundQueue.offer(message);
        }
    }


}
