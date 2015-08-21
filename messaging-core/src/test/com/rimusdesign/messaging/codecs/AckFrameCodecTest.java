package com.rimusdesign.messaging.codecs;


import com.rimusdesign.messaging.core.codecs.AckFrameCodec;
import com.rimusdesign.messaging.core.codecs.HeaderCodec;
import com.rimusdesign.messaging.core.codecs.protocol.constants.Acknowledgement;
import com.rimusdesign.messaging.core.codecs.protocol.constants.DataFrameType;
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
public class AckFrameCodecTest extends ConnectedStreamsHelper {


    private UUID uid;
    private ByteBuffer byteBuffer;


    @Before
    public void setUp () throws Exception {

        instantiateStreams();

        // Make uid
        uid = UUID.randomUUID();

        // Encode frame with details provided
        AckFrameCodec.encode(dataOutputStream, uid, Acknowledgement.SUCCESS);

        // Decode frame
        byteBuffer = AckFrameCodec.decode(dataInputStream);
    }


    @After
    public void tearDown () throws Exception {

        closeStreams();
    }


    @Test
    public void testAckCodec () throws Exception {

        Assert.assertEquals("Connection ID value should decode correctly", Acknowledgement.SUCCESS, AckFrameCodec.getAckValue(byteBuffer));
    }


    @Test
    public void testHeaderPreservation () throws Exception {

        int expectedPayloadLength = 1;

        // Check if header values get retrieved correctly
        Assert.assertEquals("Payload length value should decode correctly", expectedPayloadLength, HeaderCodec.getPayloadLength(byteBuffer));
        Assert.assertEquals("Frame type value should decode correctly", DataFrameType.ACKNOWLEDGEMENT, HeaderCodec.getFrameType(byteBuffer));
        Assert.assertEquals("UUID value should decode correctly", uid.toString(), HeaderCodec.getUid(byteBuffer).toString());
    }
}