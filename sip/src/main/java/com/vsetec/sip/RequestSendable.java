/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vsetec.sip;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author fedd
 */
public class RequestSendable extends MessageSendable implements Request {

    private final String _protocol;
    private final String _method;
    private final String _uri;

    public RequestSendable(String protocol, String method, String uri, LinkedHashMap<String, List<String>> headers, InputStream body) {
        super(headers, body);
        _method = method;
        _uri = uri;
        _protocol = protocol;
    }

    @Override
    String getFirstLine() {
        return _method + " " + _uri + " " + _protocol;
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
