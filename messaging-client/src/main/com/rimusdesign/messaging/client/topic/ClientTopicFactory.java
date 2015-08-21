package com.rimusdesign.messaging.client.topic;


import com.rimusdesign.messaging.core.codecs.protocol.constants.ConnectionType;
import com.rimusdesign.messaging.core.connection.vo.SocketConnectionDetails;


/**
 * @author Rimas Krivickas.
 */
public class ClientTopicFactory {


    public static Topic make (SocketConnectionDetails socketConnectionDetails, String topicName) {

        Topic topic = null;

        switch (socketConnectionDetails.getConnectionType()) {
            case ConnectionType.PUB:
                topic = new ClientPubTopic(topicName, socketConnectionDetails);
                break;
            case ConnectionType.SUB:
                topic = new ClientSubTopic(topicName, socketConnectionDetails);
                break;
        }

        return topic;
    }
}
