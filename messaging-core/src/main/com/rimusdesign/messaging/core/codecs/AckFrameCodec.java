package com.rimusdesign.messaging.core.codecs;


import com.rimusdesign.messaging.core.codecs.protocol.constants.DataFrameType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.UUID;


/**
 * Used to encode/decode 'acknowledgement' as per protocol definition
 *
 * @author Rimas Krivickas.
 */
public class AckFrameCodec {


    public static UUID encode (DataOutputStream outputStream, UUID uid, int ackValue) throws IOException {

        // Write header
        HeaderCodec.encode(outputStream, DataFrameType.ACKNOWLEDGEMENT, uid, 1);

        // Write ackValue type (1byte)
        outputStream.write(ackValue);

        return uid;
    }


    public static ByteBuffer decode (DataInputStream inputStream) throws IncompleteStreamException, IOException {

        return DataFrameCodec.decode(inputStream);
    }


    public static int getAckValue (ByteBuffer byteBuffer) {

        return byteBuffer.get(HeaderCodec.HEADER_SIZE);
    }

}
