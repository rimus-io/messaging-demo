import com.rimusdesign.messaging.server.MessagingServer;

import java.io.*;


/**
 * @author  Rimas Krivickas.
 */
public class ServerLauncher {


    public static void main (String[] args) {


            MessagingServer messagingServer = new MessagingServer();
            messagingServer.configure();

            try {
                messagingServer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }

    }

}
