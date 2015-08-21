Messaging framework and a simple chat app
=========================================


IMPORTANT
---------


This is not a production ready code and should only be used for educational purposes.


Messaging framework protocol (mfp) v.1
--------------------------------------


Protocol described in ABNF(Augmented Backus-Naur Form) grammar:

        mfp                   = *data-frame
        
        data-frame            = header (handshake / option / message / acknowledgement)
        
        header                = frame-type frame-uid payload-length
        frame-type            = HANDSHAKE / OPTION / MESSAGE / ACKNOWLEDGEMENT
        HANDSHAKE             = %x00
        OPTION                = %x01
        MESSAGE               = %x02
        ACKNOWLEDGEMENT       = %x03
        frame-uid             = 16OCTET ; 128 bit UUID
        payload-length        = 1*1001OCTET ; Limiting this to ~500 chars (char = 16bits). The length value should combine the length of flags etc. The vale is length in bytes (OCTET = 1byte).
        
        handshake             = version connection-type connection-id
        version               = %x01
        connection-type       = PUB / SUB
        PUB                   = %x00
        SUB                   = %x01
        connection-id         = 8*64OCTET ; Equal to value between 4 and 32 chars long.
        
        option                = option-type option-value
        option-type           = TOPICSET / TOPICSUBSCRIBE / TOPICUNSUBSCRIBE / TOPICUNSUBSCRIBEALL
        TOPICSET              = %x00
        TOPICSUBSCRIBE        = %x01
        TOPICUNSUBSCRIBE      = %x02
        TOPICUNSUBSCRIBEALL   = %x03
        option-value          = *128OCTET ; Equal to maximum 64 chars.
        
        message               = message-type acknowledge body
        message-type          = TEXT / RAWDATA
        TEXT                  = %x00
        RAWDATA               = %x01
        acknowledge           = YES / NO
        YES                   = %x00
        NO                    = %x01
        body                  = *1000OCTET ; Value to match 'payload-length'.
        
        acknowledgement       = SUCCESS / FAIL ; To link 'acknowledgement' to original 'data-frame' use 'frame-uid' provided in 'header'.
        SUCCESS               = %x00
        FAIL                  = %x01


Reference text
--------------


To give a rough idea of how much data can be sent per message - this paragraph contains 445 character:

> Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.
