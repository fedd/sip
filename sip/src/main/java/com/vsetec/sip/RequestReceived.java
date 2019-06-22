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

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author fedd
 */
public class RequestReceived extends MessageReceived implements Request {

    private final String _protocol;
    private final String _method;
    private final String _uri;

    public RequestReceived(InputStream source) throws IOException {
        super(source);
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

    @Override
    public RequestSendable getToForward(String via) {
        RequestSendable ret = new RequestSendable(_protocol, _method, _uri, getHeaders(), getBody());
        LinkedHashMap<String, Object> headers = ret.getHeaders();
        List<String> vias = (List<String>) headers.get("Via");
        vias.add(0, via);
        String maxForwards = (String) headers.get("Max-Forwards");
        if (maxForwards == null) {
            headers.put("Max-Forwards", "70");
        } else {
            try {
                int mfi = Integer.parseInt(maxForwards);
                mfi--;
                headers.put("Max-Forwards", Integer.toString(mfi));
            } catch (NumberFormatException e) {
                headers.put("Max-Forwards", "70");
            }
        }
        return ret;
    }

    public ResponseSendable getToRespond(String statusCode, String statusName, InputStream body) {
        return new ResponseSendable(_protocol, statusCode, statusName, getHeaders(), body);
    }

}
