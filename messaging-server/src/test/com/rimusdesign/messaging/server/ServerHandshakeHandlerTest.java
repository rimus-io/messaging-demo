package com.rimusdesign.messaging.server;


import com.rimusdesign.messaging.core.codecs.AckFrameCodec;
import com.rimusdesign.messaging.core.codecs.HandshakeFrameCodec;
import com.rimusdesign.messaging.core.codecs.HeaderCodec;
import com.rimusdesign.messaging.core.codecs.protocol.constants.Acknowledgement;
import com.rimusdesign.messaging.core.codecs.protocol.constants.ConnectionType;
import com.rimusdesign.messaging.core.codecs.protocol.constants.DataFrameType;
import com.rimusdesign.messaging.core.codecs.protocol.constants.Version;
import com.rimusdesign.messaging.server.connection.ServerHandshakeHandler;
import com.rimusdesign.messaging.test.helpers.PairOfConnectedStreamsHelper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;


/**
 * @author Rimas Krivickas.
 */
public class ServerHandshakeHandlerTest extends PairOfConnectedStreamsHelper {


    private ServerHandshakeHandler handler;
    private String connectionId;


    @Before
    public void setUp () throws Exception {

        instantiateStreams();

        connectionId = "helloworld";

        handler = new ServerHandshakeHandler();
    }


    @After
    public void tearDown () throws Exception {

        closeStreams();
    }


    @Test
    public void testHandshakeSuccessful () throws Exception {

        // Write handshake frame into the stream
        HandshakeFrameCodec.encode(dataOutputStreamA, ConnectionType.PUB, connectionId);

        Assert.assertTrue("Handshake should succeed", handler.handshakeSuccessful(dataInputStreamA, dataOutputStreamB));

        // Should contain correct connection details after returning successfully
        Assert.assertEquals("Should contain correct connection type after returning successfully", ConnectionType.PUB, handler.getConnectionDetails().getConnectionType());
        Assert.assertEquals("Should contain correct connection ID after returning successfully", connectionId, handler.getConnectionDetails().getConnectionId());
    }


    @Test
    public void testHandshakeFailIOException () throws Exception {

        // Close streams to cause IOException
        closeStreams();

        Assert.assertFalse("Should return 'false' if IOException is encountered ", handler.handshakeSuccessful(dataInputStreamA, dataOutputStreamB));
    }


    @Test
    public void testHandshakeFailWrongFrame () throws Exception {
        // Write a non acknowledgement frame into the stream
        AckFrameCodec.encode(dataOutputStreamA, UUID.randomUUID(), Acknowledgement.SUCCESS);

        Assert.assertFalse("Should return 'false' if receives non handshake frame", handler.handshakeSuccessful(dataInputStreamA, dataOutputStreamB));
    }


    @Test
    public void testHandshakeFailWrongProtocol () throws Exception {

        // Write fake data into the stream
        HeaderCodec.encode(dataOutputStreamA, DataFrameType.HANDSHAKE, UUID.randomUUID(), 10); // 2 bytes for flags and 8 bytes for 'abcd'
        dataOutputStreamA.write(Version.protocolVersion() + 1); // We use '+1' to make sure this test is valid once version changes
        dataOutputStreamA.write(ConnectionType.SUB);
        dataOutputStreamA.writeChars("abcd");

        Assert.assertFalse("Should return 'false' if handshake frame with invalid protocol version is received", handler.handshakeSuccessful(dataInputStreamA, dataOutputStreamB));
    }


    @Test
    public void testHandshakeFailUnknownType () throws Exception {

        // Write handshake frame into the stream
        HandshakeFrameCodec.encode(dataOutputStreamA, -1, connectionId); // '-1' will never become a valid connection type

        Assert.assertFalse("Should return 'false' if connection type is not recognised", handler.handshakeSuccessful(dataInputStreamA, dataOutputStreamB));
    }


    @Test
    public void testHandshakeFailIdTooShort () throws Exception {

        // Write handshake frame into the stream
        HandshakeFrameCodec.encode(dataOutputStreamA, ConnectionType.PUB, "abc"); // Minimum length is 4

        Assert.assertFalse("Should return 'false' if connection id is too short", handler.handshakeSuccessful(dataInputStreamA, dataOutputStreamB));
    }


    @Test
    public void testHandshakeFailIdTooLong () throws Exception {

        String longConnectionId = "_123456789_123456789_123456789abc";// Maximum length is 32
        // Write handshake frame into the stream
        HandshakeFrameCodec.encode(dataOutputStreamA, ConnectionType.PUB, longConnectionId);

        Assert.assertFalse("Should return 'false' if connection id is too long", handler.handshakeSuccessful(dataInputStreamA, dataOutputStreamB));
    }


}