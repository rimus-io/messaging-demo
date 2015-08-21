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
public class ServerSubConnectionPolicy extends BaseServerConnectionPolicy {


    private ConcurrentLinkedQueue inboundQueue;


    public ServerSubConnectionPolicy (LinkedBlockingQueue controlQueue) {

        super(controlQueue);
    }


    @Override
    public void init () {

        super.init();
        inboundQueue = new ConcurrentLinkedQueue();
    }


    public int[] acceptsFrameTypes () {

        return new int[]{
                DataFrameType.OPTION
        };
    }


    public int[] sendsFrameTypes () {

        return new int[]{
                DataFrameType.ACKNOWLEDGEMENT,
                DataFrameType.MESSAGE
        };
    }


    public int[] acceptsOptions () {

        return new int[]{
                OptionType.TOPIC_SUBSCRIBE,
                OptionType.TOPIC_UNSUBSCRIBE,
                OptionType.TOPIC_UNSUBSCRIBE_ALL
        };
    }


    public void handle (DataInputStream inputStream, DataOutputStream outputStream) {

        ByteBuffer message;
        try {
            // Receive subscription frame, if everything is fine - progress
            if (handleOption(DataFrameCodec.decode(inputStream), outputStream)) {
                // TODO Resolve this somehow
                while (true) {
                    message = (ByteBuffer) inboundQueue.poll();
                    if (message != null) {
                        outputStream.write(message.array());
                    }
                }
            }
        } catch (IncompleteStreamException e) {
//            e.printStackTrace();
        } catch (IOException e) {
//            e.printStackTrace();
        }
    }


    private boolean handleOption (ByteBuffer option, DataOutputStream outputStream) {

        int ackValue = Acknowledgement.FAIL;

        if (listContainsValue(acceptsOptions(), OptionFrameCodec.getOptionType(option))) {
            ackValue = Acknowledgement.SUCCESS;

            // Pass connection details to publishers queue
            controlQueue.add(new ControlMessage(ControlMsgType.REGISTER_TOPIC_SUBSCRIBER, new String(OptionFrameCodec.getOptionValue(option)), inboundQueue));

        }
        try {
            AckFrameCodec.encode(outputStream, HeaderCodec.getUid(option), ackValue);
        } catch (IOException e) {
//            e.printStackTrace();
            return false;
        }
        return ackValue == Acknowledgement.SUCCESS;
    }

}
