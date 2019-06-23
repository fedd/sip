/*
 * Copyright 2019 fedd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.vsetec.sip;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 *
 * @author fedd
 */
public class RequestSendable extends MessageSendable implements Request {

    private String _protocol;
    private String _method;
    private String _uri;

    public RequestSendable(String protocol, String method, String uri, Map<String, List<Object>> headers, InputStream body) {
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

    public void setProtocol(String protocol) {
        this._protocol = protocol;
    }

    @Override
    public String getMethod() {
        return _method;
    }

    public void setMethod(String method) {
        this._method = method;
    }

    @Override
    public String getUri() {
        return _uri;
    }

    public void setUri(String uri) {
        this._uri = uri;
    }

}
