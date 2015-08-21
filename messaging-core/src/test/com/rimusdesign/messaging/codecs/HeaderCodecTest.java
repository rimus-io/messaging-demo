package com.rimusdesign.messaging.codecs;


import com.rimusdesign.messaging.core.codecs.HeaderCodec;
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
public class HeaderCodecTest extends ConnectedStreamsHelper {


    private int frameType;
    private UUID uuid;
    private int payloadLength;


    @Before
    public void setUp () throws Exception {

        frameType = DataFrameType.ACKNOWLEDGEMENT;
        uuid = UUID.randomUUID();
        payloadLength = 8008;

        instantiateStreams();
    }


    @After
    public void tearDown () throws Exception {

        closeStreams();
    }


    @Test
    public void testHeaderCodec () throws Exception {

        // Encode header with details provided
        HeaderCodec.encode(dataOutputStream, frameType, uuid, payloadLength);

        // Decode header
        ByteBuffer byteBuffer = HeaderCodec.decode(dataInputStream);

        // Check if values get retrieved correctly in any order
        Assert.assertEquals("Payload length value should decode correctly", payloadLength, HeaderCodec.getPayloadLength(byteBuffer));
        Assert.assertEquals("Frame type value should decode correctly", frameType, HeaderCodec.getFrameType(byteBuffer));
        Assert.assertEquals("UUID value should decode correctly", uuid.toString(), HeaderCodec.getUid(byteBuffer).toString());
    }


}