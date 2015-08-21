package com.rimusdesign.messaging.core.codecs;


/**
 * This exception is thrown when input stream can't be read completely and 'read()' returns '-1'.
 *
 * @author Rimas Krivickas.
 */
public class IncompleteStreamException extends Exception {


    public IncompleteStreamException () {

        super("Stream was closed while trying to read from it. Incomplete data received.");
    }
}
