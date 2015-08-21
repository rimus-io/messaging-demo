package com.rimusdesign.messaging.server.connection;


import com.rimusdesign.messaging.core.codecs.protocol.constants.ConnectionType;
import com.rimusdesign.messaging.server.connection.policy.ServerPubConnectionPolicy;
import com.rimusdesign.messaging.server.connection.policy.ServerSubConnectionPolicy;

import java.util.concurrent.LinkedBlockingQueue;


/**
 * Creates a context with appropriate policy.
 *
 * @author Rimas Krivickas.
 */
public class ConnectionContextFactory {


    public ConnectionContext make (int connectionType, LinkedBlockingQueue controlQueue) {

        ConnectionContext context = null;

        switch (connectionType) {

            case ConnectionType.PUB:

                context = new ConnectionContext(new ServerPubConnectionPolicy(controlQueue));
                break;

            case ConnectionType.SUB:

                context = new ConnectionContext(new ServerSubConnectionPolicy(controlQueue));
                break;
        }

        context.init();

        return context;
    }
}
