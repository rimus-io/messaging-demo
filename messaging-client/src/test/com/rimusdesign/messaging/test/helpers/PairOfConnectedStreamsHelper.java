package com.rimusdesign.messaging.test.helpers;


import java.io.*;


/**
 * Abstract helper class used by tests. Main purpose of this class is to provide two separate connected
 * streams so that data could be written to a stream, and the same data could be read back.
 * Also to avoid code repetition.
 *
 * @author Rimas Krivickas.
 */
public abstract class PairOfConnectedStreamsHelper {


    protected DataInputStream dataInputStreamA;
    protected DataOutputStream dataOutputStreamA;
    protected DataInputStream dataInputStreamB;
    protected DataOutputStream dataOutputStreamB;


    protected void instantiateStreams () throws IOException {

        instantiateA();
        instantiateB();
    }


    protected void closeStreams () throws IOException {

        // Close A
        dataInputStreamA.close();
        dataOutputStreamA.close();

        // Close B
        dataInputStreamB.close();
        dataOutputStreamB.close();
    }


    private void instantiateA () throws IOException {

        PipedInputStream inputStream = new PipedInputStream();
        PipedOutputStream outputStream = new PipedOutputStream(inputStream);

        dataInputStreamA = new DataInputStream(inputStream);
        dataOutputStreamA = new DataOutputStream(outputStream);
    }


    private void instantiateB () throws IOException {

        PipedInputStream inputStream = new PipedInputStream();
        PipedOutputStream outputStream = new PipedOutputStream(inputStream);

        dataInputStreamB = new DataInputStream(inputStream);
        dataOutputStreamB = new DataOutputStream(outputStream);
    }

}
