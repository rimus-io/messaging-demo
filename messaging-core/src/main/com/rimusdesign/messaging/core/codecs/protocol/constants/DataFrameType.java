package com.rimusdesign.messaging.core.codecs.protocol.constants;


/**
 * Constants to be used when encoding/decoding 'frame-type' as per protocol.
 *
 * @author Rimas Krivickas.
 */
public class DataFrameType {


    public static final int HANDSHAKE = 0x00;
    public static final int OPTION = 0x01;
    public static final int MESSAGE = 0x02;
    public static final int ACKNOWLEDGEMENT = 0x03;
}
