package com.rimusdesign.messaging.test.doubles;


import com.rimusdesign.messaging.core.codecs.AckFrameCodec;
import com.rimusdesign.messaging.core.codecs.DataFrameCodec;
import com.rimusdesign.messaging.core.codecs.HeaderCodec;
import com.rimusdesign.messaging.core.codecs.IncompleteStreamException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;


/**
 * This thread reads data frame, and sends an acknowledgement
 * with UUID provided in constructor.
 *
 * @author Rimas Krivickas.
 */
public class AckResponderWithUUIDThread implements Runnable {


    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private UUID uid;
    private int ackValue;


    public AckResponderWithUUIDThread (DataInputStream inputStream, DataOutputStream outputStream, UUID uid, int ackValue) {

        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.uid = uid;
        this.ackValue = ackValue;
    }


    public void run () {

        try {
            HeaderCodec.getUid(DataFrameCodec.decode(inputStream));
            AckFrameCodec.encode(outputStream, uid, ackValue);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IncompleteStreamException e) {
            e.printStackTrace();
        }
    }
}
