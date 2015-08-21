package com.rimusdesign.messaging.core.connection;


import com.rimusdesign.messaging.core.connection.vo.ConnectionDetails;

import java.io.DataInputStream;
import java.io.DataOutputStream;


/**
 * An interface that should be implemented by both - client and server to
 * handle handshake.
 * No further communication shall be allowed between client and server until
 * 'handshakeSuccessful' method returns 'true'.
 *
 * @author Rimas Krivickas.
 */
public interface HandshakeHandler {


    boolean handshakeSuccessful (DataInputStream inputStream, DataOutputStream outputStream);


    ConnectionDetails getConnectionDetails ();

}
