package com.rimusdesign.messaging.core.codecs;


import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;


/**
 * Common functionality used to decode all data frames.
 * This class is also used by other codecs.
 *
 * @author Rimas Krivickas.
 */
public class DataFrameCodec {


    public static ByteBuffer getBytesFromStream (DataInputStream inputStream, int bufferSize) throws IncompleteStreamException, IOException {

        ByteBuffer byteBuffer = ByteBuffer.allocate(bufferSize);

        // Copy data from stream into buffer
        for (int i = 0; i < bufferSize; i++) {

            try {
                byteBuffer.put(i, inputStream.readByte());
            } catch (EOFException e) {

                // If end of file reached, client has closed stream, throw exception so threads could be closed
                throw new IncompleteStreamException();
            }
        }

        return byteBuffer;
    }


    public static ByteBuffer decode (DataInputStream inputStream) throws IncompleteStreamException, IOException {

        // Retrieve header
        ByteBuffer headerBuffer = HeaderCodec.decode(inputStream);

        // Retrieve payload
        ByteBuffer payloadBuffer = DataFrameCodec.getBytesFromStream(inputStream, HeaderCodec.getPayloadLength(headerBuffer));

        // Allocate result buffer
        ByteBuffer resultBuffer = ByteBuffer.allocate(headerBuffer.capacity() + payloadBuffer.capacity());

        // Copy bytes to result buffer
        resultBuffer.put(headerBuffer);
        resultBuffer.put(payloadBuffer);

        // Reset buffer so it could be read from
        resultBuffer.flip();

        return resultBuffer;
    }
}
