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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 *
 * @author fedd
 */
public abstract class AbstractMessageReceived implements MessageReceived {

    private final String _firstLine;
    private final LinkedHashMap<String, Object> _headers = new LinkedHashMap();
    private final InputStream _body;

    public AbstractMessageReceived(InputStream source) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(source, "UTF-8"));

        // read first line
        _firstLine = reader.readLine();

        // read headers
        String header = reader.readLine();
        while (header.length() > 0) {
            int idx = header.indexOf(":");
            if (idx == -1) {
                continue;
            }

            String headerName = header.substring(0, idx);
            String headerValue = header.substring(idx + 1, header.length());
            if (headerName.equals("Via")) { // multiple vias
                ArrayList<String> vias = (ArrayList<String>) _headers.get("Via");
                if (vias == null) {
                    vias = new ArrayList<>(4);
                    _headers.put("Via", vias);
                }
                vias.add(headerValue);
            } else {
                _headers.put(headerName, headerValue);
            }

            header = reader.readLine();
        }

        // make the rest as body
        _body = source;

    }

    @Override
    public final String getFirstLine() {
        return _firstLine;
    }

    @Override
    public LinkedHashMap<String, Object> getHeaders() {
        return _headers;
    }

    @Override
    public InputStream getBody() {
        return _body;
    }

}