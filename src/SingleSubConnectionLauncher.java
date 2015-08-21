import com.rimusdesign.messaging.client.ClientConnection;
import com.rimusdesign.messaging.core.codecs.MessageFrameCodec;
import com.rimusdesign.messaging.core.codecs.protocol.constants.ConnectionType;
import com.rimusdesign.messaging.client.topic.Topic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;


/**
 * @author Rimas Krivickas.
 */
public class SingleSubConnectionLauncher {


    public static void main (String[] args) {

        System.out.println(ManagementFactory.getRuntimeMXBean().getName());

        String connectionId;
        String topicId;

        try {

            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

            // Acquire username
            System.out.println("Please provide your username:");
            connectionId = in.readLine();

            // Acquire topic
            System.out.println("Please provide the name of a chat group:");
            topicId = in.readLine();

            // Start connection
            ClientConnection connection = new ClientConnection(ConnectionType.SUB,connectionId);
            connection.configure();
            connection.connect();

            Topic topic = connection.createTopic(topicId);
            String message;
            while((message = MessageFrameCodec.getTextBody(topic.receiveMessage())) != null){
                System.out.println("Received: " + message);
                if(message.equals("bye")) break;
            }

            // Disconnect
            connection.disconnect();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
