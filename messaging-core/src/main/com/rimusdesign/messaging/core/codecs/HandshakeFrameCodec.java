package com.rimusdesign.messaging.core.codecs;


import com.rimusdesign.messaging.core.codecs.protocol.constants.DataFrameType;
import com.rimusdesign.messaging.core.codecs.protocol.constants.Version;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.UUID;


/**
 * Used to encode/decode 'handshake' as per protocol definition
 *
 * @author Rimas Krivickas.
 */
public class HandshakeFrameCodec {


    public static UUID encode (DataOutputStream outputStream, int connectionType, char[] connectionId) throws IOException {

        // Make an UUID
        UUID uid = UUID.randomUUID();

        // Work out expected payload length
        int payloadLength = 2; // Accounts for version and connection type flags
        payloadLength += connectionId.length * 2;

        // Write header
        HeaderCodec.encode(outputStream, DataFrameType.HANDSHAKE, uid, payloadLength);

        // Write version (1byte)
        outputStream.write(Version.protocolVersion());

        // Write connection connection type (1byte)
        outputStream.write(connectionType);


        // Write 'connectionId' (number of bytes = connectionId.length x 2)
        outputStream.writeChars(new String(connectionId));

        return uid;

    }


    public static UUID encode (DataOutputStream outputStream, int connectionType, String connectionId) throws IOException {

        return HandshakeFrameCodec.encode(outputStream, connectionType, connectionId.toCharArray());
    }


    public static ByteBuffer decode (DataInputStream inputStream) throws IncompleteStreamException, IOException {

        return DataFrameCodec.decode(inputStream);
    }


    public static int getProtocolVersion (ByteBuffer byteBuffer) {

        return byteBuffer.get(HeaderCodec.HEADER_SIZE);
    }


    public static int getConnectionType (ByteBuffer byteBuffer) {

        return byteBuffer.get(HeaderCodec.HEADER_SIZE + 1);
    }


    public static char[] getConnectionId (ByteBuffer byteBuffer) {

        // Work out 'connectionId' length in chars
        int connectionIdLength = HeaderCodec.getPayloadLength(byteBuffer) - 2; // '-2' to account for flag bytes
        connectionIdLength /= 2; // Turn byte length into char length


        // Set a position to read from
        byteBuffer.position(HeaderCodec.HEADER_SIZE + 2); // Accounts for header and flags

        // Read chars into response
        char[] result = new char[connectionIdLength];

        for (int i = 0; i < connectionIdLength; i++) {
            result[i] = byteBuffer.getChar();
        }

        // Reset position
        byteBuffer.position(0);

        return result;
    }
}
