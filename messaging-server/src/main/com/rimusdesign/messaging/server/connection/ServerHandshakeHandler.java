package com.rimusdesign.messaging.server.connection;


import com.rimusdesign.messaging.core.codecs.AckFrameCodec;
import com.rimusdesign.messaging.core.codecs.HandshakeFrameCodec;
import com.rimusdesign.messaging.core.codecs.HeaderCodec;
import com.rimusdesign.messaging.core.codecs.IncompleteStreamException;
import com.rimusdesign.messaging.core.codecs.protocol.constants.*;
import com.rimusdesign.messaging.core.connection.HandshakeHandler;
import com.rimusdesign.messaging.core.connection.vo.ConnectionDetails;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;


/**
 * @author Rimas Krivickas.
 */
public class ServerHandshakeHandler implements HandshakeHandler {


    private ByteBuffer handshakeFrame;
    protected ConnectionDetails connectionDetails;


    // TODO Timeout handling
    public boolean handshakeSuccessful (DataInputStream inputStream, DataOutputStream outputStream) {

        try {

            // Read handshake frame
            handshakeFrame = HandshakeFrameCodec.decode(inputStream);

            // Check if the frame received is a handshake frame, else reject connection
            if (HeaderCodec.getFrameType(handshakeFrame) != DataFrameType.HANDSHAKE) {
                sendAcknowledgement(outputStream, Acknowledgement.FAIL);
                return false;
            }

            // Check if protocol version matches supported protocol version, else reject connection
            if (HandshakeFrameCodec.getProtocolVersion(handshakeFrame) != Version.protocolVersion()) {
                sendAcknowledgement(outputStream, Acknowledgement.FAIL);
                return false;
            }

            // Get connection type
            int connectionType = HandshakeFrameCodec.getConnectionType(handshakeFrame);

            // Check if connection type is valid, else reject connection
            if (connectionType != ConnectionType.PUB && connectionType != ConnectionType.SUB) {
                sendAcknowledgement(outputStream, Acknowledgement.FAIL);
                return false;
            }

            // Get connection ID
            char[] connectionId = HandshakeFrameCodec.getConnectionId(handshakeFrame);

            // Check if connection ID matches length restrictions, else reject connection
            if (connectionId.length < Handshake.MIN_CONNECTION_ID_LEN || connectionId.length > Handshake.MAX_CONNECTION_ID_LEN) {
                sendAcknowledgement(outputStream, Acknowledgement.FAIL);
                return false;
            }

            // Instantiate connection details object
            connectionDetails = new ConnectionDetails(connectionType, new String(connectionId));

            // Send success acknowledgement
            sendAcknowledgement(outputStream, Acknowledgement.SUCCESS);

            return true;

        } catch (IOException e) {
            // TODO Decide if this needs ot be logged
        } catch (IncompleteStreamException e) {
            e.printStackTrace();
        }

        return false;
    }


    public ConnectionDetails getConnectionDetails () {

        return connectionDetails;
    }


    private void sendAcknowledgement (DataOutputStream outputStream, int ackValue) throws IOException {

        AckFrameCodec.encode(outputStream, HeaderCodec.getUid(handshakeFrame), ackValue);
    }


}
