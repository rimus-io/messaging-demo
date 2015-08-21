package com.rimusdesign.messaging.core.codecs;


import com.rimusdesign.messaging.core.codecs.protocol.constants.DataFrameType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.UUID;


/**
 * Used to encode/decode 'option' as per protocol definition
 *
 * @author Rimas Krivickas.
 */
public class OptionFrameCodec {


    public static UUID encode (DataOutputStream outputStream, int type, byte[] value) throws IOException {

        // Make an UUID
        UUID uid = UUID.randomUUID();

        // Work out expected payload length
        int payloadLength = 1; // Accounts for type flag
        payloadLength += value.length; // Add length of the option value

        // Write header
        HeaderCodec.encode(outputStream, DataFrameType.OPTION, uid, payloadLength);

        // Write connection type (1byte)
        outputStream.write(type);

        // Write value
        outputStream.write(value);

        return uid;

    }


    public static ByteBuffer decode (DataInputStream inputStream) throws IncompleteStreamException, IOException {

        return DataFrameCodec.decode(inputStream);
    }


    public static int getOptionType (ByteBuffer byteBuffer) {

        return byteBuffer.get(HeaderCodec.HEADER_SIZE);
    }


    public static byte[] getOptionValue (ByteBuffer byteBuffer) {

        // Work out value length
        int valueLength = HeaderCodec.getPayloadLength(byteBuffer) - 1; // '-1' to account for type flag

        // Set a position to read from
        byteBuffer.position(HeaderCodec.HEADER_SIZE + 1); // Accounts for header and option type flag

        // Read value bytes
        byte[] result = new byte[valueLength];

        for (int i = 0; i < valueLength; i++) {
            result[i] = byteBuffer.get();
        }

        // Reset position
        byteBuffer.position(0);

        return result;
    }

}
