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
public class ResponseSendable extends MessageSendable implements Response {

    private String _protocol;
    private String _statusCode;
    private String _statusName;

    public ResponseSendable(String protocol, String statusCode, String statusName, Map<String, List<Object>> headers, InputStream body) {
        super(headers, body);
        _protocol = protocol;
        _statusCode = statusCode;
        _statusName = statusName;
    }

    @Override
    public String getProtocol() {
        return _protocol;
    }

    public void setProtocol(String protocol) {
        this._protocol = protocol;
    }

    @Override
    public String getStatusCode() {
        return _statusCode;
    }

    public void setStatusCode(String statusCode) {
        this._statusCode = statusCode;
    }

    @Override
    public String getStatusName() {
        return _statusName;
    }

    public void setStatusName(String statusName) {
        this._statusName = statusName;
    }

    @Override
    String getFirstLine() {
        return _protocol + " " + _statusCode + " " + _statusName;
    }

}
