package com.rimusdesign.messaging.core.codecs.protocol.constants;


/**
 * Constants to be used when encoding/decoding 'option-type' as per protocol.
 *
 * @author Rimas Krivickas.
 */
public class OptionType {


    public static final int TOPIC_SET = 0x00;
    public static final int TOPIC_SUBSCRIBE = 0x01;
    public static final int TOPIC_UNSUBSCRIBE = 0x02;
    public static final int TOPIC_UNSUBSCRIBE_ALL = 0x03;
}
