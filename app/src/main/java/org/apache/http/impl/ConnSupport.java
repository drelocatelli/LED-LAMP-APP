package org.apache.http.impl;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import org.apache.http.config.ConnectionConfig;

/* loaded from: classes.dex */
public final class ConnSupport {
    public static CharsetDecoder createDecoder(ConnectionConfig connectionConfig) {
        if (connectionConfig == null) {
            return null;
        }
        Charset charset = connectionConfig.getCharset();
        CodingErrorAction malformedInputAction = connectionConfig.getMalformedInputAction();
        CodingErrorAction unmappableInputAction = connectionConfig.getUnmappableInputAction();
        if (charset != null) {
            CharsetDecoder newDecoder = charset.newDecoder();
            if (malformedInputAction == null) {
                malformedInputAction = CodingErrorAction.REPORT;
            }
            CharsetDecoder onMalformedInput = newDecoder.onMalformedInput(malformedInputAction);
            if (unmappableInputAction == null) {
                unmappableInputAction = CodingErrorAction.REPORT;
            }
            return onMalformedInput.onUnmappableCharacter(unmappableInputAction);
        }
        return null;
    }

    public static CharsetEncoder createEncoder(ConnectionConfig connectionConfig) {
        Charset charset;
        if (connectionConfig == null || (charset = connectionConfig.getCharset()) == null) {
            return null;
        }
        CodingErrorAction malformedInputAction = connectionConfig.getMalformedInputAction();
        CodingErrorAction unmappableInputAction = connectionConfig.getUnmappableInputAction();
        CharsetEncoder newEncoder = charset.newEncoder();
        if (malformedInputAction == null) {
            malformedInputAction = CodingErrorAction.REPORT;
        }
        CharsetEncoder onMalformedInput = newEncoder.onMalformedInput(malformedInputAction);
        if (unmappableInputAction == null) {
            unmappableInputAction = CodingErrorAction.REPORT;
        }
        return onMalformedInput.onUnmappableCharacter(unmappableInputAction);
    }
}
