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
 * This thread reads data frame, gets UUID, and
 * forwards it back in acknowledgement frame with the same UUID.
 *
 * @author Rimas Krivickas.
 */
public class AckResponderThread implements Runnable {


    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private int ackValue;


    public AckResponderThread (DataInputStream inputStream, DataOutputStream outputStream, int ackValue) {

        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.ackValue = ackValue;
    }


    public void run () {

        try {
            UUID uid = HeaderCodec.getUid(DataFrameCodec.decode(inputStream));
            AckFrameCodec.encode(outputStream, uid, ackValue);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IncompleteStreamException e) {
            e.printStackTrace();
        }
    }
}
