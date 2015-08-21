package com.rimusdesign.messaging.client.connection;


import com.rimusdesign.messaging.core.codecs.*;
import com.rimusdesign.messaging.core.codecs.protocol.constants.Acknowledgement;
import com.rimusdesign.messaging.core.codecs.protocol.constants.DataFrameType;
import com.rimusdesign.messaging.core.connection.HandshakeHandler;
import com.rimusdesign.messaging.core.connection.vo.ConnectionDetails;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.UUID;


/**
 * @author Rimas Krivickas.
 */
public class ClientHandshakeHandler implements HandshakeHandler {


    private ConnectionDetails connectionDetails;
    private UUID uid;
    private ByteBuffer handshakeAck;


    public ClientHandshakeHandler (int connectionType, String connectionId) {

        connectionDetails = new ConnectionDetails(connectionType, connectionId);
    }


    // TODO Timeout handling
    public boolean handshakeSuccessful (DataInputStream inputStream, DataOutputStream outputStream) {

        try {

            // Send handshake frame
            uid = HandshakeFrameCodec.encode(outputStream, getConnectionDetails().getConnectionType(), getConnectionDetails().getConnectionId());

            // Read handshake response
            handshakeAck = DataFrameCodec.decode(inputStream);

            // If any other than acknowledgement frame comes back - fail
            if (HeaderCodec.getFrameType(handshakeAck) != DataFrameType.ACKNOWLEDGEMENT) return false;

            // If acknowledgement comers back with incorrect uid - fail
            if (!HeaderCodec.getUid(handshakeAck).equals(uid)) return false;

            // If acknowledgement value is not equal 'Acknowledgement.SUCCESS' - fail
            if (AckFrameCodec.getAckValue(handshakeAck) != Acknowledgement.SUCCESS) return false;

            // If response validates - connection is successful
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
}
