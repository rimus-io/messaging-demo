import com.rimusdesign.messaging.client.ClientConnection;
import com.rimusdesign.messaging.core.codecs.protocol.constants.ConnectionType;
import com.rimusdesign.messaging.client.topic.MessageProducer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;


/**
 * @author  Rimas Krivickas.
 */
public class SinglePubConnectionLauncher {


    public static void main (String[] args) {

        System.out.println(ManagementFactory.getRuntimeMXBean().getName());

        String connectionId;
        String topic;
        MessageProducer producer;

        try {

            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

            // Acquire username
            System.out.println("Please provide your username:");
            connectionId = in.readLine();

            // Acquire topic
            System.out.println("Please provide the name of a chat group:");
            topic = in.readLine();

            // Start connection
            ClientConnection connection = new ClientConnection(ConnectionType.PUB,connectionId);
            connection.configure();
            connection.connect();

            producer =  connection.createTopic(topic).getMessageProducer();

            System.out.println("Start sending messages!!");
            String text;
            while (!(text=in.readLine()).equals("quit")){
                if(!text.isEmpty()) producer.sendTextMessage(connectionId + ": "+text);
            }
            // Disconnect
            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
