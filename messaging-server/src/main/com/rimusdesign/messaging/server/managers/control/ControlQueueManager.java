package com.rimusdesign.messaging.server.managers.control;


import java.util.concurrent.LinkedBlockingQueue;


/**
 * @author Rimas Krivickas.
 */
public class ControlQueueManager implements Runnable {


    private TopicRouter router;
    private LinkedBlockingQueue controlQueue;


    public ControlQueueManager (TopicRouter router, LinkedBlockingQueue controlQueue) {

        this.router = router;

        this.controlQueue = controlQueue;
    }


    public void run () {

        ControlMessage message;
        try {
            while ((message = (ControlMessage) controlQueue.take()) != null) {

                handlerControlMessage(message);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void handlerControlMessage (ControlMessage message) {

        switch (message.getType()) {
            case REGISTER_TOPIC_PUBLISHER:
                router.handleTopicPublisher(message.getTopicId(), message.getDataQueue());
                break;
            case REGISTER_TOPIC_SUBSCRIBER:
                router.handleTopicSubscriber(message.getTopicId(), message.getDataQueue());
                break;
        }
    }


}
