package com.rimusdesign.messaging.test.helpers;


import java.io.*;


/**
 * Abstract helper class used by tests. Main purpose of this class is to provide connected
 * streams so that data could be written to a stream, and the same data could be read back.
 * Also to avoid code repetition.
 *
 * @author Rimas Krivickas.
 */
public abstract class ConnectedStreamsHelper {


    protected DataInputStream dataInputStream;
    protected DataOutputStream dataOutputStream;


    protected void instantiateStreams () throws IOException {

        PipedInputStream inputStream = new PipedInputStream();
        PipedOutputStream outputStream = new PipedOutputStream(inputStream);

        dataInputStream = new DataInputStream(inputStream);
        dataOutputStream = new DataOutputStream(outputStream);
    }


    protected void closeStreams () throws IOException {

        dataInputStream.close();
        dataOutputStream.close();
    }

}
