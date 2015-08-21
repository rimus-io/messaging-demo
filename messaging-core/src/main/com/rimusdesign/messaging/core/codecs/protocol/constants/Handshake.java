package com.rimusdesign.messaging.core.codecs.protocol.constants;


/**
 * Defines length limits for 'connection-id' values as per protocol.
 *
 * @author Rimas Krivickas.
 */
public class Handshake {


    public static final int MIN_CONNECTION_ID_LEN = 4;
    public static final int MAX_CONNECTION_ID_LEN = 32;
}
