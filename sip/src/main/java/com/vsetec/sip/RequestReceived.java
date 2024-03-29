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
public class RequestReceived extends MessageReceived implements Request {

    private final String _protocol;
    private final String _method;
    private final String _uri;

    RequestReceived(String[] splitFirstLine, Map<String, List<Object>> headers, InputStream body) {
        super(headers, body);
        _method = splitFirstLine[0];
        _uri = splitFirstLine[1];
        _protocol = splitFirstLine[2];
    }

    @Override
    public boolean isRequest() {
        return true;
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

    @Override
    public RequestSendable getToForward() {
        RequestSendable ret = new RequestSendable(_protocol, _method, _uri, getHeaders(), getBody());
        Map<String, List<Object>> headers = ret.getHeaders();
        List<Object> maxForwards = headers.get("Max-Forwards");
        if (maxForwards.isEmpty()) {
            maxForwards.add("70");
        } else {
            try {
                int mfi = Integer.parseInt((String) maxForwards.remove(0));
                mfi--;
                maxForwards.add(Integer.toString(mfi));
            } catch (NumberFormatException e) {
                maxForwards.add("70");
            }
        }
        return ret;
    }

    public ResponseSendable getToRespond(String statusCode, String statusName, InputStream body) {
        return new ResponseSendable(_protocol, statusCode, statusName, getHeaders(), body);
    }

}
