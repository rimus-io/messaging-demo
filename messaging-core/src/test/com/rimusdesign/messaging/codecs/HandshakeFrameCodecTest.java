package com.rimusdesign.messaging.codecs;


import com.rimusdesign.messaging.core.codecs.HandshakeFrameCodec;
import com.rimusdesign.messaging.core.codecs.HeaderCodec;
import com.rimusdesign.messaging.core.codecs.protocol.constants.ConnectionType;
import com.rimusdesign.messaging.core.codecs.protocol.constants.DataFrameType;
import com.rimusdesign.messaging.core.codecs.protocol.constants.Version;
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
public class HandshakeFrameCodecTest extends ConnectedStreamsHelper {


    private int connectionType;
    private char[] connectionId;
    private UUID uid;
    private ByteBuffer byteBuffer;


    @Before
    public void setUp () throws Exception {

        connectionType = ConnectionType.SUB;
        connectionId = "helloworld".toCharArray();

        instantiateStreams();

        // Encode frame with details provided
        uid = HandshakeFrameCodec.encode(dataOutputStream, connectionType, connectionId);

        // Decode frame
        byteBuffer = HandshakeFrameCodec.decode(dataInputStream);
    }


    @After
    public void tearDown () throws Exception {

        closeStreams();
    }


    @Test
    public void testConnectionCodec () throws Exception {

        // Check if values get retrieved correctly
        Assert.assertEquals("Connection ID value should decode correctly", new String(connectionId), new String(HandshakeFrameCodec.getConnectionId(byteBuffer)));
        Assert.assertEquals("Connection should return correct protocol version", Version.protocolVersion(), HandshakeFrameCodec.getProtocolVersion(byteBuffer));
        Assert.assertEquals("Connection type value should decode correctly", connectionType, HandshakeFrameCodec.getConnectionType(byteBuffer));
    }


    @Test
    public void testConnectionCodecWithStringId () throws Exception {

        String stringId = "helloworld string";

        // Encode frame with details provided
        UUID stringTestUid = HandshakeFrameCodec.encode(dataOutputStream, connectionType, stringId);

        // Decode frame
        ByteBuffer stringTestByteBuffer = HandshakeFrameCodec.decode(dataInputStream);

        // Check if values get retrieved correctly
        Assert.assertEquals("Connection ID value should decode correctly", stringId, new String(HandshakeFrameCodec.getConnectionId(stringTestByteBuffer)));
        Assert.assertEquals("Connection should return correct protocol version", Version.protocolVersion(), HandshakeFrameCodec.getProtocolVersion(stringTestByteBuffer));
        Assert.assertEquals("Connection type value should decode correctly", connectionType, HandshakeFrameCodec.getConnectionType(stringTestByteBuffer));
    }


    @Test
    public void testHeaderPreservation () throws Exception {

        int expectedPayloadLength = (connectionId.length * 2) + 2; // '*2' to get length in bytes

        // Check if header values get retrieved correctly
        Assert.assertEquals("Payload length value should decode correctly", expectedPayloadLength, HeaderCodec.getPayloadLength(byteBuffer));
        Assert.assertEquals("Frame type value should decode correctly", DataFrameType.HANDSHAKE, HeaderCodec.getFrameType(byteBuffer));
        Assert.assertEquals("UUID value should decode correctly", uid.toString(), HeaderCodec.getUid(byteBuffer).toString());
    }
}