package com.rimusdesign.messaging.core.connection.vo;


import java.net.Socket;


/**
 * A simple decorator that adds 'socket' parameter to 'ConnectionDetails'.
 *
 * @author Rimas Krivickas.
 * @see ConnectionDetails
 */
public class SocketConnectionDetails extends ConnectionDetails {


    private Socket socket;


    public SocketConnectionDetails (Socket socket, ConnectionDetails connectionDetails) {

        this(socket, connectionDetails.getConnectionType(), connectionDetails.getConnectionId());
    }


    public SocketConnectionDetails (Socket socket, int type, String connectionId) {

        super(type, connectionId);
        this.socket = socket;
    }


    public Socket getSocket () {

        return socket;
    }
}
