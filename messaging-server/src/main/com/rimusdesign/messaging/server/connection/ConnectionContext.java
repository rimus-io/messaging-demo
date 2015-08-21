package com.rimusdesign.messaging.server.connection;


import com.rimusdesign.messaging.server.connection.policy.ConnectionPolicy;

import java.io.DataInputStream;
import java.io.DataOutputStream;


/**
 * Wraps a policy and forwards all method calls to concrete implementation.
 *
 * @author Rimas Krivickas.
 */
public class ConnectionContext implements ConnectionPolicy {


    private ConnectionPolicy policy;


    public ConnectionContext (ConnectionPolicy policy) {

        this.policy = policy;
    }


    public int[] acceptsFrameTypes () {

        return policy.acceptsFrameTypes();
    }


    public int[] sendsFrameTypes () {

        return policy.sendsFrameTypes();
    }


    public int[] acceptsOptions () {

        return policy.acceptsOptions();
    }


    public void init () {

        policy.init();
    }


    public void handle (DataInputStream inputStream, DataOutputStream outputStream) {

        policy.handle(inputStream, outputStream);
    }


}
