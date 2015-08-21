package com.rimusdesign.messaging.server.connection.policy;


import java.io.DataInputStream;
import java.io.DataOutputStream;


/**
 * Concrete implementations of this interface will allow for encapsulation
 * of behaviour logic for each connection type.
 *
 * @author Rimas Krivickas.
 */
public interface ConnectionPolicy {


    /**
     * A static array of frame types the concrete implementation accepts.
     *
     * @return - int array with values that must match some or none of the values defined in {@see com.rimusdesign.messaging.core.codecs.protocol.constants.DataFrameType} class.
     */
    int[] acceptsFrameTypes ();


    /**
     * A static array of frame types the concrete implementation is allowed to send.
     *
     * @return - int array with values that must match some or none of the values defined in {@see com.rimusdesign.messaging.core.codecs.protocol.constants.DataFrameType} class.
     */
    int[] sendsFrameTypes ();


    /**
     * A static array of option types the concrete implementation accepts.
     *
     * @return - int array with values that must match some or none of the values defined in {@see com.rimusdesign.messaging.core.codecs.protocol.constants.OptionType} class.
     */
    int[] acceptsOptions ();


    /**
     * Used to initialise the context, use it to instantiate any required objects. Call 'super()' first.
     * Generally called by factory.
     */
    void init ();


    /**
     * Concrete implementations should implement this method.
     *
     * @param inputStream  an actual reference to the socket input stream
     * @param outputStream an actual reference to the socket output stream
     */
    void handle (DataInputStream inputStream, DataOutputStream outputStream);

}
