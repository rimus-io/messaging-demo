package com.rimusdesign.messaging.core.codecs;


import com.rimusdesign.messaging.core.codecs.protocol.constants.DataFrameType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.UUID;


/**
 * Used to encode/decode 'message' as per protocol definition
 *
 * @author Rimas Krivickas.
 */
public class MessageFrameCodec {


    public static UUID encode (DataOutputStream outputStream, int messageType, int requireAck, byte[] body) throws IOException {

        // Make an UUID
        UUID uid = UUID.randomUUID();

        // Work out expected payload length
        int payloadLength = 2; // Accounts for message type and acknowledgement flags
        payloadLength += body.length; // Add length of the option value

        // Write header
        HeaderCodec.encode(outputStream, DataFrameType.MESSAGE, uid, payloadLength);

        // Write message message type (1byte)
        outputStream.write(messageType);

        // Write acknowledgement required flag (1byte)
        outputStream.write(requireAck);

        // Write body
        outputStream.write(body);

        return uid;
    }


    public static UUID encode (DataOutputStream outputStream, int messageType, int requireAck, String text) throws IOException {

        return encode(outputStream, messageType, requireAck, text.getBytes());
    }


    public static ByteBuffer decode (DataInputStream inputStream) throws IncompleteStreamException, IOException {

        return DataFrameCodec.decode(inputStream);
    }


    public static int getMessageType (ByteBuffer byteBuffer) {

        return byteBuffer.get(HeaderCodec.HEADER_SIZE);
    }


    public static int getAckFlag (ByteBuffer byteBuffer) {

        return byteBuffer.get(HeaderCodec.HEADER_SIZE + 1);
    }


    public static byte[] getBody (ByteBuffer byteBuffer) {

        // Work out body length
        int bodyLength = HeaderCodec.getPayloadLength(byteBuffer) - 2; // '-2' to account for flags

        // Set a position to read from
        byteBuffer.position(HeaderCodec.HEADER_SIZE + 2); // Accounts for flags

        // Read body bytes
        byte[] result = new byte[bodyLength];

        for (int i = 0; i < bodyLength; i++) {
            result[i] = byteBuffer.get();
        }

        // Reset position
        byteBuffer.position(0);

        return result;
    }


    public static String getTextBody (ByteBuffer byteBuffer) {

        return new String(MessageFrameCodec.getBody(byteBuffer));
    }
}
