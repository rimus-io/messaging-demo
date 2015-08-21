package com.rimusdesign.messaging.server.connection;


import com.rimusdesign.messaging.core.connection.vo.SocketConnectionDetails;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * @author Rimas Krivickas.
 */
public class ConnectionThread implements Runnable {


    private SocketConnectionDetails connectionDetails;
    private ConnectionContextFactory contextFactory;
    private ConnectionContext context;
    private LinkedBlockingQueue controlQueue;


    public ConnectionThread (SocketConnectionDetails connectionDetails, ConnectionContextFactory contextFactory,
                             LinkedBlockingQueue controlQueue) {

        this.connectionDetails = connectionDetails;
        this.contextFactory = contextFactory;
        this.controlQueue = controlQueue;
    }


    public void run () {


        try {
            execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //System.out.println("ConnectionThread: T_END");
    }


    protected void execute () throws IOException {

        // Get I/O streams
        DataInputStream inputStream = new DataInputStream(getSocket().getInputStream());
        DataOutputStream outputStream = new DataOutputStream(getSocket().getOutputStream());

        // Make context
        context = contextFactory.make(connectionDetails.getConnectionType(), controlQueue);

        // Start handling
        context.handle(inputStream, outputStream);

    }


    protected Socket getSocket () {

        return connectionDetails.getSocket();
    }

}
