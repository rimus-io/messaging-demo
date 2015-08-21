package com.rimusdesign.messaging.core.connection.vo;


/**
 * A simple value object to store connection details.
 *
 * @author Rimas Krivickas.
 */
public class ConnectionDetails {


    private int connctionType;
    private String connectionId;


    public ConnectionDetails (int connctionType, String connectionId) {

        this.connctionType = connctionType;
        this.connectionId = new String(connectionId);
    }


    public int getConnectionType () {

        return connctionType;
    }


    public String getConnectionId () {

        return connectionId;
    }
}
