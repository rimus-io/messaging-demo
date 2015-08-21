import com.rimusdesign.messaging.client.ClientConnection;
import com.rimusdesign.messaging.client.topic.MessageProducer;
import com.rimusdesign.messaging.core.codecs.protocol.constants.ConnectionType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;


/**
 * @author Rimas Krivickas.
 */
public class ChatBotServerLauncher {


    public static void main (String[] args) {


        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        int numConnections;
        String topic;
        String currentConnectionName;

        try {


            // Acquire number of connections to launch
            System.out.println("Please provide the number of connections to launch:");

            numConnections = Integer.parseInt(in.readLine());

            // Acquire topic
            System.out.println("Please provide the name of a chat group:");
            topic = in.readLine();

            while (numConnections != 0) {

                currentConnectionName = "Chatter-Ma-Bob-" + numConnections;

                System.out.println(currentConnectionName);

                new Thread(new ChatBot(currentConnectionName, topic)).start();

                numConnections--;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}


class ChatBot implements Runnable {


    private String connectionName;
    private String topic;

    private String[] lipsum;


    public ChatBot (String connectionName, String topic) {

        this.connectionName = connectionName;
        this.topic = topic;

        lipsum = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum".split(" ");
    }


    public void run () {

        MessageProducer producer;

        try {
            // Start connection
            ClientConnection connection = new ClientConnection(ConnectionType.PUB, connectionName);
            connection.configure();
            connection.connect();

            // Get producer
            producer = connection.createTopic(topic).getMessageProducer();

            // Start writing messages at random intervals
            while (true) {
                Thread.sleep(getRandomInterval() * 1000);
                sendRandomMessage(producer);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void sendRandomMessage (MessageProducer producer) throws IOException {

        String text = connectionName + ":";
        int messageLen = getRandomInterval();

        for (int i = 0; i < messageLen; i++) {
            text += " " + lipsum[randInt(0, lipsum.length-1)];
        }

        producer.sendTextMessage(text);
    }


    private int getRandomInterval () {

        return randInt(5, 30);
    }


    private int randInt (int min, int max) {

        Random rand = new Random();

        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
}
