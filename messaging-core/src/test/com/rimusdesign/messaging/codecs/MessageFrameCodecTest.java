package com.rimusdesign.messaging.codecs;


import com.rimusdesign.messaging.core.codecs.HeaderCodec;
import com.rimusdesign.messaging.core.codecs.MessageFrameCodec;
import com.rimusdesign.messaging.core.codecs.protocol.constants.DataFrameType;
import com.rimusdesign.messaging.core.codecs.protocol.constants.MessageAck;
import com.rimusdesign.messaging.core.codecs.protocol.constants.MessageType;
import com.rimusdesign.messaging.test.helpers.ConnectedStreamsHelper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.UUID;


/**
 * @author Rimas Krivickas.
 */
public class MessageFrameCodecTest extends ConnectedStreamsHelper {


    private UUID uid;
    private ByteBuffer byteBuffer;
    private String stringValue;
    private byte[] rawValue;
    private int messageAckFlag;
    private int messageType;


    @Before
    public void setUp () throws Exception {

        messageType = MessageType.TEXT;
        messageAckFlag = MessageAck.YES;
        stringValue = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";

        instantiateStreams();
    }


    @After
    public void tearDown () throws Exception {

        closeStreams();
    }


    @Test
    public void testMessageCodecWithRawData () throws Exception {


        rawValue = ByteBuffer.allocate(Long.SIZE).putLong(1234567890).array();

        // Encode frame with details provided
        uid = MessageFrameCodec.encode(dataOutputStream, MessageType.RAWDATA, MessageAck.YES, rawValue);

        // Decode frame
        byteBuffer = MessageFrameCodec.decode(dataInputStream);

        // Check if values get retrieved correctly
        Assert.assertEquals("Message body should decode correctly", ByteBuffer.wrap(rawValue).getLong(), ByteBuffer.wrap(MessageFrameCodec.getBody(byteBuffer)).getLong());
    }


    @Test
    public void testMessageCodecWithTextData () throws Exception {

        // Encode frame with details provided
        uid = MessageFrameCodec.encode(dataOutputStream, messageType, messageAckFlag, stringValue);

        // Decode frame
        byteBuffer = MessageFrameCodec.decode(dataInputStream);

        // Check if values get retrieved correctly
        Assert.assertEquals("Message body should decode correctly", stringValue, new String(MessageFrameCodec.getBody(byteBuffer)));
        Assert.assertEquals("Message type value should decode correctly", messageType, MessageFrameCodec.getMessageType(byteBuffer));
        Assert.assertEquals("Acknowledge flag should decode correctly", messageAckFlag, MessageFrameCodec.getAckFlag(byteBuffer));
        Assert.assertEquals("Method to get data as string should decode correctly", stringValue, MessageFrameCodec.getTextBody(byteBuffer));

    }


    @Test
    public void testHeaderPreservation () throws Exception {

        // Encode frame with details provided
        uid = MessageFrameCodec.encode(dataOutputStream, messageType, messageAckFlag, stringValue);

        // Decode frame
        byteBuffer = MessageFrameCodec.decode(dataInputStream);

        // Work out payload length
        int expectedPayloadLength = stringValue.getBytes().length + 2; // '+ 2' Accounts for flags

        // Check if header values get retrieved correctly
        Assert.assertEquals("Payload length value should decode correctly", expectedPayloadLength, HeaderCodec.getPayloadLength(byteBuffer));
        Assert.assertEquals("Frame type value should decode correctly", DataFrameType.MESSAGE, HeaderCodec.getFrameType(byteBuffer));
        Assert.assertEquals("UUID value should decode correctly", uid.toString(), HeaderCodec.getUid(byteBuffer).toString());
    }
}