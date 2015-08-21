package com.rimusdesign.messaging.codecs;


import com.rimusdesign.messaging.core.codecs.HeaderCodec;
import com.rimusdesign.messaging.core.codecs.OptionFrameCodec;
import com.rimusdesign.messaging.core.codecs.protocol.constants.DataFrameType;
import com.rimusdesign.messaging.core.codecs.protocol.constants.OptionType;
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
public class OptionFrameCodecTest extends ConnectedStreamsHelper {


    private int optionType;
    private UUID uid;
    private ByteBuffer byteBuffer;
    private long longValue;
    private String stringValue;


    @Before
    public void setUp () throws Exception {

        optionType = OptionType.TOPIC_SUBSCRIBE;
        stringValue = "helloworld topic";

        instantiateStreams();
    }


    @After
    public void tearDown () throws Exception {

        closeStreams();
    }


    @Test
    public void testOptionCodecWithStringValue () throws Exception {

        // Encode frame with details provided
        uid = OptionFrameCodec.encode(dataOutputStream, optionType, stringValue.getBytes());

        // Decode frame
        byteBuffer = OptionFrameCodec.decode(dataInputStream);

        // Check if values get retrieved correctly
        Assert.assertEquals("Option type value should decode correctly", optionType, OptionFrameCodec.getOptionType(byteBuffer));
        Assert.assertEquals("Option value should decode correctly", stringValue, new String(OptionFrameCodec.getOptionValue(byteBuffer)));
    }


    @Test
    public void testOptionCodecWithLongValue () throws Exception {

        longValue = 123456789;

        // Encode frame with details provided
        uid = OptionFrameCodec.encode(dataOutputStream, optionType, ByteBuffer.allocate(Long.SIZE).putLong(longValue).array());

        // Decode frame
        byteBuffer = OptionFrameCodec.decode(dataInputStream);

        Assert.assertEquals("Option type value should decode correctly", longValue, ByteBuffer.wrap(OptionFrameCodec.getOptionValue(byteBuffer)).getLong());
    }


    @Test
    public void testHeaderPreservation () throws Exception {

        // Encode frame with details provided
        uid = OptionFrameCodec.encode(dataOutputStream, optionType, stringValue.getBytes());

        // Decode frame
        byteBuffer = OptionFrameCodec.decode(dataInputStream);

        int expectedPayloadLength = stringValue.getBytes().length + 1; // '+ 1' Accounts for option type flag

        // Check if header values get retrieved correctly
        Assert.assertEquals("Payload length value should decode correctly", expectedPayloadLength, HeaderCodec.getPayloadLength(byteBuffer));
        Assert.assertEquals("Frame type value should decode correctly", DataFrameType.OPTION, HeaderCodec.getFrameType(byteBuffer));
        Assert.assertEquals("UUID value should decode correctly", uid.toString(), HeaderCodec.getUid(byteBuffer).toString());
    }
}
