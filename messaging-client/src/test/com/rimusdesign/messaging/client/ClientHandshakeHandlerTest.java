package com.rimusdesign.messaging.client;


import com.rimusdesign.messaging.client.connection.ClientHandshakeHandler;
import com.rimusdesign.messaging.core.codecs.protocol.constants.Acknowledgement;
import com.rimusdesign.messaging.core.codecs.protocol.constants.ConnectionType;
import com.rimusdesign.messaging.test.doubles.AckResponderThread;
import com.rimusdesign.messaging.test.doubles.AckResponderWithUUIDThread;
import com.rimusdesign.messaging.test.doubles.FrameEchoResponderThread;
import com.rimusdesign.messaging.test.helpers.PairOfConnectedStreamsHelper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;


/**
 * @author Rimas Krivickas.
 */
public class ClientHandshakeHandlerTest extends PairOfConnectedStreamsHelper {


    private ClientHandshakeHandler handler;


    @Before
    public void setUp () throws Exception {

        instantiateStreams();

        // Instantiate handshake handler
        handler = new ClientHandshakeHandler(ConnectionType.PUB, "test_connection");
    }


    @After
    public void tearDown () throws Exception {

        closeStreams();
    }


    @Test
    public void testHandshakeSuccessful () throws Exception {

        // Launch a thread to simulate server response
        new Thread(new AckResponderThread(dataInputStreamB, dataOutputStreamA, Acknowledgement.SUCCESS)).start();
        Assert.assertTrue("Frame with correct acknowledgement UUID should be successful", handler.handshakeSuccessful(dataInputStreamA, dataOutputStreamB));
    }


    @Test
    public void testHandshakeFailWrongFrameReturned () throws Exception {

        // Launch a thread to simulate server responding with incorrect data
        new Thread(new FrameEchoResponderThread(dataInputStreamB, dataOutputStreamA)).start();
        Assert.assertFalse("Heandshake request must be followed by acknowledgement, if any other frame is returned method should return 'false'", handler.handshakeSuccessful(dataInputStreamA, dataOutputStreamB));
    }


    @Test
    public void testHandshakeFailWrongUid () throws Exception {

        // Launch a thread to simulate server responding with wrong uid
        new Thread(new AckResponderWithUUIDThread(dataInputStreamB, dataOutputStreamA, UUID.randomUUID(), Acknowledgement.SUCCESS)).start();
        Assert.assertFalse("If UUID that comes back with acknowledgement doesn't match handshake UUID method should return 'false'", handler.handshakeSuccessful(dataInputStreamA, dataOutputStreamB));
    }


    @Test
    public void testHandshakeFailIOException () throws Exception {
        // Close streams to cause IOException
        closeStreams();

        Assert.assertFalse("Method should return 'false' if IOException is encountered", handler.handshakeSuccessful(dataInputStreamA, dataOutputStreamB));
    }


    @Test
    public void testHandshakeAckFail () throws Exception {

        // Launch a thread to simulate server response
        new Thread(new AckResponderThread(dataInputStreamB, dataOutputStreamA, Acknowledgement.FAIL)).start();
        Assert.assertFalse("If acknowledgement value is 'fail', method should return 'false'", handler.handshakeSuccessful(dataInputStreamA, dataOutputStreamB));
    }
}