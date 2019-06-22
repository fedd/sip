/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vsetec.sip;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 *
 * @author fedd
 */
public class RequestToSend extends AbstractMessageToSend implements Request {

    private final String _protocol;
    private final String _method;
    private final String _uri;

    public RequestToSend(String firstLine, Map<String, Object> headers, InputStream body) throws IOException {
        super(firstLine, headers, body);
        String[] split = getFirstLine().split("\\s+");
        _method = split[0];
        _uri = split[1];
        _protocol = split[2];
    }

    @Override
    public String getProtocol() {
        return _protocol;
    }

    @Override
    public String getMethod() {
        return _method;
    }

    @Override
    public String getUri() {
        return _uri;
    }
}
