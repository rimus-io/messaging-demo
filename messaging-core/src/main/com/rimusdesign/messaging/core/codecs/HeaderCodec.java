package com.rimusdesign.messaging.core.codecs;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.UUID;


/**
 * Used to encode/decode 'header' as per protocol definition
 *
 * @author Rimas Krivickas.
 */
public class HeaderCodec {


    public static final int HEADER_SIZE = 21;


    /**
     * This method is intended for use by other codecs.
     *
     * @param outputStream
     * @param frameType
     * @param frameUid
     * @param payloadLength
     * @throws IOException
     */
    public static void encode (DataOutputStream outputStream, int frameType, UUID frameUid, int payloadLength) throws IOException {

        // Write frame type (1byte)
        outputStream.write(frameType);

        // Write UUID (16bytes)
        outputStream.writeLong(frameUid.getMostSignificantBits());
        outputStream.writeLong(frameUid.getLeastSignificantBits());

        // Write payload length (4bytes)
        outputStream.writeInt(payloadLength);
    }


    public static ByteBuffer decode (DataInputStream inputStream) throws IncompleteStreamException, IOException {

        return DataFrameCodec.getBytesFromStream(inputStream, HEADER_SIZE);
    }


    public static int getFrameType (ByteBuffer byteBuffer) {

        return byteBuffer.get(0);
    }


    public static UUID getUid (ByteBuffer byteBuffer) {

        return new UUID(byteBuffer.getLong(1), byteBuffer.getLong(9));
    }


    public static int getPayloadLength (ByteBuffer byteBuffer) {

        return byteBuffer.getInt(17);
    }
}
