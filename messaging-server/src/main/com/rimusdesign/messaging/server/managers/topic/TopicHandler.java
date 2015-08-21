package com.rimusdesign.messaging.server.managers.topic;


import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * @author Rimas Krivickas.
 */
public class TopicHandler {


    private boolean isRunning;
    private CopyOnWriteArrayList<ConcurrentLinkedQueue> subscribers = new CopyOnWriteArrayList<ConcurrentLinkedQueue>();
    private CopyOnWriteArrayList<ConcurrentLinkedQueue> publishers = new CopyOnWriteArrayList<ConcurrentLinkedQueue>();


    public void addPublisher (ConcurrentLinkedQueue queue) {

        //System.out.println("TopicHandler.addPublisher()");
        publishers.add(queue);
        if (!isRunning) start();
    }


    public void addSubscriber (ConcurrentLinkedQueue queue) {

        //System.out.println("TopicHandler.addSubscriber()");
        subscribers.add(queue);
        if (!isRunning) start();
    }


    private void start () {

        // Mark as running
        isRunning = true;

        new Thread(new TopicHandlerProcessor(subscribers, publishers)).start();
    }


}


class TopicHandlerProcessor implements Runnable {


    private CopyOnWriteArrayList<ConcurrentLinkedQueue> subscribers;
    private CopyOnWriteArrayList<ConcurrentLinkedQueue> publishers;


    public TopicHandlerProcessor (CopyOnWriteArrayList<ConcurrentLinkedQueue> subscribers,
                                  CopyOnWriteArrayList<ConcurrentLinkedQueue> publishers) {

        this.subscribers = subscribers;
        this.publishers = publishers;
    }


    public void run () {

        Iterator<ConcurrentLinkedQueue> pubIterator;
        Iterator<ConcurrentLinkedQueue> subIterator;
        ByteBuffer message;
        while (hasParticipants()) {
            pubIterator = publishers.iterator();
            while (pubIterator.hasNext()) {
                message = (ByteBuffer) pubIterator.next().poll();
                if (message != null) {
                    subIterator = subscribers.iterator();
                    while (subIterator.hasNext()) {
                        subIterator.next().offer(message);
                    }
                    //System.out.println("Message distributed: " + MessageFrameCodec.getTextBody(message));
                }
            }
        }
        // TODO Decide what to do when this finishes looping
    }


    private boolean hasParticipants () {

        if (!publishers.isEmpty() || !subscribers.isEmpty()) return true;
        return false;
    }
}