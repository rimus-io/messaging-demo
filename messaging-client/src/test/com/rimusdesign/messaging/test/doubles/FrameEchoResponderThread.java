package com.rimusdesign.messaging.test.doubles;


import com.rimusdesign.messaging.core.codecs.DataFrameCodec;
import com.rimusdesign.messaging.core.codecs.IncompleteStreamException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;


/**
 * This thread reads data frame from input stream, and forwards
 * it to output stream without interpreting it.
 *
 * @author Rimas Krivickas.
 */
public class FrameEchoResponderThread implements Runnable {


    private DataInputStream inputStream;
    private DataOutputStream outputStream;


    public FrameEchoResponderThread (DataInputStream inputStream, DataOutputStream outputStream) {

        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }


    public void run () {

        try {
            // Read data
            ByteBuffer data = DataFrameCodec.decode(inputStream);

            // Send it back without interpreting it
            outputStream.write(data.array());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (IncompleteStreamException e) {
            e.printStackTrace();
        }
    }
}
