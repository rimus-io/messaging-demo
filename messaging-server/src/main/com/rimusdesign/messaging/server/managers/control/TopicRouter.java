package com.rimusdesign.messaging.server.managers.control;


import com.rimusdesign.messaging.server.managers.topic.TopicHandler;

import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;


/**
 * @author Rimas Krivickas.
 */
public class TopicRouter {


    private HashMap<String, TopicHandler> topicHandlers = new HashMap();


    public void handleTopicPublisher (String topicId, ConcurrentLinkedQueue dataQueue) {

        if (!topicHandlers.containsKey(topicId)) topicHandlers.put(topicId, new TopicHandler());
        //System.out.println("handleTopicPublisher: " + topicId);
        topicHandlers.get(topicId).addPublisher(dataQueue);
    }


    public void handleTopicSubscriber (String topicId, ConcurrentLinkedQueue dataQueue) {

        if (!topicHandlers.containsKey(topicId)) topicHandlers.put(topicId, new TopicHandler());
        //System.out.println("handleTopicSubscriber: " + topicId);
        topicHandlers.get(topicId).addSubscriber(dataQueue);
    }

}
