package com.rimusdesign.messaging.server;


import com.rimusdesign.messaging.server.connection.ConnectionHandler;
import com.rimusdesign.messaging.server.managers.control.ControlQueueManager;
import com.rimusdesign.messaging.server.managers.control.TopicRouter;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * This class is the main server class.
 * It is only responsible for binding server socket to a port, starting up managers, and accepting connections.
 * Once connection is accepted it is then forwarded to connection handler for processing.
 * Managers deal with processing data from connections.
 *
 * @author Rimas Krivickas.
 */
public class MessagingServer {


    protected int port;
    protected ServerSocket serverSocket;
    protected ExecutorService executorService;
    protected LinkedBlockingQueue controlQueue;

    protected boolean isConfigured;
    protected boolean isRunning;


    public MessagingServer () {

        // Use default port
        this(7777);
    }


    public MessagingServer (int port) {

        this.port = port;
    }


    public void configure () {

        // Create queues
        controlQueue = new LinkedBlockingQueue();

        // Create thread pool for connection handlers
        // TODO Make pool size externally configurable
        executorService = Executors.newFixedThreadPool(2);

        // Flag server as configured
        isConfigured = true;
    }


    public void start () throws IOException, IllegalStateException {

        // Do not start if server hasn't been configured
        if (!isConfigured) {
            throw new IllegalStateException("Server needs to be configured using 'configure' method before it can be started.");
        }

        // Bind server socket
        serverSocket = new ServerSocket(port);

        // Mark server as running
        isRunning = true;

        // Start managers
        new Thread(new ControlQueueManager(new TopicRouter(), controlQueue)).start();


        // Start monitoring for connections
        monitorForConnections();
    }


    protected void monitorForConnections () {

        while (isRunning) {

            try {
                // If connection is accepted - forward it to connection handler for further processing
                executorService.execute(new ConnectionHandler(serverSocket.accept(), controlQueue));
            } catch (IOException e) {
                // TODO add proper logging and handling here
                e.printStackTrace();
            }
        }
    }
}
